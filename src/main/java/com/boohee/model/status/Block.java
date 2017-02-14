package com.boohee.model.status;

import android.text.TextUtils;

public class Block {
    public String img;
    public String link_to;
    public String name;
    public String show_more;

    public String getName() {
        return TextUtils.isEmpty(this.name) ? "" : this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShow_more() {
        return TextUtils.isEmpty(this.show_more) ? "" : this.show_more;
    }

    public void setShow_more(String show_more) {
        this.show_more = show_more;
    }

    public String getImg() {
        return TextUtils.isEmpty(this.img) ? "" : this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink_to() {
        return TextUtils.isEmpty(this.link_to) ? "" : this.link_to;
    }

    public void setLink_to(String link_to) {
        this.link_to = link_to;
    }
}
