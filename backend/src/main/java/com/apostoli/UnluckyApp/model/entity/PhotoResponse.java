package com.apostoli.UnluckyApp.model.entity;

public class PhotoResponse {

    private byte[] data;

    public PhotoResponse(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
