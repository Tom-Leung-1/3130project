package edu.cuhk.csci3310.trablog_3310;


public class Image {

    private String name;
    private String url;
    private String imageId;

    public Image() { }

    public String getUrl() {
        return url;
    }

    public Image(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}