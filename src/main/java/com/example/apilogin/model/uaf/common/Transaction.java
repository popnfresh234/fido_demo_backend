package com.example.apilogin.model.uaf.common;

import com.example.apilogin.model.uaf.common.DisplayPNGCharacteristicsDescriptor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
    private String contentType;
    private String content;
    private DisplayPNGCharacteristicsDescriptor tcDisplayPNGCharacteristics;
}
