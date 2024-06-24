package com.nc13.springBoard.model;

import lombok.Data;

import java.util.Date;

@Data
public class BoardDTO {
    private int id;
    private String title;
    private String content;
    private Date entryDate;
    private Date modifyDate;
    private String nickname;
    private int writerId;
}
