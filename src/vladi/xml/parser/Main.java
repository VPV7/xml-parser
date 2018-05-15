package vladi.xml.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4) {
            printUsage();
            return;
        }
        Request request;
        try {
            request = new Request(args[0], args[1], args[2].split(","), args[3].split(","));
        } catch (IllegalStateException e) {
            printUsage();
            return;
        }
        XMLParser parser = new XMLParser(request);
        try {
            parser.parse();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println("Could not parse file: " + e.getMessage());
            return;
        }
        parser.printResult();
    }

    private static void printUsage() {
        System.out.println("Usage:\n\tfile tag attributes values");
        System.out.println("Attributes and values are comma separated.");
    }
}
