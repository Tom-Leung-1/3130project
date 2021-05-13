package edu.cuhk.csci3310.trablog_3310;

import java.util.ArrayList;
import java.util.LinkedList;

public class Blog {
    private String title;
    private String description;
    private String username;
    private Integer id;
    private Double lat;
    private Double lng;
    private Double img;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() { return lng; }

    public Object getImgFileName() { return img;}
}
