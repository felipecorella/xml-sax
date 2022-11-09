import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class DisplayXML extends DefaultHandler {
    private static final String CLASS_NAME = DisplayXML.class.getName();
    private final static Logger LOG = Logger.getLogger(CLASS_NAME);

    private SAXParser parser = null;

    private static   DisplayXML handler = null;

    public static void main(String args[]) {
        if (args.length == 0) {
            LOG.severe("No file to process. Usage is:" + "\njava DisplayXML <filename>");
            return;
        }
        File xmlFile = new File( args[0] );
        handler = new DisplayXML();
        handler.process(xmlFile);
    }

    private void process(File file) {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(true);

        System.out.println("Parser will " + (spf.isNamespaceAware() ? "" : "not ")
                + "be namespace aware");
        System.out.println("Parser will " + (spf.isValidating() ? "" : "not ") + "validate XML");
        try {
            parser = spf.newSAXParser();
            System.out.println("Parser object is: " + parser);
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nStarting parsing of " + file + "\n");
        try {
            parser.parse(file, this);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    public void startDocument() {
        // En este metodo va codigo que se tiene ejecutar antes
        // iniciar el analisis de los elementos  e.g. inicializar variables
        System.out.println("START-DOCUMENT");
    }

    public void endDocument() {
        // En este metodo va codigo que se tiene ejecutar antes
        // iniciar el analisis de los elementos  e.g. inicializar variables
        System.out.println("END-DOCUMENT");
    }

    public void startElement(String uri, String localName, String qname, Attributes attr) {
        System.out.printf("START-ELEMENT: local name: \'%s\' qname: \'%s\' uri: \'%s\'\n",localName, qname, uri);

       int n = attr.getLength();
       // si hay attributos, imprimir
       for (int i=0;i<n; i++) {
           String attName = attr.getLocalName(i);
           String attrType = attr.getType(i);
           String attrValue = attr.getValue(i);
           System.out.printf("\t%s = %s (%s)\n",attName,attrValue,attrType);
       }
    }

    public void endElement(String uri, String localName, String qname) {
        System.out.printf("END-ELEMENT: local name: \'%s\' qname: \'%s\' uri: \'%s\'\n",localName, qname, uri);
    }

    public void characters(char[] ch, int start, int length) {
        // Contenido/datos que tiene el elemento.
        String data = new String(ch, start, length);
        System.out.printf("CHARACTERS (%d): \"%s\"\n", length, data );
    }

    public void ignorableWhitespace(char[] ch, int start, int length) {
        String data = new String(ch, start, length);
        System.out.printf("IGNORABLE WHITESPACE (%d): \"%s\"\n",length, data );
    }
}
