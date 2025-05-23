package com.structure.ChatVedioConsultation;

import java.time.LocalDateTime;

public class ChatClient {

    private String sender ;
    private String receiver ;
    private String message ;
    private LocalDateTime time ;

    public ChatClient(String sender, String receiver, String message, LocalDateTime time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }
    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getTime() {
        return time;
    }
    @Override
    public String toString() {
        return "ChatClient{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }


}
