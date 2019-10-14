package edu.cmu.andrew.karim.server.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import edu.cmu.andrew.karim.server.http.exceptions.HttpBadRequestException;
import edu.cmu.andrew.karim.server.http.responses.AppResponse;
import edu.cmu.andrew.karim.server.http.utils.PATCH;
import edu.cmu.andrew.karim.server.managers.BookManager;
import edu.cmu.andrew.karim.server.managers.CheckoutManager;
import edu.cmu.andrew.karim.server.models.Book;
import edu.cmu.andrew.karim.server.models.Checkout;
import edu.cmu.andrew.karim.server.utils.AppLogger;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/checkouts")
public class CheckoutHttpInterface extends HttpInterface {

    private ObjectWriter ow;
    private MongoCollection<Document> checkoutCollection = null;

    public CheckoutHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postCheckouts(Object request, @QueryParam("bookId") String bookId, @QueryParam("borrowerId") String borrowerId){

        try{
            CheckoutManager.getInstance().createCheckout(bookId,borrowerId);
            return new AppResponse("Insert Successful" + bookId + borrowerId);

        }catch (Exception e){
            throw handleException("POST checkouts", e);
        }

    }



    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getCheckouts(@Context HttpHeaders headers){

        try{
            AppLogger.info("Got an API call");
            ArrayList<Checkout> checkouts = CheckoutManager.getInstance().getCheckoutList();

            if(checkouts != null)
                return new AppResponse(checkouts);
            else
                throw new HttpBadRequestException(0, "Problem with getting checkouts");
        }catch (Exception e){
            throw handleException("GET /checkouts", e);
        }


    }


    @DELETE
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteCheckouts(@QueryParam("bookId") String bookId, @QueryParam("borrowerId") String borrowerId){

        try{
            CheckoutManager.getInstance().deleteCheckout(borrowerId, bookId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE books/{bookId}", e);
        }

    }
}
