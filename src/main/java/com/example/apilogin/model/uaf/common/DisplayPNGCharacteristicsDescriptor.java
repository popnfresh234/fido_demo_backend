package com.example.apilogin.model.uaf.common;

import com.example.apilogin.model.uaf.request.reg.do_reg_req.RGBPaletteEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DisplayPNGCharacteristicsDescriptor {
    private Integer width;
    private Integer height;
    private Integer bitDepth;
    private Integer colorType;
    private Integer compression;
    private Integer filter;
    private Integer interlace;
    private ArrayList<RGBPaletteEntry> plte;
}
