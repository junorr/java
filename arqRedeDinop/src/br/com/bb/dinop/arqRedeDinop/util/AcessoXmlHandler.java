package br.com.bb.dinop.arqRedeDinop.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The Class AcessoXmlHandler.
 */
public class AcessoXmlHandler extends org.xml.sax.helpers.DefaultHandler {
    /** The chave aplicativo. */
    private String chaveAplicativo = null;
    /** The processa acesso. */
    private boolean processaAcesso = false;
    /** The processa depe. */
    private boolean processaDepe = false;
    /** The processa chaves. */
    private boolean processaChaves = false;
    /** The processa funcoes. */
    private boolean processaFuncoes = false;
    /** The depes. */
    private final StringBuffer depes = new StringBuffer();
    /** The chaves. */
    private final StringBuffer chaves = new StringBuffer();
    /** The funcoes. */
    private final StringBuffer funcoes = new StringBuffer();

    /**
     * Instantiates a new acesso xml handler.
     */
    public AcessoXmlHandler() {
        super();
    }

    /**
     * Instantiates a new acesso xml handler.
     * 
     * @param chaveAplicativo
     *            the chave aplicativo
     * @throws SAXException
     *             the SAX exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ParserConfigurationException
     */
    public AcessoXmlHandler(final String chaveAplicativo) throws SAXException, IOException,
            ParserConfigurationException {
        super();
        this.chaveAplicativo = chaveAplicativo;
        // final org.apache.xerces.parsers.SAXParser sp = new org.apache.xerces.parsers.SAXParser();
        // sp.setContentHandler(this);
        // sp.setErrorHandler(this);
        final SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
        final InputStream is = SqlXmlResource.class.getClassLoader().getResourceAsStream(
                "br/com/bb/dinop/arqRedeDinop/acessos.properties.xml");
        final InputSource iss = new InputSource(is);
        sp.parse(iss, this);
    }

    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (this.processaDepe) {
            final char[] part = new char[length];
            System.arraycopy(ch, start, part, 0, length);
            this.depes.append(new String(part));
        }
        if (this.processaChaves) {
            final char[] part = new char[length];
            System.arraycopy(ch, start, part, 0, length);
            this.chaves.append(new String(part));
        }
        if (this.processaFuncoes) {
            final char[] part = new char[length];
            System.arraycopy(ch, start, part, 0, length);
            this.funcoes.append(new String(part));
        }
    }

    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(final String uri, final String localName, final String qName)
            throws org.xml.sax.SAXException {
        if (this.processaAcesso && qName.equalsIgnoreCase("acesso")) {
            this.processaAcesso = false;
        }
        if (this.processaAcesso && qName.equalsIgnoreCase("depes")) {
            this.processaDepe = false;
        }
        if (this.processaAcesso && qName.equalsIgnoreCase("chaves")) {
            this.processaChaves = false;
        }
        if (this.processaAcesso && qName.equalsIgnoreCase("funcoes")) {
            this.processaFuncoes = false;
        }
    }

    /**
     * Gets the chaves autorizadas.
     * 
     * @return the chaves autorizadas
     */
    public String getChavesAutorizadas() {
        if (this.chaves.length() == 0) {
            return null;
        }
        return this.chaves.toString();
    }

    /**
     * Gets the depes autorizadas.
     * 
     * @return the depes autorizadas
     */
    public String getDepesAutorizadas() {
        if (this.depes.length() == 0) {
            return null;
        }
        return this.depes.toString();
    }
    
    /**
     * Gets the funcoes autorizadas.
     * 
     * @return the funcoes autorizadas
     */
    public String getFuncoesAutorizadas() {
        if (this.funcoes.length() == 0) {
            return null;
        }
        return this.funcoes.toString();
    }

    /*
     * (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String,
     * org.xml.sax.Attributes)
     */
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if (!this.processaAcesso && qName.equalsIgnoreCase("acesso")
                && atts.getValue("id").equalsIgnoreCase(this.chaveAplicativo)) {
            this.processaAcesso = true;
        }
        this.processaDepe = this.processaAcesso && qName.equalsIgnoreCase("depes");
        this.processaChaves = this.processaAcesso && qName.equalsIgnoreCase("chaves");
        this.processaFuncoes = this.processaAcesso && qName.equalsIgnoreCase("funcoes");
    }
}
