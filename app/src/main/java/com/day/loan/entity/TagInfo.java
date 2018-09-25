package com.day.loan.entity;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/2 上午11:47
 * - @Email whynightcode@gmail.com
 */
public class TagInfo {
    private String title;
    private boolean isClicked;

    public TagInfo(String title) {
        this.title = title;
        isClicked = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
