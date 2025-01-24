package io.confluent.pie.quickstart.gcp.mongodb.repository;

import io.confluent.pie.quickstart.gcp.mongodb.entities.Product;

import java.util.List;

public interface ProductRepo {
    /**
     * Find products by vector
     *
     * @param embedding         Embedding
     * @param numberOfCandidate Number of candidate
     * @param limit             Limit
     * @param minScore          Minimum score
     * @return List of products
     */
    List<Product> findProductsByVector(List<Double> embedding, int numberOfCandidate, long limit, double minScore);
}

