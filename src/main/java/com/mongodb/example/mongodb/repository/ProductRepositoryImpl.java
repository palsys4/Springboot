package com.mongodb.example.mongodb.repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.example.mongodb.model.ProductResult;
import org.bson.BsonNull;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductRepositoryImpl implements ProductRepository{

    @Autowired
    MongoClient client;

    @Autowired
    MongoConverter converter;

    @Override
    public List<ProductResult> getProducts(String text,String locationLanguage) {
    List<ProductResult> results = new ArrayList<>();
        MongoDatabase database = client.getDatabase("sos");
        MongoCollection<Document> collection = database.getCollection("test_products");

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                        new Document("compound",
                                new Document("should", Arrays.asList(
                                        autoCompleteByProductName(text,locationLanguage),
                                        fullTextByKey(text),
                                        fullTextByDescription(text,locationLanguage),
                                        autoCompleteByTechnicalName(text))))),
                new Document("$facet",
                        new Document("results", getResultFacetAggregation())
                                .append("facets", getFacetsFacetAggregation())
                                .append("total", getTotalCount()))));

        result.forEach(doc -> results.add(converter.read(ProductResult.class,doc)));
        return results;
    }

    private List<Document> getFacetsFacetAggregation() {
        List<Document> facetsAggregationSteps = new ArrayList<>();
        facetsAggregationSteps.add(unwindVariants());
        facetsAggregationSteps.add(unwindVariantsBenefits());
        facetsAggregationSteps.add(unwindVariantProductGroups());
        facetsAggregationSteps.add(groupVariantsAndBenefits());
        facetsAggregationSteps.add(facetProjection());
        return facetsAggregationSteps;
    }

    private List<Document> getResultFacetAggregation() {
        List<Document> resultAggregationSteps = new ArrayList<>();
        resultAggregationSteps.add(addLimit());
        resultAggregationSteps.add(unwindVariants());
        resultAggregationSteps.add(groupDocuments());
       // resultAggregationSteps.add(matchBenefits());
        return resultAggregationSteps;
    }

    private Document autoCompleteByTechnicalName(String text) {
        return new Document("autocomplete",
                new Document("query", Arrays.asList(text))
                        .append("path", "variants.technical_name")
                        .append("fuzzy",
                                new Document("maxEdits", 1L)
                                        .append("prefixLength", 0L)));
    }


    private Document fullTextByDescription(String text,String locationLanguage) {
        return new Document("text",
                new Document("query", Arrays.asList(text))
                        .append("path", "description"+"."+locationLanguage));
    }

    private Document fullTextByKey(String text) {
        return new Document("text",
                new Document("query", Arrays.asList(text))
                        .append("path", "key"));
    }

    private Document autoCompleteByProductName(String text,String locationLanguage) {
        return new Document("autocomplete",
                new Document("query", Arrays.asList(text))
                        .append("path", "name"+"."+locationLanguage)
                        .append("fuzzy",
                                new Document("maxEdits", 1L)
                                        .append("prefixLength", 0L)));
    }

    private List<Document> getTotalCount() {
        return Arrays.asList(new Document("$count", "count"));
    }

    private Document facetProjection() {
        return new Document("$project",
                new Document("_id", 0L)
                        .append("benefits", 1L)
                        .append("productGroups", 1L));
    }

    private Document groupVariantsAndBenefits() {
        return new Document("$group",
                new Document("_id",
                        new BsonNull())
                        .append("benefits",
                                new Document("$addToSet", "$variants.benefits.en"))
                        .append("productGroups",
                                new Document("$addToSet", "$variants.productGroup.en")));
    }

    private Document unwindVariantProductGroups() {
        return new Document("$unwind", "$variants.productGroup");
    }

    private Document unwindVariantsBenefits() {
        return new Document("$unwind", "$variants.benefits");
    }

    private Document matchBenefits() {
        return new Document("$match",
                new Document("benefits",
                        new Document("$in", Arrays.asList("Sulfate Free"))));
    }

    private Document groupDocuments() {
        return new Document("$group",
                new Document("_id", "$_id")
                        .append("name",
                                new Document("$first", "$name.en"))
                        .append("key",
                                new Document("$first", "$key"))
                        .append("description",
                                new Document("$first", "$description.en"))
                        .append("technicalName",
                                new Document("$first", "$variants.technical_name"))
                        .append("benefits",
                                new Document("$first", "$variants.benefits.en"))
                        .append("productGroups",
                                new Document("$first", "$variants.productGroup.en"))
                        .append("slug",
                                new Document("$first", "$slug.en")));
    }

    private Document unwindVariants() {
        return new Document("$unwind",
                new Document("path", "$variants"));
    }

    private Document addLimit() {
        return new Document("$limit", 10L);
    }
}
