package com.example.apilogin.services;

import com.example.apilogin.model.uaf.request.auth.do_auth_req.UafDoAuthReq;
import com.example.apilogin.model.uaf.request.auth.req_auth_req.UafRequestAuthReq;
import com.example.apilogin.model.uaf.request.pair.UafRequestPairAuthReq;
import com.example.apilogin.model.uaf.request.qr_code.RequestQRCodeReq;
import com.example.apilogin.model.uaf.request.qr_code.ValidateQRCodeReq;
import com.example.apilogin.model.uaf.request.reg.do_dereg_req.UafDoDeregReq;
import com.example.apilogin.model.uaf.request.reg.do_reg_req.UafDoRegReq;
import com.example.apilogin.model.uaf.request.reg.reg_req.UafRequestRegReq;
import com.example.apilogin.model.uaf.response.auth.do_auth_res.UafDoAuthRes;
import com.example.apilogin.model.uaf.response.auth.qr_code.RequestQRCodeRes;
import com.example.apilogin.model.uaf.response.auth.req_auth_res.UafRequestAuthRes;
import com.example.apilogin.model.uaf.response.qrcode.ValidateQRCodeRes;
import com.example.apilogin.model.uaf.response.reg.do_dergeg_res.UafDoDeregRes;
import com.example.apilogin.model.uaf.response.reg.do_reg_res.UafDoRegRes;
import com.example.apilogin.model.uaf.response.reg.req_reg_res.UafReqRegRes;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j2
@Service
public class UafService {
    private static final String facet_url = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/facets?appID=https://demo-frontend-alex-demo.apps.oc.webcomm.com.tw/api/uaf/facets";
    private static final String REQ_REG_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/requestReg";
    private static final String DO_REG_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/doReg";
    private static final String DO_DEREG_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/doDereg";
    private static final String REQ_AUTH_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/requestAuth";
    private static final String DO_AUTH_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/doAuth";
    private static final String REQ_QRCODE_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/oob/requestQRCode";
    private static final String REQ_VALIDATE_QRCODE_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/oob/validateQRCode";

    private static final String REQ_PAIR_AUTH_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/requestPairAuth";


    private static final String BIN_URL = "https://httpbin.org/post";


    public HttpResponse<String> getFacets() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(facet_url))
                .build();
        return client.send(
                req,
                HttpResponse.BodyHandlers.ofString());
    }

    public UafReqRegRes requestReg(UafRequestRegReq req) throws Exception {
        WebClient webClient = WebClient.create();

        return webClient.post()
                .uri(REQ_REG_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(UafReqRegRes.class)
                .block();
    }

    public UafDoRegRes doReg(UafDoRegReq req) throws Exception {
        WebClient webClient = WebClient.create();
        return webClient.post()
                .uri(DO_REG_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(UafDoRegRes.class)
                .block();
    }

    public UafDoDeregRes doDeReg(UafDoDeregReq req) throws Exception {
        WebClient webClient = WebClient.create();
        return webClient.post()
                .uri(DO_DEREG_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(UafDoDeregRes.class)
                .block();
    }

    public UafRequestAuthRes requestAuth(UafRequestAuthReq req) throws Exception {
        WebClient webClient = WebClient.create();
        return webClient.post()
                .uri(REQ_AUTH_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(UafRequestAuthRes.class)
                .block();
    }


    public UafDoAuthRes doAuth(UafDoAuthReq req) throws Exception {
        WebClient webClient = WebClient.create();
        return webClient.post()
                .uri(DO_AUTH_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(UafDoAuthRes.class)
                .block();
    }

    public RequestQRCodeRes requestQRCode(RequestQRCodeReq req) throws Exception {
        WebClient webClient = WebClient.create();
        log.error("CREATE QR CODE");
        log.error(new Gson().toJson(req));
        return webClient.post()
                .uri(REQ_QRCODE_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(RequestQRCodeRes.class)
                .block();
    }

    public UafRequestAuthRes requestPairAuth(UafRequestPairAuthReq req) throws Exception {
        WebClient webClient = WebClient.create();
        return webClient.post()
                .uri(REQ_PAIR_AUTH_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(UafRequestAuthRes.class)
                .block();
    }


    public ValidateQRCodeRes validateQrCode(ValidateQRCodeReq req) throws Exception {
        WebClient webClient = WebClient.create();
        return webClient.post()
                .uri(REQ_VALIDATE_QRCODE_URL)
                .header("applicationContent-type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(req))
                .retrieve()
                .bodyToMono(ValidateQRCodeRes.class)
                .block();
    }
}

