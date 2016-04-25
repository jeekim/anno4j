package com.github.anno4j.similarity.model;

import com.github.anno4j.model.Annotation;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.similarity.SimilarityTestSetup;
import com.github.anno4j.similarity.ontologies.ANNO4JREC;
import org.junit.Test;
import org.openrdf.repository.RepositoryException;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Suite to test the {@link com.github.anno4j.similarity.model.SimilarityStatement}
 */
public class SimilarityStatementTest extends SimilarityTestSetup {

    private final static String SOME_PAGE = "http://example.org/";

    @Test
    public void testSimilarityStatement() throws Exception {
        ResourceObject subject = anno4j.createObject(ResourceObject.class);
        subject.setResourceAsString(SOME_PAGE + "subject");
        ResourceObject object = anno4j.createObject(ResourceObject.class);
        object.setResourceAsString(SOME_PAGE + "object");
        double similarity = 1.0;

        // Create an annotation
        Annotation annotation = anno4j.createObject(Annotation.class);

        // Create the statement
        SimilarityStatement statement = anno4j.createObject(SimilarityStatement.class);
        statement.setSubject(subject);
        statement.setObject(object);
        statement.setPredicateAsString(ANNO4JREC.SIMILAR_TO);
        statement.setSimilarityValue(similarity);

        // Connect the statement with subject and predicate
        statement.setSubject(subject);
        statement.setObject(object);

        // Conntect annotation and body
        annotation.setBody(statement);

        // Negative test, query for all SimilarityStatements
        List<Annotation> result = queryService.addCriteria("oa:hasBody[is-a arec:SimilarityStatement]").execute();

        // Query object
        result = queryService.execute();

        assertEquals(1, result.size());

//        SimilarityStatement resultObject = result.get(0);
        Annotation resultObject = result.get(0);
        SimilarityStatement resultStatement = (SimilarityStatement) resultObject.getBody();

        // Tests
        assertEquals(statement.getResource(), resultStatement.getResource());

        assertEquals(statement.getSubject().getResource(), resultStatement.getSubject().getResource());
        assertEquals(statement.getObject().getResource(), resultStatement.getObject().getResource());
        assertEquals(ANNO4JREC.SIMILAR_TO, resultStatement.getPredicate().toString());
    }

    @Override
    protected void persistTestData() throws RepositoryException, InstantiationException, IllegalAccessException {}
}