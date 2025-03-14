package in.oasys.gatepass.service;

import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.WriterException;

import in.oasys.gatepass.entity.GatePassRequest;
import in.oasys.gatepass.entity.GatePassRequest.Status;
import in.oasys.gatepass.repository.GatePassRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class GatePassService implements IGatePassService {

	@Autowired
	GatePassRepository gaetpassrepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	QRCodeGenerator qrcodegenerator;

	// send the gate pass request by student
	@Override
	public GatePassRequest insert(GatePassRequest gatepassrequest) {
		// TODO Auto-generated method stub
		gatepassrequest.setStatus(GatePassRequest.Status.PENDING);
		GatePassRequest savedRequest = gaetpassrepository.save(gatepassrequest);

		// Send notification to staff
		notificationService.sendApprovalNotification("r.ashvitha2003@gmail.com", gatepassrequest.getStudentname(),
				gatepassrequest.getReason());

		return savedRequest;
	}

	// Fetch the data by students to check status of the specific student
	@Override
	public List<GatePassRequest> findstudentstatusbystudentid(String studentId) {
		// TODO Auto-generated method stub
		return gaetpassrepository.findByStudentId(studentId);
	}

	// getall pending status
	public List<GatePassRequest> findallstatus() {
		return gaetpassrepository.findByStatus(GatePassRequest.Status.PENDING);
	}

	// getallstatusdata
	public List<GatePassRequest> findall() {
		return gaetpassrepository.findAll();

	}

	// getallaprovedstatus
	public List<GatePassRequest> findallaproved() {

		return gaetpassrepository.findByStatus(GatePassRequest.Status.APPROVED);

	}

	// update the request by student
	@Override
	public GatePassRequest updateRequest(String studentId, GatePassRequest gatepassrequest) {
		List<GatePassRequest> requests = gaetpassrepository.findByStudentId(studentId);

		if (!requests.isEmpty()) {
			GatePassRequest request = requests.get(0); // Assuming one request per student at a time

			if (request.getStatus() == GatePassRequest.Status.PENDING) {
				request.setEnterdate(gatepassrequest.getEnterdate());
				request.setEntertime(gatepassrequest.getEntertime());
				return gaetpassrepository.save(request);
			} else {
				throw new RuntimeException("Request cannot be updated as it is already processed.");
			}
		} else {
			throw new RuntimeException("Request not found for studentId: " + studentId);
		}
	}

	// Cancel a pending request
	public String cancelRequest(String studentId) {
		List<GatePassRequest> requests = gaetpassrepository.findByStudentId(studentId);

		if (!requests.isEmpty()) {
			GatePassRequest request = requests.get(0); // Assuming one request per student at a time

			if (request.getStatus() == GatePassRequest.Status.PENDING) {
				request.setStatus(GatePassRequest.Status.REJECTED);
				gaetpassrepository.save(request);
				return "Request canceled successfully for studentId: " + studentId;
			} else {
				return "Request cannot be canceled as it is already processed.";
			}
		} else {
			return "No pending request found for studentId: " + studentId;
		}
	}

// Approve Request & Generate QR Code
	@Transactional
	public String approveRequest(String studentId) {
		List<GatePassRequest> requests = gaetpassrepository.findByStudentId(studentId);

		if (requests.isEmpty()) {
			return "No request found for studentId: " + studentId;
		}

		GatePassRequest request = requests.get(0);
		request.setStatus(GatePassRequest.Status.APPROVED);
		gaetpassrepository.save(request);

		// Ensure studentEmail is retrieved
		String studentEmail = request.getStudentEmail();

		// Fetch Student Email

		System.out.println(" Debug: Student Email = " + studentEmail); // Debugging purpose

		if (studentEmail == null || studentEmail.isEmpty()) {
			return "Gate Pass Approved, but email sending failed: Student email is missing.";
		}

		// Generate QR Code with both studentId and studentName
		String qrCodePath;
		try {
			qrCodePath = qrcodegenerator.generateQRCode(request.getStudentId(), request.getStudentname());
		} catch (WriterException | IOException e) {
			return "Error generating QR Code: " + e.getMessage();
		}
		try {
			notificationService.sendApprovalNotification(request.getStudentEmail(), qrCodePath);
		} catch (MessagingException e) {
			return "Gate Pass Approved, but email sending failed: " + e.getMessage();
		}

		return "Gate Pass Approved. QR Code Generated at: " + qrCodePath;
	}

// Reject Request with Reason by staff
	public String rejectRequest(String studentId, String reason) {
		List<GatePassRequest> requests = gaetpassrepository.findByStudentId(studentId);

		if (!requests.isEmpty()) {
			GatePassRequest request = requests.get(0);

			if (request.getStatus() == GatePassRequest.Status.PENDING) {
				request.setStatus(GatePassRequest.Status.REJECTED);
				gaetpassrepository.save(request);

				// Fetch the correct email
				String studentEmail = request.getStudentEmail();

				if (studentEmail == null || !studentEmail.contains("@")) {
					return "Gate Pass Rejected, but email is invalid for Student ID: " + studentId;
				}

				// Send rejection email
				notificationService.sendRejectionNotification(studentEmail, reason);
				return "Gate Pass Rejected. Notification sent.";
			} else {
				return "Request already processed.";
			}
		} else {
			return "No pending request found for studentId: " + studentId;
		}
	}

//Reapply for gate pass after rejection

	public String resubmitRequest(String studentId, GatePassRequest updatedRequest) {
		List<GatePassRequest> requests = gaetpassrepository.findByStudentId(studentId);

		if (!requests.isEmpty()) {
			GatePassRequest existingRequest = requests.get(0);

			if (existingRequest.getStatus() == GatePassRequest.Status.REJECTED) {
				// Update necessary fields while keeping old data
				existingRequest.setEnterdate(updatedRequest.getEnterdate());
				existingRequest.setEntertime(updatedRequest.getEntertime());
				existingRequest.setReason(updatedRequest.getReason());
				existingRequest.setWhometovisit(updatedRequest.getWhometovisit());
				existingRequest.setStatus(GatePassRequest.Status.PENDING); // Set status to PENDING

				gaetpassrepository.save(existingRequest);

				// Notify staff about the resubmission
				notificationService.sendApprovalNotification("r.ashvitha2003@gmail.com",
						existingRequest.getStudentname(), existingRequest.getReason());

				return "Gate Pass resubmitted successfully.";
			} else {
				return "Only rejected requests can be resubmitted.";
			}
		} else {
			return "No rejected request found for studentId: " + studentId;
		}
	}

//  Emergency Pass Request
	public GatePassRequest emergencyRequest(GatePassRequest gatepassrequest) {
		gatepassrequest.setEmergency(true); // Mark as emergency
		gatepassrequest.setStatus(GatePassRequest.Status.PENDING);
		GatePassRequest savedRequest = gaetpassrepository.save(gatepassrequest);

		// Notify Security instead of Staff
		notificationService.sendSecurityNotification("ashvithasurendhar@gmail.com", gatepassrequest.getStudentname(),
				gatepassrequest.getReason());

		return savedRequest;
	}

//  Approve Emergency Request (by Security)
	public String approveEmergencyRequest(String studentId) {
		List<GatePassRequest> requests = gaetpassrepository.findByStudentId(studentId);

		if (requests.isEmpty()) {
			return "No request found for studentId: " + studentId;
		}

		GatePassRequest request = requests.get(0);
		request.setStatus(GatePassRequest.Status.EMERGENCYAPPROVED);
		gaetpassrepository.save(request);

		// Ensure studentEmail is retrieved
		String studentEmail = request.getStudentEmail();

		// Fetch Student Email

		System.out.println(" Debug: Student Email = " + studentEmail); // Debug Print

		if (studentEmail == null || studentEmail.isEmpty()) {
			return "Emergency Gate Pass Approved, but email sending failed: Student email is missing.";
		}

		// Generate QR Code with both studentId and studentName
		String qrCodePath;
		try {
			qrCodePath = qrcodegenerator.generateQRCode(request.getStudentId(), request.getStudentname());
		} catch (WriterException | IOException e) {
			return "Error generating QR Code: " + e.getMessage();
		}
		try {
			notificationService.sendApprovalNotification(request.getStudentEmail(), qrCodePath);
		} catch (MessagingException e) {
			return "Emergency Gate Pass Approved, but email sending failed: " + e.getMessage();
		}

		return "Emergency Gate Pass Approved. QR Code Generated at: " + qrCodePath;
	}

//bulk approval

	@Transactional
	public String bulkApproveRequests(String eventId) {
		// Fetch all pending requests for the given event
		List<GatePassRequest> pendingRequests = gaetpassrepository.findByEventIdAndStatus(eventId, Status.PENDING);

		if (pendingRequests == null || pendingRequests.isEmpty()) {
			return "No pending requests found for this event.";
		}

		// Update each request status to APPROVED
		for (GatePassRequest req : pendingRequests) {
			req.setStatus(Status.APPROVED);
		}

		gaetpassrepository.saveAll(pendingRequests);

		try {
			// Generate a common QR Code for the event
			String qrCodeData = "Event ID " + eventId + "  Approved Students " + pendingRequests.size();
			String qrCodePath = qrcodegenerator.generateQRCode(qrCodeData, eventId);

			// Send notifications to all students
			for (GatePassRequest req : pendingRequests) {
				try {
					notificationService.sendApprovalNotification(req.getStudentEmail(), qrCodePath);
				} catch (MessagingException e) {
					e.printStackTrace(); // Log the error
					return "Gate Pass Approved, but email sending failed for " + req.getStudentEmail() + ": "
							+ e.getMessage();
				}
			}

			return qrCodePath; // Return QR Code Path

		} catch (IOException | WriterException e) {
			e.printStackTrace(); // Log the error
			return "Error generating QR Code";
		}
	}

}
