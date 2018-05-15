package vladi.xml.parser;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMLParser {

    private Request request;

    private List<ResultItem> result = null;

    public XMLParser(Request request) {
        this.request = request;
    }

    public void parse()
            throws ParserConfigurationException,
            SAXException, IOException {

        if (result != null) {
            return;
        }
        result = new ArrayList<>();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(new DefaultHandler() {

            private String name;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes);
                if (request.tag.equals(localName)) {
                    findName(attributes);
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                super.endElement(uri, localName, qName);
                name = null;
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                super.characters(ch, start, length);
                if (name != null) {
                    result.add(new ResultItem(name, new String(ch, start, length)));
                }
            }

            private void findName(Attributes attributes) {
                for (Request.AttributeValuePair valuePair : request.attrValuePairs) {
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (valuePair.attribute.equals(attributes.getLocalName(i))
                                && valuePair.value.equals(attributes.getValue(i))) {
                            name = valuePair.value;
                            return;
                        }
                    }
                }
                name = null;
            }
        });
        xmlReader.parse(request.file);
    }

    public void printResult() {
        if (result == null) {
            return;
        }
        for (ResultItem item : result) {
            System.out.println(item);
        }
    }

    public List<ResultItem> getResult() {
        return Collections.unmodifiableList(result);
    }

    public static class ResultItem {
        public final String name;
        public final String value;

        public ResultItem(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", name, value);
        }
    }
}
