package com.gnaad.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnaad.student.util.AESUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UtilityController {

    @PostMapping("/set")
    public String setSession(HttpSession session) throws Exception {
        System.out.println("Executing set");
        String secretKey = AESUtil.generateAESKey();
        session.setAttribute("sec", secretKey);
        System.out.println("Secret Key : " + secretKey);
        return "AES key stored in session";
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody Object payload, HttpSession session) throws Exception {
        String json = new ObjectMapper().writeValueAsString(payload);
        Object secretKey = session.getAttribute("sec");
        return AESUtil.encrypt(json, (String) secretKey);
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestBody String payload, HttpSession session) throws Exception {
        Object secretKey = session.getAttribute("sec");
        return AESUtil.decrypt(payload, (String) secretKey);
    }
}
