package edu.cmu.andrew.karim.server.managers;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.cmu.andrew.karim.server.exceptions.AppException;
import edu.cmu.andrew.karim.server.exceptions.AppInternalServerException;
import edu.cmu.andrew.karim.server.models.Borrower;
import edu.cmu.andrew.karim.server.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import java.util.ArrayList;

public class BorrowerManager extends Manager{
    public static BorrowerManager _self;
    private MongoCollection<Document> borrowerCollection;


    public BorrowerManager() {
        this.borrowerCollection = MongoPool.getInstance().getCollection("borrowers");
    }

    public static BorrowerManager getInstance(){
        if (_self == null)
            _self = new BorrowerManager();
        return _self;
    }


    public void createBorrower(Borrower borrower) throws AppException {

        try{
            JSONObject json = new JSONObject(borrower);

            Document newDoc = new Document()
                    .append("id", borrower.getId())
                    .append("name", borrower.getName())
                    .append("phone", borrower.getPhone());
            if (newDoc != null)
                borrowerCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new borrower");

        }catch(Exception e){
            throw handleException("Create Borrower", e);
        }

    }

    public void updateBorrower( Borrower borrower) throws AppException {
        try {
            Bson filter = new Document("id", borrower.getId());
            Bson newValue = new Document()
                    .append("id", borrower.getId())
                    .append("name", borrower.getName())
                    .append("phone", borrower.getPhone());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                borrowerCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update borrower details");

        } catch(Exception e) {
            throw handleException("Update Borrower", e);
        }
    }

    public void deleteBorrower(String borrowerId) throws AppException {
        try {
            Bson filter = new Document("id", borrowerId);
            borrowerCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Borrower", e);
        }
    }

    public ArrayList<Borrower> getBorrowerList() throws AppException {
        try{
            ArrayList<Borrower> borrowerList = new ArrayList<>();
            FindIterable<Document> borrowerDocs = borrowerCollection.find();
            for(Document borrowerDoc: borrowerDocs) {
                Borrower borrower = new Borrower(
                        borrowerDoc.getString("id"),
                        borrowerDoc.getString("name"),
                        borrowerDoc.getString("phone")
                );
                borrowerList.add(borrower);
            }
            return new ArrayList<>(borrowerList);
        } catch(Exception e){
            throw handleException("Get Borrower List", e);
        }
    }

    public ArrayList<Borrower> getBorrowerById(String id) throws AppException {
        try{
            ArrayList<Borrower> borrowerList = new ArrayList<>();
            FindIterable<Document> borrowerDocs = borrowerCollection.find();
            for(Document borrowerDoc: borrowerDocs) {
                if(borrowerDoc.getString("id").equals(id)) {
                        Borrower borrower = new Borrower(
                            borrowerDoc.getString("id"),
                            borrowerDoc.getString("name"),
                            borrowerDoc.getString("phone")
                    );
                    borrowerList.add(borrower);
                }
            }
            return new ArrayList<>(borrowerList);
        } catch(Exception e){
            throw handleException("Get Borrower List", e);
        }
    }
}
