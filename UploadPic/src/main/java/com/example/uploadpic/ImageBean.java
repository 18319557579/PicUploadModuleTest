package com.example.uploadpic;

/**
 * Created by cyd on 19-4-11.
 */

public class ImageBean {

    private String abFilePath;
    private String date;
    private String uilFilePath;

    private boolean checked;
    private int index = -1;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ImageBean(String abFilePath, String date, String uilFilePath) {
        this.abFilePath = abFilePath;
        this.date = date;
        this.uilFilePath = uilFilePath;
    }

    public String getAbFilePath() {
        return abFilePath;
    }

    public void setAbFilePath(String abFilePath) {
        this.abFilePath = abFilePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUilFilePath() {
        return uilFilePath;
    }

    public void setUilFilePath(String uilFilePath) {
        this.uilFilePath = uilFilePath;
    }
}
