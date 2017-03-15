package com.document.recommendation.test.application.endpoint;

import br.com.six2six.fixturefactory.Fixture;
import com.document.recommendation.domain.model.entity.Document;
import com.document.recommendation.domain.model.entity.ScoredDocument;
import com.document.recommendation.test.support.TestApiEndpoints;
import com.document.recommendation.test.support.TestFixtureSupport;
import org.joda.time.Interval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DocumentControllerLoadTest extends TestFixtureSupport {

    @Override
    public void setUp() {

    }

    // Esse método garante que a visualização de documentos execute na escala da Globo.com (3k requests por minuto)
    // Para isso, é criado um parallel stream de 3000 posições e todos devem ser processados em no máximo 60000 milissegundos (1 minuto)
    @Test
    public void viewDocuments() {
        final List<Document> docs = Fixture.from(Document.class).gimme(3000, "valid");
        final Long before = System.currentTimeMillis();

        docs
            .parallelStream()
                .forEach(doc -> {
                    ResponseEntity<HttpStatus> response = TestApiEndpoints.viewDocument(doc.getUrl(), doc.getUsers().get(0));
                    assertEquals(HttpStatus.CREATED, response.getStatusCode());
                });

        final Long after = System.currentTimeMillis();
        final Interval it = new Interval(before, after);
        assert(it.toDurationMillis() <= 60000);

        TestApiEndpoints.deleteAll();

    }

}