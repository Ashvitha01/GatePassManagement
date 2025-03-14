package in.oasys.gatepass.service;

import java.util.List;

import in.oasys.gatepass.entity.GatePassRequest;

public interface IGatePassService {
	// Send the request for gatepass by the student
	public GatePassRequest insert(GatePassRequest gatepassrequest);

	// Fetch the data by students to check status of the specific student
	public List<GatePassRequest> findstudentstatusbystudentid(String studentId);

	// fetch all the pending status data
	public List<GatePassRequest> findallstatus();

	// fetch all the data with all status
	public List<GatePassRequest> findall();

	// update request by student
	public GatePassRequest updateRequest(String studentId, GatePassRequest gatepassrequest);

	// cancel request by student
	public String cancelRequest(String studentId);

	// Approve request by the staff
	public String approveRequest(String studentId);

	// Reject the request by the staff
	public String rejectRequest(String studentId, String reason);

	// Resubmit the request by the student after rejection
	public String resubmitRequest(String studentId, GatePassRequest updatedRequest);

	// send the emergency request by the student
	public GatePassRequest emergencyRequest(GatePassRequest gatepassrequest);

	// Approve the emergency gatepass by the security
	public String approveEmergencyRequest(String studentId);

	// Approve the bulk approval by the staff
	public String bulkApproveRequests(String eventId);

}
