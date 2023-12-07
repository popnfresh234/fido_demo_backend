package com.example.apilogin.services;

import com.example.apilogin.model.response.Response;
import com.example.apilogin.model.uaf.request.reg.req_reg.UafRequestRegReq;
import com.example.apilogin.model.uaf.request.reg.req_reg.UafRequestRegRestRequestBody;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Log4j2
@Service
public class UafService {
    private static final String facet_url = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/facets?appID=https://demo-frontend-alex-demo.apps.oc.webcomm.com.tw/api/uaf/facets";
    private static final String REQ_REG_URL = "https://fidolab.apps.oc.webcomm.com.tw/identity-svr/rest/requestReg";

    public HttpResponse<String> getFacets() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(facet_url))
                .build();
        return client.send(
                req,
                HttpResponse.BodyHandlers.ofString());

    }

    public HttpResponse<String> requestReg(UafRequestRegReq req) throws Exception {
        Gson gson = new Gson();
        log.error(gson.toJson(req));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest regReq = HttpRequest.newBuilder()
                .uri(URI.create(REQ_REG_URL))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(req)))
                .build();
        return client.send(regReq, HttpResponse.BodyHandlers.ofString());
    }
}
