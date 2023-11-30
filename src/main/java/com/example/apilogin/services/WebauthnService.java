package com.example.apilogin.services;

import com.example.apilogin.model.webauthn.request.auth.req_do_auth.Fido2DoAuthReq;
import com.example.apilogin.model.webauthn.request.reg.do_req.Fido2DoRegReq;
import com.example.apilogin.model.webauthn.request.auth.req_auth.Fido2RequestAuthReq;
import com.example.apilogin.model.webauthn.request.reg.req_reg.Fido2RequestRegReq;
import com.example.apilogin.model.webauthn.response.auth.do_auth.Fido2DoAuthResp;
import com.example.apilogin.model.webauthn.response.reg.do_reg.Fido2DoRegResp;
import com.example.apilogin.model.webauthn.response.auth.req_auth.Fido2RequestAuthResp;
import com.example.apilogin.model.webauthn.response.reg.req_reg.Fido2RequestRegResp;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
public class WebauthnService {
    private static final String REQUEST_REG = "/webauthn/requestReg";
    private static final String DO_REG = "/webauthn/doReg";
    private static final String REQUEST_AUTH = "/webauthn/requestAuth";
    private static final String DO_AUTH = "/webauthn/doAuth";



    @Value("${fido.middleware.url}")
    private String fidoMiddlewareUrl;

    private final RestTemplate restTemplate;

    public WebauthnService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

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

    public Fido2RequestAuthResp requestAuth(Fido2RequestAuthReq req) {
        String url = fidoMiddlewareUrl + REQUEST_AUTH;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Fido2RequestAuthReq> httpEntity = new HttpEntity<>(req, requestHeaders);
        ResponseEntity<Fido2RequestAuthResp> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
                                                                         Fido2RequestAuthResp.class);

        return res.getBody();
    }

    public Fido2DoAuthResp doAuth(Fido2DoAuthReq req) {
        String url = fidoMiddlewareUrl + DO_AUTH;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Fido2DoAuthReq> httpEntity = new HttpEntity<>(req, requestHeaders);
        ResponseEntity<Fido2DoAuthResp> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
                                                                    Fido2DoAuthResp.class);
        return res.getBody();
    }
}
