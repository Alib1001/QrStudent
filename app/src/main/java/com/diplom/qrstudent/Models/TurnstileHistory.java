package com.diplom.qrstudent.Models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class TurnstileHistory implements Serializable {
    @SerializedName("id")
    private Long id;

    @SerializedName("scanDateTime")
    private Date scanDateTime;

    @SerializedName("userId")
    private long userId;

    @SerializedName("scanCount")
    private int scanCount;

    public TurnstileHistory() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getScanDateTime() {
        return scanDateTime;
    }

    public void setScanDateTime(Date scanDateTime) {
        this.scanDateTime = scanDateTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }
}
