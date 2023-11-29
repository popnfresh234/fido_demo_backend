package com.example.apilogin.services;

import com.example.apilogin.model.webauthn.request.Fido2DoRegReq;
import com.example.apilogin.model.webauthn.request.Fido2RequestRegReq;
import com.example.apilogin.model.webauthn.response.Fido2DoRegResp;
import com.example.apilogin.model.webauthn.response.Fido2RequestRegResp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class WebauthnService {
    private static final String REQUEST_REG = "/webauthn/requestReg";
    private static final String DO_REG = "/webauthn/doReg";

    @Value("${fido.middleware.url}")
    private String fidoMiddlewareUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Fido2RequestRegResp requestReg(Fido2RequestRegReq req) {
        String url = fidoMiddlewareUrl + REQUEST_REG;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Fido2RequestRegReq> httpEntity = new HttpEntity<>(req, requestHeaders);
        ResponseEntity<Fido2RequestRegResp> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
                                                                        Fido2RequestRegResp.class);
        return res.getBody();
    }

    public Fido2DoRegResp doReg(Fido2DoRegReq req) {

        String url = fidoMiddlewareUrl + DO_REG;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Fido2DoRegReq> httpEntity = new HttpEntity<>(req, requestHeaders);

        ResponseEntity<Fido2DoRegResp> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
                                                                   Fido2DoRegResp.class);
        return res.getBody();
    }
}
