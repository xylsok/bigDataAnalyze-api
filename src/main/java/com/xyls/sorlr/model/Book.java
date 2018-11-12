package com.xyls.sorlr.model;

import lombok.Data;

@Data
public class Book {
    private String id;
    private long fileSize;
    private String fileLastModified;
    private String fileAbsolutePath;
    private String title;
    private String text;
}
