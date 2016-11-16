package com.github.anno4j.schema_parsing;

import com.github.anno4j.Anno4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.helpers.RDFHandlerBase;

/**
 * AbstractRDFHandler used to parse a given RDF schema file.
 */
class SchemaParsingHandler extends RDFHandlerBase {

    private Anno4j anno4j;

    SchemaParsingHandler(Anno4j anno4j) {
        this.anno4j = anno4j;
    }

    @Override
    public void handleStatement(Statement st) {
        try {
            Resource subject = new URIImpl(st.getSubject().toString());
            URI predicate = new URIImpl(st.getPredicate().toString());

            // The object needs special treatment when a language is associated
            Value object;
            String objectString = st.getObject().toString();
            if(objectString.length() > 3 && objectString.charAt(objectString.length() - 3) == '@') {
                String language = objectString.substring(objectString.length() - 2);

                object = ValueFactoryImpl.getInstance().createLiteral(st.getObject().stringValue(), language);
            } else {
                // The object of the statement is not a literal with language tag, so add the given URI
                UrlValidator validator = new UrlValidator();

                if(validator.isValid(objectString)) {
                    object = new URIImpl(st.getObject().stringValue());
                } else {
                    object = ValueFactoryImpl.getInstance().createLiteral(st.getObject().stringValue());
                }
            }

            Statement statement = new StatementImpl(subject, predicate, object);

            this.anno4j.getRepository().getConnection().add(statement);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }
}
