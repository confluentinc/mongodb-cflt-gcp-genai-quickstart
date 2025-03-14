package io.confluent.pie.quickstart.gcp.mongodb.repository.Impl;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MongoMedication {
    @BsonProperty("_id")
    private ObjectId _Id;
    private Double score;


    @BsonProperty("alcohol_interaction")
    private String alcoholInteraction;
    @BsonProperty("brand_names")
    private List<String> brandNames;
    @BsonProperty("contraindications")
    private List<String> contraindications;
    @BsonProperty("cost_per_unit")
    private Double costPerUnit;
    @BsonProperty("country_availability")
    private List<String> countryAvailability;
    @BsonProperty("disclaimers")
    private List<String> disclaimers;
    @BsonProperty("dosage_form")
    private String dosageForm;
    @BsonProperty("drug_class")
    private String drugClass;
    @BsonProperty("duration_in_days")
    private Integer durationInDays;
    @BsonProperty("expiry_date")
    private String expiryDate;
    @BsonProperty("frequency_per_day")
    private Integer frequencyPerDay;
    @BsonProperty("generic_name")
    private String genericName;
    @BsonProperty("id")
    private String id;
    @BsonProperty("illnesses_treated")
    private List<String> illnessesTreated;
    @BsonProperty("manufacturers")
    private List<String> manufacturers;
    @BsonProperty("name")
    private String name;
    @BsonProperty("pregnancy_category")
    private String pregnancyCategory;
    @BsonProperty("prescription_required")
    private Boolean prescriptionRequired;
    @BsonProperty("route_of_administration")
    private String routeOfAdministration;
    @BsonProperty("side_effects")
    private List<String> sideEffects;
    @BsonProperty("storage_instructions")
    private String storageInstructions;
    @BsonProperty("strength")
    private String strength;
    @BsonProperty("summary")
    private String summary;

    public Medication toMedication() {
        return new Medication(
                this.alcoholInteraction,
                this.brandNames,
                this.contraindications,
                this.costPerUnit,
                this.countryAvailability,
                this.disclaimers,
                this.dosageForm,
                this.drugClass,
                this.durationInDays,
                this.expiryDate,
                this.frequencyPerDay,
                this.genericName,
                this.id,
                this.illnessesTreated,
                this.manufacturers,
                this.name,
                this.pregnancyCategory,
                this.prescriptionRequired,
                this.routeOfAdministration,
                this.sideEffects,
                this.storageInstructions,
                this.strength,
                this.summary
        );
    }
}
