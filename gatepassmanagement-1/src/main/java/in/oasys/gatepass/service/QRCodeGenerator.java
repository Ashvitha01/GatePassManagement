package in.oasys.gatepass.service;

import java.io.File;
import java.nio.file.Path;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class QRCodeGenerator {

	private static final String QR_CODE_DIRECTORY = "C:/Users/Ashvitha/GatePassManagement/qrcode.png"; // Folder to save
																										// QR codes

	public String generateQRCode(String studentId, String studentName) throws WriterException, IOException {
		String qrContent = "Student ID: " + studentId + "\nStudent Name: " + studentName;
		int width = 300;
		int height = 300;

		// Define encoding settings
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height, hints);

		// Ensure directory exists
		File directory = new File(QR_CODE_DIRECTORY);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Save the QR Code
		String filePath = QR_CODE_DIRECTORY + studentId + ".png";
		Path path = Paths.get(filePath);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

		return filePath; // Return the path of the generated QR Code
	}
}
