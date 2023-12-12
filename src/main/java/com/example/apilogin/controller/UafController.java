package com.example.apilogin.controller;

import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.model.response.Response;
import com.example.apilogin.model.uaf.request.auth.do_auth_req.UafDoAuthReq;
import com.example.apilogin.model.uaf.request.auth.req_auth_req.UafRequestAuthReq;
import com.example.apilogin.model.uaf.request.facet.FacetsBody;
import com.example.apilogin.model.uaf.request.facet.ResFacets;
import com.example.apilogin.model.uaf.request.reg.do_dereg_req.UafDoDeregReq;
import com.example.apilogin.model.uaf.request.reg.do_reg_req.UafDoRegReq;
import com.example.apilogin.model.uaf.request.reg.reg_req.UafRequestRegReq;
import com.example.apilogin.model.uaf.response.auth.do_auth_res.UafDoAuthRes;
import com.example.apilogin.model.uaf.response.auth.req_auth_res.UafRequestAuthRes;
import com.example.apilogin.model.uaf.response.reg.do_dergeg_res.UafDoDeregRes;
import com.example.apilogin.model.uaf.response.reg.do_reg_res.UafDoRegRes;
import com.example.apilogin.model.uaf.response.reg.req_reg_res.UafReqRegRes;
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


    @GetMapping(path="/test")
    public Response test(){
        return new Response("This is a test");
    }

    @PostMapping(path = "/requestReg")
    public UafReqRegRes requestReg(
            @RequestBody
            UafRequestRegReq uafRequestRegReq,
            HttpServletRequest httpServletRequest) {


        log.info("POST /uaf/requestReg" );

        uafRequestRegReq.getBody().setAppID(appId);
        uafRequestRegReq.getBody().setRpServerData("testdata");
        try {

            return uafService.requestReg(uafRequestRegReq);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("uafReqReg Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping("/doReg")
    public UafDoRegRes doReg(@RequestBody
            UafDoRegReq doRegReq, HttpServletRequest httpServletRequest){
        log.info("POST /uaf/doReg" );
        try {
            return uafService.doReg(doRegReq);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("uafReqReg Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping("/doDereg")
    public UafDoDeregRes doDereg(@RequestBody UafDoDeregReq req, HttpServletRequest httpServletRequest){
        log.info("POST /uaf/doDereg");
        req.getBody().setAppID(appId);
        try {
            return uafService.doDeReg(req);
        }  catch (Exception e) {
            log.error(e.getMessage());
            log.error("uafReqReg Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_DEREG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }

    }

    @PostMapping("/requestAuth")
    public UafRequestAuthRes requestAuth(@RequestBody
            UafRequestAuthReq req, HttpServletRequest httpServletRequest){
        log.info("POST /uaf/requestAuth");
        req.getBody().setAppID(appId);
        try {
            UafRequestAuthRes res = uafService.requestAuth(req);
            log.error(new Gson().toJson(res));
            return uafService.requestAuth(req);
        }  catch (Exception e) {
            log.error(e.getMessage());
            log.error("uafReqReg Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping("/doAuth")
    public UafDoAuthRes doAuth(@RequestBody UafDoAuthReq req, HttpServletRequest httpServletRequest){
        log.info("POST /uaf/doAuth");
        try {
            return uafService.doAuth(req);
        }  catch (Exception e) {
            log.error(e.getMessage());
            log.error("uafReqReg Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @GetMapping("/facets")
    public FacetsBody getFacets(HttpServletRequest httpServletRequest){
        return getFacetsBody(
                "GET /uaf/facets",
                httpServletRequest);
    }

    @GetMapping("/facets/")
    public FacetsBody noGood (HttpServletRequest httpServletRequest){
        return getFacetsBody(
                "GET /uaf/facets/",
                httpServletRequest);
    }

    private FacetsBody getFacetsBody(
            String message,
            HttpServletRequest httpServletRequest) {
        log.info(message);
        try {
            HttpResponse<String> res = uafService.getFacets();
            Gson gson = new GsonBuilder().create();
            ResFacets resFacets = gson.fromJson(
                    res.body(),
                    ResFacets.class);
            return resFacets.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("reqReg Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.REQ_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }
}
