package io.confluent.pie.quickstart.gcp.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @BsonProperty("_id")
    private ObjectId Id;
    private String productId;
    private String name;
    private String description;
    private String currency;
    private String termMinLength;
    private String termMaxLength;
    private String repaymentFrequency;
    private String riskLevel;
    private String status;
    private String rateTable;
    private String createdAt;
    private String updatedAt;
    private String refLink;
    private String summary;
    private Double score;
}