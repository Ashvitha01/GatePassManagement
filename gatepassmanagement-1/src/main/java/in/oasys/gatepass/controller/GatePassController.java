package in.oasys.gatepass.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.oasys.gatepass.entity.GatePassRequest;
import in.oasys.gatepass.service.GatePassService;

@RestController
@RequestMapping("/api/gatepass")
public class GatePassController {

	@Autowired
	GatePassService gatepassservice;

	// send request by students
	@PostMapping("/sendrequest")
	public GatePassRequest insert(@RequestBody GatePassRequest gatepassrequest) {
		return gatepassservice.insert(gatepassrequest);
	}

	// Fetch the data by students to check status of the specific student
	@GetMapping("/myrequests/{studentId}")
	public List<GatePassRequest> findstudentstatusbystudentid(@PathVariable("studentId") String studentId) {
		return gatepassservice.findstudentstatusbystudentid(studentId);
	}

	// Update the request by student when the status is pending
	@PutMapping("/updaterequest/{studentId}")
	public GatePassRequest updateRequest(@PathVariable("studentId") String studentId,
			@RequestBody GatePassRequest gatepassrequest) {
		return gatepassservice.updateRequest(studentId, gatepassrequest);
	}

	// Cancel the request by student when the status is pending
	@DeleteMapping("/cancelrequest/{studentId}")
	public String cancelRequest(@PathVariable("studentId") String studentId) {
		return gatepassservice.cancelRequest(studentId);

	}

	// getallpendingdatas
	@GetMapping("/getallpendingstatus")
	public List<GatePassRequest> findallstatus() {
		return gatepassservice.findallstatus();
	}

	// getallstatusdata
	@GetMapping("/getallstatusdata")
	public List<GatePassRequest> findall() {

		return gatepassservice.findall();
	}

	// getallaprovaldata
	@GetMapping("/getallaprovedata")
	public List<GatePassRequest> findallaproved() {

		return gatepassservice.findallaproved();

	}

	// send the approval notification to student by staff
	@PutMapping("/approverequest/{studentId}")
	public String approveRequest(@PathVariable("studentId") String studentId) {

		return gatepassservice.approveRequest(studentId);

	}

	// Reject the request by the staff
	@PutMapping("/rejectrequest/{studentId}")
	public String rejectRequest(@PathVariable("studentId") String studentId, @RequestBody String reason) {
		return gatepassservice.rejectRequest(studentId, reason);

	}

	// Resubmit the gatepass request by student after rejection
	@PutMapping("/resubmitrequest/{studentId}")
	public String resubmitRequest(@PathVariable("studentId") String studentId,
			@RequestBody GatePassRequest updatedRequest) {
		return gatepassservice.resubmitRequest(studentId, updatedRequest);
	}

	// Request an Emergency GatePass by the student
	@PostMapping("/emergencyrequest")
	public GatePassRequest emergencyRequest(@RequestBody GatePassRequest gatepassrequest) {
		return gatepassservice.emergencyRequest(gatepassrequest);
	}

	// Approve the emergency gate pass by the staff
	@PutMapping("/approveEmergency/{studentId}")
	public String approveEmergency(@PathVariable("studentId") String studentId) {
		return gatepassservice.approveEmergencyRequest(studentId);
	}

	// Bulk aprroval to the students by the staff
	@PutMapping("/bulkapproverequest/{eventId}")
	public String bulkApproveRequests(@PathVariable("eventId") String eventId) {

		return gatepassservice.bulkApproveRequests(eventId);

	}

}
