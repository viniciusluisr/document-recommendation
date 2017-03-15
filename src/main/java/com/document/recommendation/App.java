package com.document.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@SpringBootApplication
public class App {

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
    }


    public static void main( String[] args ) throws InterruptedException {

        SpringApplication.run(App.class, args);

    }

}

