package com.example.apilogin.controller;

import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.model.response.Response;
import com.example.apilogin.model.uaf.request.facet.ResFacets;
import com.example.apilogin.model.uaf.request.reg.req_reg.UafRequestRegReq;
import com.example.apilogin.model.uaf.request.reg.req_reg.UafRequestRegRestRequestBody;
import com.example.apilogin.model.webauthn.request.reg.req_reg.Fido2RequestRegReq;
import com.example.apilogin.services.UafService;
import com.example.apilogin.utils.AuthUtils;
import com.example.apilogin.utils.LogUtils;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(path = "/uaf")
public class UafController {

    private final UafService uafService;
    private final static String appId = "https://demo-frontend-alex-demo.apps.oc.webcomm.com.tw/api/uaf/facets";

    public UafController(UafService uafService) {
        this.uafService = uafService;
    }


    @GetMapping(path = "/requestReg")
    public Response test(
            @RequestBody
            UafRequestRegReq uafRequestRegReq,
            HttpServletRequest httpServletRequest) {
        uafRequestRegReq.getBody().setAppID(appId);
        uafRequestRegReq.getBody().setRpServerData("");

        try {
            HttpResponse<String> res = uafService.requestReg(uafRequestRegReq);
            log.error(res);

            return new Response(res.body());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("uafReqReg Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.REQ_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }

//        try{
//            HttpResponse<String> res = uafService.getFacets();
//            Gson gson = new GsonBuilder().create();
//            ResFacets resFacets = gson.fromJson(res.body(), ResFacets.class);
//            return new Response(resFacets.getBody().getTrustedFacets().get(0).getIds().get(0));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            log.error("reqReg Exception");
//            log.error(e.getMessage());
//            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.REQ_REG_REQ)
//                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
//        }
    }
}
