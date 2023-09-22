package com.zeal.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Book implements Serializable {

    private static final long serialVersionUID = 4346171857596918006L;

    private String name;

}
