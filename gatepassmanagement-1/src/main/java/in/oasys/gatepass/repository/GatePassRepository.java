package in.oasys.gatepass.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.oasys.gatepass.entity.GatePassRequest;
import in.oasys.gatepass.entity.GatePassRequest.Status;

@Repository
public interface GatePassRepository extends JpaRepository<GatePassRequest, Long> {

	List<GatePassRequest> findByStudentId(String studentId);

	List<GatePassRequest> findByStatus(GatePassRequest.Status status);

	List<GatePassRequest> findByIsEmergencyTrue();

	List<GatePassRequest> findByEventIdAndStatus(String eventId, Status pending);

}
