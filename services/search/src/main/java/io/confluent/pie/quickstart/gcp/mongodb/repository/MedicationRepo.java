package io.confluent.pie.quickstart.gcp.mongodb.repository;

import io.confluent.pie.quickstart.gcp.mongodb.entities.data.Medication;

import java.util.List;

public interface MedicationRepo {
    /**
     * Find products by vector
     *
     * @param embedding         Embedding
     * @param numberOfCandidate Number of candidate
     * @param limit             Limit
     * @param minScore          Minimum score
     * @return List of products
     */
    List<Medication> findMedicationsByVector(List<Double> embedding, int numberOfCandidate, long limit, double minScore);
}

