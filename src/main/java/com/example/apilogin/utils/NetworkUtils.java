package com.example.apilogin.utils;

import com.example.apilogin.model.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

public class NetworkUtils {

    public static void buildErrorResponse(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        ErrorResponse e = new ErrorResponse("Not Authorized");
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, e);
        responseStream.flush();
    }
}
