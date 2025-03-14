package in.oasys.gatepass.service;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService implements INotificationService {

	@Autowired
	JavaMailSender emailSender;

	// send notification to the staff when the student request a gate pass
	@Override
	public void sendApprovalNotification(String staffEmail, String studentName, String reason) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(staffEmail);
		message.setSubject("New Gate Pass Request for Approval");
		message.setText("Student " + studentName + " has requested a gate pass. Reason: " + reason
				+ ". Please review and approve/reject.");
		emailSender.send(message);

	}

	// Notify student for approval when the staff aproved the gate pass
	@Override

	public void sendApprovalNotification(String studentEmail, String qrCodePath) throws MessagingException {

		if (studentEmail == null || studentEmail.isEmpty()) {
			throw new MessagingException("Student email is missing.");
		}

		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(studentEmail);
		helper.setSubject("Gate Pass Approved");
		helper.setText("Your Gate Pass has been approved. Please find your QR Code attached.");

		// Attach the QR Code Image
		FileSystemResource file = new FileSystemResource(new File(qrCodePath));
		helper.addAttachment("GatePassQRCode.png", file);

		emailSender.send(message);
		System.out.println("✅ Email with QR Code sent successfully to " + studentEmail);
	}

	// Notify student of rejection by the staff to student
	@Override
	public void sendRejectionNotification(String studentEmail, String reason) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(studentEmail);
		message.setSubject("Gate Pass Rejected");
		message.setText("Your Gate Pass request has been rejected. Reason: " + reason);
		emailSender.send(message);

	}

	// Notify Security about Emergency Pass
	public void sendSecurityNotification(String securityEmail, String studentName, String reason) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(securityEmail);
		message.setSubject("Emergency Gate Pass Request");
		message.setText("Emergency Pass Requested by Student: " + studentName + ".\nReason: " + reason);
		emailSender.send(message);
	}

}
