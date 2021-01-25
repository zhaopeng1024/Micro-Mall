package com.pingan.life.micromall.product.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttrVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long attrId;

    private String attrName;

    private String attrValue;

}
