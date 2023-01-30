package com.mongodb.example.mongodb.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


public class ProductFacets {

    private List<Object> benefits;
    private List<Object> productGroups;

    public List<Object> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<Object> benefits) {
        this.benefits = benefits;
    }

    public List<Object> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<Object> productGroups) {
        this.productGroups = productGroups;
    }
}
