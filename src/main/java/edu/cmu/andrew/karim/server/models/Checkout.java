package edu.cmu.andrew.karim.server.models;

public class Checkout {
    String book_id = null;
    String title = null;
    String author = null;
    String borrower_id;
    String borrower_name;

    public Checkout(String book_id, String title, String author, String borrower_id, String borrower_name) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.borrower_id = borrower_id;
        this.borrower_name = borrower_name;
    }
}
