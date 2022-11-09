package XML_SAX;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class x extends DefaultHandler {
    private static final String CLASS_NAME = x.class.getName();
    private final static Logger LOG = Logger.getLogger(CLASS_NAME);

    private SAXParser parser = null;
    private SAXParserFactory spf;

    private double estadosTotal;
    private double departamentosTotal;
    private boolean s1;
    private boolean s2;

    private String currentElement;
    private String id;
    private String name;
    private String lastName;
    private String sales;
    private String state;
    private String dept;

    private String keyword;

    private HashMap<String, Double> estados;
    private HashMap<String, Double> departamentos;

    public x(){
        super();
        spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(true);

        estados = new HashMap<>();
        departamentos = new HashMap<>();
    }
    private void process(File file){
        try{
            parser = spf.newSAXParser();
        } catch (SAXException | ParserConfigurationException e){
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nStarting parsing of " + file + "\n");
        try{
            keyword = state;
            parser.parse(file, this);
        } catch (IOException | SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public void startDocument() throws SAXException {
        estadosTotal = 0.0;
        departamentosTotal = 0.0;
    }

    @Override
    public void endDocument() throws SAXException {
        Set<Map.Entry<String,Double>> entries1 = estados.entrySet();

        for (Map.Entry<String,Double> entry: entries1) {
            System.out.printf("%-15.15s $%,9.2f\n",entry.getKey(),entry.getValue());
        }

        Set<Map.Entry<String,Double>> entries2 = departamentos.entrySet();

        for (Map.Entry<String,Double> entry: entries2) {
            System.out.printf("%-15.15s $%,9.2f\n",entry.getKey(),entry.getValue());
        }

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (localName.equals("sale_record")) {
            s1 = true;
            s2 = true;
        }
        currentElement = localName;
    }

    @Override
    public void characters(char[] bytes, int start, int length) throws SAXException {

        switch (currentElement) {
            case "id":
                this.id = new String(bytes, start, length);
                break;
            case "first_name":
                this.name = new String(bytes, start, length);
                break;
            case "last_name":
                this.lastName = new String(bytes, start, length);
                break;
            case "sales":
                this.sales = new String(bytes, start, length);
                break;
            case "state":
                this.state = new String(bytes, start, length);
                break;
            case "department":
                this.dept = new String(bytes, start, length);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("sale_record")) {
            double val1 = 0.0;
            try {
                val1 = Double.parseDouble(this.sales);
            } catch (NumberFormatException e) {
                LOG.severe(e.getMessage());
            }

            if (estados.containsKey(this.state)) {
                double sum1 = estados.get(this.state);
                estados.put(this.state, sum1 + val1);
            } else {
                estados.put(this.state, val1);
            }
            estadosTotal = estadosTotal + val1;
            s1 = false;
        }

        if (localName.equals("sale_record")) {
            double val2 = 0.0;
            try {
                val2 = Double.parseDouble(this.sales);
            } catch (NumberFormatException e) {
                LOG.severe(e.getMessage());
            }

            if (departamentos.containsKey(this.dept)) {
                double sum2 = departamentos.get(this.dept);
                departamentos.put(this.dept, sum2 + val2);
            } else {
                departamentos.put(this.dept, val2);
            }
            departamentosTotal = departamentosTotal + val2;
            s2 = false;
        }
    }

    private void printRecord() {
        System.out.printf(id, name, lastName, sales, state, dept);
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            LOG.severe("No file to process. Usage is:" + "\njava DeptSalesReport <keyword>");
            return;
        }
        File xmlFile = new File(args[0]);
        x handler = new x();
        handler.process( xmlFile );
    }
}
