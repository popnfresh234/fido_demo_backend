package com.example.apilogin.controller;

import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.exceptions.UafException;
import com.example.apilogin.model.request.LoginRequest;
import com.example.apilogin.model.response.LoginResponse;
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
    private final Gson gson;
    private final static String appId = "https://demo-frontend-alex-demo.apps.oc.webcomm.com.tw/api/uaf/facets";

    public UafController(UafService uafService, UserService userService, JwtIssuer jwtIssuer) {
        this.uafService = uafService;
        this.userService = userService;
        this.jwtIssuer = jwtIssuer;
        this.gson = new Gson();
    }

    // ***************************************
    // Request Registration
    // ***************************************

    @PostMapping(path = "/requestReg")
    public UafReqRegRes requestReg(
            @RequestBody UafRequestRegReq uafRequestRegReq, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /uaf/requestReg"));
        uafRequestRegReq.getBody().setAppID(appId);
        uafRequestRegReq.getBody().setRpServerData("testdata");
        UafReqRegRes res = null;
        try {
            res = uafService.requestReg(uafRequestRegReq);
            return res;
        } catch (Exception e) {
            log.error("UAF Request Reg Request: ");
            log.error(gson.toJson(uafRequestRegReq));
            log.error("UAF Request Reg Response: ");
            log.error(gson.toJson(res));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    // ***************************************
    // Do Registration
    // ***************************************

    @PostMapping("/doReg")
    public UafDoRegRes doReg(
            @RequestBody UafDoRegReq req, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /uaf/doReg"));
        UafDoRegRes res = null;
        try {
            res = uafService.doReg(req);
            return res;
        } catch (Exception e) {
            log.error("UAF doReg Request: ");
            log.error(gson.toJson(req));
            log.error("UAF doReg Response: ");
            log.error(gson.toJson(res));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_REG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    // ***************************************
    // Do Deregistration
    // ***************************************

    @PostMapping("/doDereg")
    public UafDoDeregRes doDereg(
            @RequestBody UafDoDeregReq req, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /uaf/doDereg"));
        req.getBody().setAppID(appId);
        try {
            UafDoDeregRes res = null;
            res = uafService.doDeReg(req);
            return res;
        } catch (Exception e) {
            log.error("UAF doDereg Request: ");
            log.error(gson.toJson(req));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_DEREG_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }

    }

    // ***************************************
    // Request Authorization
    // ***************************************

    @PostMapping("/requestAuth")
    public UafRequestAuthRes requestAuth(
            @RequestBody UafRequestAuthReq req, HttpServletRequest httpServletRequest) {
        req.getBody().setAppID(appId);
        log.info(LogUtils.buildRouteLog("POST /uaf/requestAuth"));
        UafRequestAuthRes res = null;
        try {
            res = uafService.requestAuth(req);
            return res;
        } catch (Exception e) {
            log.error("UAF reqAuth Request: ");
            log.error(gson.toJson(req));
            log.error("UAF reqAUth Response");
            log.error(gson.toJson(res));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    // ***************************************
    // Do Authorization
    // ***************************************

    @PostMapping("/doAuth")
    public UafDoAuthRes doAuth(
            @RequestBody UafDoAuthReq req, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /uaf/doAuth"));
        log.error(new Gson().toJson(req));

        UafDoAuthRes res = null;
        try {
            res = uafService.doAuth(req);
            return res;
        } catch (Exception e) {
            log.error("UAF doAuth request: ");
            log.error(gson.toJson(req));
            log.error("UAF doAuth Response: ");
            log.error(gson.toJson(res));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_DO_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    // ***************************************
    // Get Facets
    // ***************************************

    @GetMapping("/facets")
    public FacetsBody getFacets(HttpServletRequest httpServletRequest) {
        return getFacetsBody(
                "GET /uaf/facets",
                httpServletRequest);
    }

    // ***************************************
    // Get Facets with Trailing Slash Added
    // ***************************************

    @GetMapping("/facets/")
    public FacetsBody getFacetsWithSlashAddedForSomeReason(HttpServletRequest httpServletRequest) {
        return getFacetsBody(
                "GET /uaf/facets/",
                httpServletRequest);
    }

    private FacetsBody getFacetsBody(
            String message, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog(message));
        try {
            HttpResponse<String> res = uafService.getFacets();
            Gson gson = new GsonBuilder().create();
            ResFacets resFacets = gson.fromJson(
                    res.body(),
                    ResFacets.class);
            return resFacets.getBody();
        } catch (Exception e) {
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_FACETS_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    // ***************************************
    // Request QR Code
    // ***************************************

    @PostMapping(path = "/qrcode/requestQrCode")
    public RequestQRCodeRes requestQrCode(
            @RequestBody RequestQRCodeReq req, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /qrcode/requestQrCode"));
        RequestQRCodeRes res = null;
        try {
            res = uafService.requestQRCode(req);
            return res;
        } catch (Exception e) {
            log.error("UAF getQRCode request: ");
            log.error(gson.toJson(req));
            log.error("UAF requestQRCode Response: ");
            log.error(gson.toJson(res));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_QR_CODE_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    // ***************************************
    // Request Pair Authorization
    // ***************************************

    @PostMapping(path = "/requestPairAuth")
    public UafRequestAuthRes requestPairAuth(
            @RequestBody UafRequestPairAuthReq req, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /ufa/requestPairAuth"));
        req.getBody().setAppID(appId);
        UafRequestAuthRes res = null;
        try {
            res = uafService.requestPairAuth(req);
            return res;
        } catch (Exception e) {
            log.error("UAF requestPairAuth request: ");
            log.error(gson.toJson(req));
            log.error("UAF requestPairAuth response: ");
            log.error(gson.toJson(res));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_REQ_PAIR_AUTH_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();

        }
    }

    // ***************************************
    // Validate QR Code
    // ***************************************

    @PostMapping(path = "qrcode/validateQrCode")
    public ValidateQRCodeRes validateQRCode(
            @RequestBody ValidateQRCodeReq req, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /uaf/qrcode/validateQrCode"));
        ValidateQRCodeRes res = null;
        try {
            req.getBody().setAppId(appId);
            res =  uafService.validateQrCode(req);
            return res;
        } catch (Exception e) {
            log.error("UAF validateQRCode request: ");
            log.error(gson.toJson(req));
            log.error("UAF validateQRCode response: ");
            log.error(gson.toJson(res));
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_VALIDATE_QR_CODE_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }

    // ***************************************
    // Do QR Code Login
    // ***************************************

    @PostMapping(path = "/qrcode/login")
    public LoginResponse qrCodeLogin(
            @RequestBody LoginRequest req, HttpServletRequest httpServletRequest) {
        log.info(LogUtils.buildRouteLog("POST /qrcode/login"));

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
            return new LoginResponse(
                    "QR Code Login Success",
                    token,
                    stringAuths);

        } catch (Exception e) {
            throw UafException.builder().msg(e.getMessage()).operation(LogUtils.UAF_QR_CODE_LOGIN_REQ)
                    .ip(httpServletRequest.getRemoteAddr()).target(AuthUtils.getPrincipal().getAccount()).build();
        }
    }
}
