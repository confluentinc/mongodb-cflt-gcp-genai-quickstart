package io.confluent.pie.quickstart.gcp.mongodb.repository.Impl;

import io.confluent.pie.quickstart.gcp.mongodb.entities.data.Medication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MongoMedication extends Medication {
    @BsonProperty("_id")
    private ObjectId _Id;
    private Double score;

//    public MongoMedication(String alcoholInteraction,
//                           List<String> brandNames,
//                           List<String> contraindications,
//                           Double costPerUnit,
//                           List<String> countryAvailability,
//                           List<String> disclaimers,
//                           String dosageForm,
//                           String drugClass,
//                           Integer durationInDays,
//                           String expiryDate,
//                           Integer frequencyPerDay,
//                           String genericName,
//                           String id,
//                           List<String> illnessesTreated,
//                           List<String> manufacturers,
//                           String name,
//                           String pregnancyCategory,
//                           Boolean prescriptionRequired,
//                           String routeOfAdministration,
//                           List<String> sideEffects,
//                           String storageInstructions,
//                           String strength,
//                           String summary,
//                           ObjectId id1,
//                           Double score) {
//        super(alcoholInteraction, brandNames, contraindications, costPerUnit, countryAvailability, disclaimers, dosageForm, drugClass, durationInDays, expiryDate, frequencyPerDay, genericName, id, illnessesTreated, manufacturers, name, pregnancyCategory, prescriptionRequired, routeOfAdministration, sideEffects, storageInstructions, strength, summary);
//        Id = id1;
//        this.score = score;
//    }
}
