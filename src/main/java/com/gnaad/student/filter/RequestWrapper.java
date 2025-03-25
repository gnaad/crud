package com.gnaad.student.filter;

import com.gnaad.student.util.AESUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RequestWrapper extends HttpServletRequestWrapper {

    private String decryptedBody;

    public RequestWrapper(HttpServletRequest request, String secretKey) throws IOException {
        super(request);
        if (request.getContentLength() > 0) {
            try {
                String encryptedBody = readRequestBody(request);
                System.out.println("Inside Request Wrapper with Encrypted Request : " + encryptedBody);
                decryptedBody = AESUtil.decrypt(encryptedBody, secretKey);
                System.out.println("Decrypted Request : " + decryptedBody);
            } catch (Exception e) {
                throw new IOException("Failed to decrypt request", e);
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBody.getBytes(StandardCharsets.UTF_8));

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }
        };
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
