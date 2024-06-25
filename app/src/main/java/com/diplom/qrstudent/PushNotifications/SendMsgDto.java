package com.diplom.qrstudent.PushNotifications;

public class SendMsgDto {
    private String title;
    private String message;

    public SendMsgDto(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
