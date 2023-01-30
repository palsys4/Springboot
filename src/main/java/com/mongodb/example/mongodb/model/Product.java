package com.mongodb.example.mongodb.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

public class Product {
    private String name;
    private String key;
    private String technicalName;

    private List<String> benefits;

    private List<String> productGroups;
    private String slug;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    public List<String> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }

    public List<String> getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(List<String> productGroups) {
        this.productGroups = productGroups;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
