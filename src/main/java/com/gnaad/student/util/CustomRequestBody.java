package com.gnaad.student.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomRequestBody {

    @Autowired
    private HttpSession session;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T extractPayload(String encryptedBody, Class<T> clazz) throws Exception {
        return objectMapper.readValue(encryptedBody, clazz);
    }
}
