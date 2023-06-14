package com.wy.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Book implements Serializable {

    private static final long serialVersionUID = 4346171857596918006L;

    private String name;

}
