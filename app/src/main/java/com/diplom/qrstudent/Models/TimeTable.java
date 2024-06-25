package com.diplom.qrstudent.Models;

import com.diplom.qrstudent.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TimeTable implements Serializable {
    @SerializedName("id")
    private Long id;

    @SerializedName("dayOfWeek")
    private String dayOfWeek;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("groupName")
    private String groupName;



    @SerializedName("date")
    private String date;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("subjectName")
    private String subjectName;

    @SerializedName("scanable")
    private Boolean scanable;

    @SerializedName("classroom")
    private Room classroom;

    @SerializedName("weekNumber")
    private int weekNumber;

    @SerializedName("studentIds")
    private List<Integer> studentIds;

    private Boolean attendance = false;


    public TimeTable() {
    }

    public TimeTable(String groupName, String subjectName, String dayOfWeek, String startTime, String endTime, int weekNumber, List<Integer> studentIds, Boolean attendance) {
        this.groupName = groupName;
        this.subjectName = subjectName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekNumber = weekNumber;
        this.studentIds = studentIds;
        this.attendance = attendance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public List<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.studentIds = studentIds;
    }


    public Boolean getAttendance() {
        return attendance;
    }

    public void setAttendance(Boolean attendance) {
        this.attendance = attendance;
    }

    public Room getClassroom() {
        return classroom;
    }

    public void setClassroom(Room classroom) {
        this.classroom = classroom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Boolean getScanable() {
        return scanable;
    }

    public void setScanable(Boolean scanable) {
        this.scanable = scanable;
    }

}
