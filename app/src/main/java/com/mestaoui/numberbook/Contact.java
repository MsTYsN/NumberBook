package com.mestaoui.numberbook;

public class Contact {
    private int id;
    private String fullname;
    private String code;
    private String phone;

    public Contact() {
    }

    public Contact(String fullname, String code, String phone) {
        this.fullname = fullname;
        this.code = code;
        this.phone = phone;
    }

    public Contact(int id, String fullname, String code, String phone) {
        this.id = id;
        this.fullname = fullname;
        this.code = code;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
