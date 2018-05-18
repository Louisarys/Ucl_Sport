package com.sport_ucl;

import java.util.Date;

/**
 * Created by Thomas on 25/03/2018.
 */
//Structure for a chatMessage
public class ChatMessage {

    //Parameters
    private String messageText;
    private String messageUser;
    private long messageTime;

    //Constructors
    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    //Getters/Setters
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
