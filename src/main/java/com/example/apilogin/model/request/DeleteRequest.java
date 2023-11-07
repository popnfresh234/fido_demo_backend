package com.example.apilogin.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeleteRequest
{
    @JsonProperty("deleteArray")
    private Integer[] deleteArray;
}
