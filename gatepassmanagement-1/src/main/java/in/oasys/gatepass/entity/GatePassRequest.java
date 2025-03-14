package in.oasys.gatepass.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gate_pass_requests")
public class GatePassRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String studentId;
	private String studentname;
	private String whometovisit;
	private String reason;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime enterdate;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime entertime;

	private String studentEmail;
	private String eventId;

	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDING;
	private boolean isEmergency = false;
	private LocalDateTime createdAt = LocalDateTime.now();

	public enum Status {
		PENDING, APPROVED, REJECTED, CANCELLED, EMERGENCYAPPROVED, VALIDATED, EXPIRED, INVALID, COMPLETED
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public boolean isEmergency() {
		return isEmergency;
	}

	public void setEmergency(boolean isEmergency) {
		this.isEmergency = isEmergency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentname() {
		return studentname;
	}

	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}

	public String getWhometovisit() {
		return whometovisit;
	}

	public void setWhometovisit(String whometovisit) {
		this.whometovisit = whometovisit;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getEnterdate() {
		return enterdate;
	}

	public void setEnterdate(LocalDateTime enterdate) {
		this.enterdate = enterdate;
	}

	public LocalDateTime getEntertime() {
		return entertime;
	}

	public void setEntertime(LocalDateTime entertime) {
		this.entertime = entertime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

}
