package edu.cmu.andrew.karim.server.managers;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.models.Book;
import edu.cmu.andrew.karim.server.models.Borrower;
import edu.cmu.andrew.karim.server.models.Checkout;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckoutManager extends Manager {
    public static CheckoutManager _self;
    private MongoCollection<Document> checkoutCollection;
    private MongoCollection<Document> bookCollection;
    private MongoCollection<Document> borrowerCollection;


    public CheckoutManager() {
        this.checkoutCollection = MongoPool.getInstance().getCollection("checkouts");
        this.bookCollection = MongoPool.getInstance().getCollection("books");
        this.borrowerCollection = MongoPool.getInstance().getCollection("borrowers");
    }


    public static CheckoutManager getInstance(){
        if (_self == null)
            _self = new CheckoutManager();
        return _self;
    }

    public void createCheckout(String book_id, String borrower_id) throws AppException {

        String borrower_name = "";
        try{
            FindIterable<Document> borrowerDocs = borrowerCollection.find();
            for (Document borrowerDoc : borrowerDocs) {
                if (borrowerDoc.getString("id").equals(borrower_id)) {
                    borrower_name = borrowerDoc.getString("name");
                }
            }
            bookCollection.updateOne(Filters.eq("id", book_id), Updates.set("available", "false"));
            bookCollection.updateOne(Filters.eq("id", book_id), Updates.set("borrower_id", borrower_id));
            bookCollection.updateOne(Filters.eq("id", book_id), Updates.set("borrower_name", borrower_name));


        }catch(Exception e){
            throw handleException("Create Checkout", e);
        }

    }

    public void deleteCheckout(String borrower_id, String book_id) throws AppException {
        try {

            bookCollection.updateOne(Filters.eq("id", book_id), Updates.set("available", "true"));
            bookCollection.updateOne(Filters.eq("id", book_id), Updates.set("borrower_id", ""));
            bookCollection.updateOne(Filters.eq("id", book_id), Updates.set("borrower_name", ""));


        }catch (Exception e){
            throw handleException("Delete Checkout", e);
        }
    }

    public ArrayList<Checkout> getCheckoutList() throws AppException {
        try{
            ArrayList<Checkout> checkoutList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                if(bookDoc.getString("available").equals("false")) {
                    Checkout checkout = new Checkout(
                            bookDoc.getString("id"),
                            bookDoc.getString("name"),
                            bookDoc.getString("author"),
                            bookDoc.getString("borrower_id"),
                            bookDoc.getString("borrower_name")
                    );
                    checkoutList.add(checkout);
                }
            }
            return checkoutList;
        } catch(Exception e){
            throw handleException("Get Checkout List", e);
        }
    }

}
