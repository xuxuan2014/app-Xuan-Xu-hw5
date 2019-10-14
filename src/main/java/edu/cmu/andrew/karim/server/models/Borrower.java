package edu.cmu.andrew.karim.server.models;

public class Borrower {
    String id = null;
    String name = null;
    String phone = null;

    public Borrower(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
