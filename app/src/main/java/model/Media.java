package model;

import java.io.Serializable;
import java.util.Dictionary;

import enums.MediaType;

public class Media implements Serializable {
    private Image i = new Image();//image
    private String id = "";
    private String l = "";//title
    private MediaType qid = MediaType.movie;//default is movie
    private int y = 0;//year

    public Media() {
    }


    public Image getImage() {
        return i;
    }

    public Media setImage(Image i) {
        this.i = i;
        return this;
    }

    public String getId() {
        return id;
    }

    public Media setId(String id) {
        this.id = id;
        return this;
    }

    public String getL() {
        return l;
    }

    public Media setL(String l) {
        this.l = l;
        return this;
    }

    public MediaType getQid() {
        return qid;
    }

    public Media setQid(MediaType qid) {
        this.qid = qid;
        return this;
    }

    public int getYear() {
        return y;
    }

    public Media setYear(int y) {
        this.y = y;
        return this;
    }
}
