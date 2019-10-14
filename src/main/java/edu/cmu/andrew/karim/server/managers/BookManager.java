package edu.cmu.andrew.karim.server.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.models.Book;
import edu.cmu.andrew.karim.server.models.Borrower;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookManager extends Manager{
    public static BookManager _self;
    private MongoCollection<Document> bookCollection;


    public BookManager() {
        this.bookCollection = MongoPool.getInstance().getCollection("books");
    }

    public static BookManager getInstance(){
        if (_self == null)
            _self = new BookManager();
        return _self;
    }


    public void createBook(Book book) throws AppException {

        try{
            JSONObject json = new JSONObject(book);

            Document newDoc = new Document()
                    .append("id", book.getId())
                    .append("name", book.getName())
                    .append("author", book.getAuthor())
                    .append("available", book.getAvailable());
            if (newDoc != null)
                bookCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new borrower");

        }catch(Exception e){
            throw handleException("Create Book", e);
        }

    }

    public void updateBook(Book book) throws AppException {
        try {
            Bson filter = new Document("id", book.getId());
            Bson newValue = new Document()
                    .append("id", book.getId())
                    .append("name", book.getName())
                    .append("author", book.getAuthor());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                bookCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update book details");

        } catch(Exception e) {
            throw handleException("Update Book", e);
        }
    }

    public void deleteBook(String bookId) throws AppException {
        try {
            Bson filter = new Document("id", bookId);
            bookCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Book", e);
        }
    }

    public ArrayList<Book> getBookList() throws AppException {
        try{
            ArrayList<Book> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                Book book = new Book(
                        bookDoc.getString("id"),
                        bookDoc.getString("name"),
                        bookDoc.getString("author"),
                        bookDoc.getString("available"),
                        bookDoc.getString("borrower_id"),
                        bookDoc.getString("borrower_name")
                );
                bookList.add(book);
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }

    public ArrayList<Book> getAvailableBookList() throws AppException {
        try{
            ArrayList<Book> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                if(bookDoc.getString("available").equals("true")) {
                    Book book = new Book(
                            bookDoc.getString("id"),
                            bookDoc.getString("name"),
                            bookDoc.getString("author"),
                            bookDoc.getString("available"),
                            bookDoc.getString("borrower_id"),
                            bookDoc.getString("borrower_name")
                    );
                    bookList.add(book);
                }
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }

    public ArrayList<Book> getBookById(String id) throws AppException {
        try{
            ArrayList<Book> bookList = new ArrayList<>();
            FindIterable<Document> bookDocs = bookCollection.find();
            for(Document bookDoc: bookDocs) {
                if(bookDoc.getString("id").equals(id)) {
                    Book book = new Book(
                            bookDoc.getString("id"),
                            bookDoc.getString("name"),
                            bookDoc.getString("author"),
                            bookDoc.getString("available"),
                            bookDoc.getString("borrower_id"),
                            bookDoc.getString("borrower_name")
                    );
                    bookList.add(book);
                }
            }
            return new ArrayList<>(bookList);
        } catch(Exception e){
            throw handleException("Get Book List", e);
        }
    }
}
