package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.managers.BookManager;
import edu.cmu.andrew.karim.server.models.Book;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/books")
public class BookHttpInterface extends HttpInterface{
    private ObjectWriter ow;
    private MongoCollection<Document> bookCollection = null;

    public BookHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postBooks(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Book newbook = new Book(
                    json.getString("id"),
                    json.getString("name"),
                    json.getString("author"),
                    "true",
                    null,
                    null
            );
            BookManager.getInstance().createBook(newbook);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST books", e);
        }

    }



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getBooks(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Book> books = BookManager.getInstance().getBookList();

            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books", e);
        }


    }

    @GET
    @Path("/{bookId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleBook(@Context HttpHeaders headers, @PathParam("bookId") String bookId){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Book> books = BookManager.getInstance().getBookById(bookId);

            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books/{bookId}", e);
        }
    }

    @GET
    @Path("/?available=true")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getAvailableBook(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Book> books = BookManager.getInstance().getAvailableBookList();

            if(books != null)
                return new AppResponse(books);
            else
                throw new HttpBadRequestException(0, "Problem with getting books");
        }catch (Exception e){
            throw handleException("GET /books/{bookId}", e);
        }
    }


    @PATCH
    @Path("/{bookId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchBooks(Object request, @PathParam("bookId") String bookId){

        JSONObject json = null;

        try{
            ArrayList<Book> books = BookManager.getInstance().getBookById(json.getString("id"));
            if (books.isEmpty()) throw new Exception("shabi mei you zhe ben shu");

            json = new JSONObject(ow.writeValueAsString(request));
            Book newBook = new Book(
                    json.getString("id"),
                    json.getString("name"),
                    json.getString("author"),
                    json.getString("available"),
                    books.get(0).getBorrower_id(),
                    books.get(0).getBorrower_name()
            );

            BookManager.getInstance().updateBook(newBook);

        }catch (Exception e){
            throw handleException("PATCH books/{bookId}", e);
        }

        return new AppResponse("Update Successful");
    }




    @DELETE
    @Path("/{bookId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteBooks(@PathParam("bookId") String bookId){

        try{
            BookManager.getInstance().deleteBook( bookId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE books/{bookId}", e);
        }

    }
}
