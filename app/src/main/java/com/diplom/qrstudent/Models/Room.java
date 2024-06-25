package com.diplom.qrstudent.Models;

import com.google.gson.annotations.SerializedName;

public class Room {
    @SerializedName("id")
    private Integer id;

    @SerializedName("roomName")
    private String roomName;

    @SerializedName("building")
    private String building;

    @SerializedName("busy")
    private Boolean busy = false;

    public Room() {
    }

    public Room(String roomName) {
        this.roomName = roomName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Boolean getBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }
}
