package com.ssacksri.memo;

public class MemoModel {
    private String num;
    private String date;
    private String comment;
    private byte[] image;

    public MemoModel(String num, String date, String comment, byte[] image) {
        this.num = num;
        this.date = date;
        this.comment = comment;
        this.image = image;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
