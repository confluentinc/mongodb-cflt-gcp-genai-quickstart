package io.confluent.pie.quickstart.gcp.mongodb.entities.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.confluent.pie.quickstart.gcp.mongodb.repository.Impl.MongoMedication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Medication {
    @JsonProperty("alcohol_interaction")
    private String alcoholInteraction;
    @JsonProperty("brand_names")
    private List<String> brandNames;
    @JsonProperty("contraindications")
    private List<String> contraindications;
    @JsonProperty("cost_per_unit")
    private Double costPerUnit;
    @JsonProperty("country_availability")
    private List<String> countryAvailability;
    @JsonProperty("disclaimers")
    private List<String> disclaimers;
    @JsonProperty("dosage_form")
    private String dosageForm;
    @JsonProperty("drug_class")
    private String drugClass;
    @JsonProperty("duration_in_days")
    private Integer durationInDays;
    @JsonProperty("expiry_date")
    private String expiryDate;
    @JsonProperty("frequency_per_day")
    private Integer frequencyPerDay;
    @JsonProperty("generic_name")
    private String genericName;
    @JsonProperty("id")
    private String id;
    @JsonProperty("illnesses_treated")
    private List<String> illnessesTreated;
    @JsonProperty("manufacturers")
    private List<String> manufacturers;
    @JsonProperty("name")
    private String name;
    @JsonProperty("pregnancy_category")
    private String pregnancyCategory;
    @JsonProperty("prescription_required")
    private Boolean prescriptionRequired;
    @JsonProperty("route_of_administration")
    private String routeOfAdministration;
    @JsonProperty("side_effects")
    private List<String> sideEffects;
    @JsonProperty("storage_instructions")
    private String storageInstructions;
    @JsonProperty("strength")
    private String strength;
    @JsonProperty("summary")
    private String summary;
}
