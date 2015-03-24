package br.com.bb.dinop.arqRedeDinop.util;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

/**
 * The Class SqlXmlResource.
 */
public class SqlXmlResource {
    public static String getSqlResource(final String queryGroup, final String sqlName) throws XPathExpressionException {
        final InputSource is = new org.xml.sax.InputSource(SqlXmlResource.class
                .getResourceAsStream("/br/com/bb/dinop/arqRedeDinop/sql.properties.xml"));
        return XPathFactory.newInstance().newXPath().evaluate(
                "/queries/query[@group='" + queryGroup + "']/sql[@name='" + sqlName + "']/text()", is);
    }
}
