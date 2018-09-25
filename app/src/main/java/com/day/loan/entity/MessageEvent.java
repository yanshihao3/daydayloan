package com.day.loan.entity;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/2 下午4:46
 * - @Email whynightcode@gmail.com
 */
public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
