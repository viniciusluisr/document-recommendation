package com.document.recommendation.test.template.loader;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.document.recommendation.domain.model.entity.Document;

import java.util.Arrays;

public class DocumentTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Document.class).addTemplate("valid", new Rule() {
            {
                add("url", uniqueRandom("www.doc1.com", "www.doc2.com", "www.doc3.com", "www.doc4.com", "www.doc5.com", "www.doc6.com"));
                add("users", random(Arrays.asList("user1", "user2", "user3"), Arrays.asList("user1", "user4", "user2"), Arrays.asList("user3", "user5", "user2"), Arrays.asList("user1", "user1", "user3")));
            }
        });

    }
}