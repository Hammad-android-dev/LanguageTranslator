package com.hammad.translator;

public class userdata {
    String fullname,email,pass,phone;

    public userdata() {
    }
    public userdata(String fullname, String email, String pass, String phone) {
        this.fullname = fullname;
        this.email = email;
        this.pass = pass;
        this.phone = phone;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
