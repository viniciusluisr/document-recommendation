package com.document.recommendation.infra.avro;

public class AvroTemplates {

    public static final String DOC_SCHEMA = "{\n" +
            "    \"fields\": [\n" +
            "        { \"name\": \"url\", \"type\": \"string\" },\n" +
            "        { \"name\": \"user\", \"type\": \"string\" }\n" +
            "    ],\n" +
            "    \"name\": \"document\",\n" +
            "    \"type\": \"record\"\n" +
            "}";
}
