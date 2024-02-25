/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.robynng.usamongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.Arrays;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author Robyn
 */
public class UsaMongoDB {

    public static void main(String[] args) {
        //URI de conexión al cluester remoto MongoDB
        String connectionString = "mongodb+srv://<username>:<password>@cluster0.3wyvcvy.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        
        //establecer conexión con el servidor MongoDB
        try(MongoClient mongoClient = MongoClients.create(new ConnectionString(connectionString))) {
        //para conectarse local
        //try(MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
        
            //obtener base de datos
            MongoDatabase database = mongoClient.getDatabase("databaseRobyn");
            //obtener colección
            MongoCollection<Document> collection = database.getCollection("collectionRobyn");
            
            //insertar documento de ejemplo
            Document document = new Document("nombre", "Ejemplo3")
                    .append("edad", 43)
                    .append("ciudad", "EjemploAldea");
            collection.insertOne(document);
            
            //se establece un filtro para borrar dos documentos, de nombre Ejemplo y Ejemplo3
            Bson deleteFilter = Filters.in("nombre", Arrays.asList("Ejemplo", "Ejemplo3"));
            try {
                DeleteResult delete = collection.deleteMany(deleteFilter);
                System.out.println("\nBorrados " + delete.getDeletedCount() + " documentos.");
            }
            catch (MongoException ex) {
                System.out.println("Error al borrar: " +ex);
                ex.printStackTrace();
            }
            
            //consultar e imprimer todos los documentos en la colección
            MongoCursor<Document> cursor = collection.find().iterator();
            try {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toJson());
                }
            }
            finally {
                cursor.close();
            }
            
            //actualizar el documento recién insertado
            /**UpdateResult update = collection.updateOne(
                    Filters.eq("nombre","Ejemplo"),
                    new Document("$set", new Document("edad",21)
                    .append("ciudad", "CityModificada")));
            
            //para contar número de documentos modificados
            System.out.println("Num documentos actualizados:\n" + update.getModifiedCount());*/
        }
    }
}
