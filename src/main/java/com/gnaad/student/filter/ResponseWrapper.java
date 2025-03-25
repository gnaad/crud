package com.gnaad.student.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnaad.student.util.AESUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(outputStream);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
            }
        };
    }

    public void encryptAndSend(String secretKey) throws Exception {
        writer.flush();
        String originalContent = outputStream.toString();
        System.out.println("Inside Response Wrapper with Decrypted Response : " + originalContent);
        String encryptedContent = AESUtil.encrypt(originalContent, secretKey);
        System.out.println("Encrypted Response : " + encryptedContent);
        getResponse().setContentLength(encryptedContent.length());
        getResponse().getWriter().write(encryptedContent);
    }
}
