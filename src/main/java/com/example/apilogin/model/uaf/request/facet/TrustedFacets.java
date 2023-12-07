package com.example.apilogin.model.uaf.request.facet;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class TrustedFacets {
    Version version;
    ArrayList<String> ids;
}
