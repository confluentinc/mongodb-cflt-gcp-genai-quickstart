package io.confluent.pie.quickstart.gcp.mongodb.repository.Impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.search.FieldSearchPath;
import com.mongodb.client.model.search.VectorSearchOptions;
import io.confluent.pie.quickstart.gcp.mongodb.entities.data.Medication;
import io.confluent.pie.quickstart.gcp.mongodb.repository.MedicationRepo;
import io.confluent.pie.quickstart.gcp.mongodb.utils.Lazy;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.vectorSearch;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.search.SearchPath.fieldPath;
import static java.util.Arrays.asList;

@Service
public class MedicationsRepoImpl implements MedicationRepo {

    private final String embeddingsFieldName;
    private final FieldSearchPath fieldSearchPath;
    private final String indexName;
    private final Lazy<MongoCollection<MongoMedication>> collection;

    public MedicationsRepoImpl(@Autowired MongoDatabase mongoDatabase,
                               @Value("${mongodb.field_path}") String embeddingsFieldName,
                               @Value("${mongodb.index_name}") String indexName,
                               @Value("${mongodb.collection}") String collection) {
        this.embeddingsFieldName = embeddingsFieldName;
        this.fieldSearchPath = fieldPath(embeddingsFieldName);
        this.indexName = indexName;
        this.collection = new Lazy<>(() -> mongoDatabase.getCollection(collection, MongoMedication.class));
    }

    @Override
    public List<Medication> findMedicationsByVector(List<Double> embedding, int numberOfCandidate, long limit, double minScore) {
        final boolean hasScoreFilter = minScore > 0;
        final List<Medication> medications = new ArrayList<>();

        final List<Bson> pipeline = asList(vectorSearch(
                        this.fieldSearchPath,
                        embedding,
                        this.indexName,
                        limit,
                        VectorSearchOptions.approximateVectorSearchOptions(numberOfCandidate)),
                project(fields(
                        exclude(embeddingsFieldName),
                        metaVectorSearchScore("score"))));

        collection.get().aggregate(pipeline).forEach(document -> {
            if (hasScoreFilter && document.getScore() < minScore) {
                return;
            }

            medications.add(document.toMedication());
        });

        return medications;
    }
}
