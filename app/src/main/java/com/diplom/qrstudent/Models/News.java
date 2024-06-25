package com.diplom.qrstudent.Models;

import java.io.Serializable;

public class News implements Serializable {
    private int id;
    private String title;
    private String content;
    private String publishDateTime;

    public News() {
    }

    public News(String title, String content, String publishDateTime) {
        this.title = title;
        this.content = content;
        this.publishDateTime = publishDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishDateTime() {
        return publishDateTime;
    }

    public void setPublishDateTime(String publishDateTime) {
        this.publishDateTime = publishDateTime;
    }
}

