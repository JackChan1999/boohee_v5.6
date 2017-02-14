package com.boohee.model;

public class Contact {
    public String cellphone;
    public String email;
    public String section;
    public String user_name;

    public Contact(String section, String user_name, String cellphone, String email) {
        this.section = section;
        this.user_name = user_name;
        this.cellphone = cellphone;
        this.email = email;
    }
}
