package com.example.apilogin.controller;

import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.model.request.LoginRequest;
import com.example.apilogin.model.response.LoginResponse;
import com.example.apilogin.model.response.Response;
import com.example.apilogin.model.uaf.request.auth.do_auth_req.UafDoAuthReq;
import com.example.apilogin.model.uaf.request.auth.req_auth_req.UafRequestAuthReq;
import com.example.apilogin.model.uaf.request.facet.FacetsBody;
import com.example.apilogin.model.uaf.request.facet.ResFacets;
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
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.services.UafService;
import com.example.apilogin.services.UserService;
import com.example.apilogin.utils.AuthUtils;
import com.example.apilogin.utils.LogUtils;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@CrossOrigin
@RestController
@RequestMapping(path = "/uaf")
public class UafController {

    private final UafService uafService;
    private final UserService userService;
    private final JwtIssuer jwtIssuer;
    private final static String appId = "https://demo-frontend-alex-demo.apps.oc.webcomm.com.tw/api/uaf/facets";

    public UafController(UafService uafService, UserService userService, JwtIssuer jwtIssuer) {
        this.uafService = uafService;
        this.userService = userService;
        this.jwtIssuer = jwtIssuer;
    }


    @GetMapping(path = "/test")
    public Response test() {
        return new Response("This is a test");
    }

    @PostMapping(path = "/requestReg")
    public UafReqRegRes requestReg(
            @RequestBody
            UafRequestRegReq uafRequestRegReq,
            HttpServletRequest httpServletRequest) {


        log.info("POST /uaf/requestReg");

        uafRequestRegReq.getBody().setAppID(appId);
        uafRequestRegReq.getBody().setRpServerData("testdata");
        try {

            return uafService.requestReg(uafRequestRegReq);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UAF Request Reg Error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping("/doReg")
    public UafDoRegRes doReg(
            @RequestBody
            UafDoRegReq doRegReq,
            HttpServletRequest httpServletRequest) {
        log.info("POST /uaf/doReg");
        try {
            return uafService.doReg(doRegReq);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UAF Do Reg Error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping("/doDereg")
    public UafDoDeregRes doDereg(
            @RequestBody UafDoDeregReq req,
            HttpServletRequest httpServletRequest) {
        log.info("POST /uaf/doDereg");
        req.getBody().setAppID(appId);
        try {
            return uafService.doDeReg(req);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UAF Do Dereg Error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_DEREG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }

    }

    @PostMapping("/requestAuth")
    public UafRequestAuthRes requestAuth(
            @RequestBody
            UafRequestAuthReq req,
            HttpServletRequest httpServletRequest) {
        log.info("POST /uaf/requestAuth");
        req.getBody().setAppID(appId);
        try {
            UafRequestAuthRes res = uafService.requestAuth(req);
            return uafService.requestAuth(req);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UAF Req Auth Error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping("/doAuth")
    public UafDoAuthRes doAuth(
            @RequestBody UafDoAuthReq req,
            HttpServletRequest httpServletRequest) {
        log.info("POST /uaf/doAuth");
        log.info("do auth request");
        log.info(new Gson().toJson(req));
        try {

            log.info("do auth response");
            UafDoAuthRes res = uafService.doAuth(req);
            log.info(new Gson().toJson(res));
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("UAF Do Auth Exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @GetMapping("/facets")
    public FacetsBody getFacets(HttpServletRequest httpServletRequest) {
        return getFacetsBody(
                "GET /uaf/facets",
                httpServletRequest);
    }

    @GetMapping("/facets/")
    public FacetsBody noGood(HttpServletRequest httpServletRequest) {
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
            log.error("Get Facets Error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_FACETS_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping(path = "/qrcode/requestQrCode")
    public RequestQRCodeRes requestQrCode(
            @RequestBody
            RequestQRCodeReq req,
            HttpServletRequest httpServletRequest) {
        log.info("POST /qrcode/requestQrCode");
        try {
            return uafService.requestQRCode(req);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Request QR Code Error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_QR_CODE_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping(path = "/requestPairAuth")
    public UafRequestAuthRes requestPairAuth(
            @RequestBody
            UafRequestPairAuthReq req, HttpServletRequest httpServletRequest) {
        log.info("POST /ufa/requestPairAuth");
        req.getBody().setAppID(appId);
        log.info("Request Pair Auth Req");
        log.info(new Gson().toJson(req));
        try {
            UafRequestAuthRes res =uafService.requestPairAuth(req);

            log.info("requestPairAuthResponse");
            log.info(new Gson().toJson(res));
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("Request Pair Auth Error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_PAIR_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();

        }
    }

    @PostMapping(path="qrcode/validateQrCode")
    public ValidateQRCodeRes validateQRCode(@RequestBody
            ValidateQRCodeReq req, HttpServletRequest httpServletRequest){
        log.info("POST /uaf/qrcode/validateQrCode");
        try{
            req.getBody().setAppId(appId);
            log.error(new Gson().toJson(req));
            ValidateQRCodeRes res =  uafService.validateQrCode(req);
            log.error(new Gson().toJson(res));
            return res;
        } catch(Exception e){
            log.error(e.getMessage());
            log.error("validate QR Code error");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.UAF_VALIDATE_QR_CODE_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    @PostMapping(path="/qrcode/login")
    public LoginResponse qrCodeLogin(@RequestBody
            LoginRequest req, HttpServletRequest httpServletRequest){
        log.info("POST /qrcode/login");

        try {
            // Look up the user by account
            Optional<UserEntity> opt = userService.findByAccount(req.getAccount());
            UserEntity user = opt.orElseThrow();

            // Login user with correct roles
            Set<RoleEntity> roles = user.getRole();
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            for (RoleEntity role : roles) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
                authorities.add(authority);
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.getAccount(),
                    "",
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Issue JWT for login flow
            List<String> stringAuths = new ArrayList<>();
            for (SimpleGrantedAuthority auth : authorities) {
                String authority = auth.getAuthority();
                stringAuths.add(authority);
            }
            var token = jwtIssuer.issue(
                    user.getId(),
                    user.getAccount(),
                    user.getName(),
                    user.getEmail(),
                    stringAuths);

            // Add the login response to the fido response for frontend
            LoginResponse res = new LoginResponse(
                    "Login Success",
                    token,
                    stringAuths);
            return res;

        } catch (Exception e) {
            log.error("doAuth exception");
            log.error(e.getMessage());
            throw AuthException.builder().msg(e.getMessage()).operation(LogUtils.DO_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }
}
