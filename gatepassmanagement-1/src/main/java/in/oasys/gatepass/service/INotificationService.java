package in.oasys.gatepass.service;

import jakarta.mail.MessagingException;

public interface INotificationService {

	
	public void sendApprovalNotification(String staffEmail, String studentName, String reason) ;
	
	public void sendApprovalNotification(String studentEmail, String qrCodePath) throws MessagingException;
	  public void sendRejectionNotification(String studentEmail, String reason);
	  public void sendSecurityNotification(String securityEmail, String studentName, String reason);
}
