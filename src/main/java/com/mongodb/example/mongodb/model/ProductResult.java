package com.mongodb.example.mongodb.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
public class ProductResult {

    private List<Product> results;
    private List<ProductFacets> facets;

    public List<Product> getResults() {
        return results;
    }

    private List<ProductTotal> total;

    public void setResults(List<Product> results) {
        this.results = results;
    }

    public List<ProductFacets> getFacets() {
        return facets;
    }

    public void setFacets(List<ProductFacets> facets) {
        this.facets = facets;
    }

    public List<ProductTotal> getTotal() {
        return total;
    }

    public void setTotal(List<ProductTotal> total) {
        this.total = total;
    }
}
