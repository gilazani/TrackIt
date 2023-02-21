package model;

import java.io.Serializable;

public class Image implements Serializable {
    private String imageUrl = "";
    private int height = 0;
    private int width = 0;

    public Image() {
    }

    public Image(Image image) {
        this.imageUrl = image.getImageUrl();
        this.height = image.getHeight();
        this.width = image.getWidth();
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public Image setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Image setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Image setWidth(int width) {
        this.width = width;
        return this;
    }
}
