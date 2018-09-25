package com.day.loan.entity;

import java.io.Serializable;

/**
 * Created by apple on 2017/7/16.
 */

public class WelfareBean implements Serializable {


    /**
     * name : 信用卡
     * image : http://or2eh71ll.bkt.clouddn.com/150182091528169.png?e=1501824516&token=Npg7Sanmf4z8uv3mvwwffjOvoCMYN8Ezm4T8pDrC:h3YR1zQ-qHWCJuFrtrskrvaZUOs=
     * link : http://www.shoujiweidai.com/Card/index.html
     */

    private String name;
    private String image;
    private String link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
