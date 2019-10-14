package edu.cmu.andrew.karim.server.models;

public class Book {
    String id = null;
    String name = null;
    String author = null;
    String available = "true";
    String borrower_id = null;
    String borrower_name = null;

    public Book(String id, String name, String author, String available, String borrower_id, String borrower_name) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.available = available;
        this.borrower_id = borrower_id;
        this.borrower_name = borrower_name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvailable() { return available;}

    public String getBorrower_id() { return borrower_id; }

    public String getBorrower_name() { return borrower_name; }
}
