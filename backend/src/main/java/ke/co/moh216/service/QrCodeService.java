package ke.co.moh216.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class QrCodeService {

    public byte[] generateQrCode(UUID qrToken) {
        if (qrToken == null) {
            throw new IllegalArgumentException("QR token cannot be null");
        }

        try {
            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(
                    qrToken.toString(),
                    BarcodeFormat.QR_CODE,
                    300,
                    300
            );

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(
                    matrix,
                    "PNG",
                    output
            );

            return output.toByteArray();

        } catch (WriterException | IOException e) {
            throw new IllegalStateException(
                    "Failed to generate QR code",
                    e
            );
        }
    }
}
