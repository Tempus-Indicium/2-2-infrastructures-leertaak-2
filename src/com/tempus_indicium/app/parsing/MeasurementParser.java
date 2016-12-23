package com.tempus_indicium.app.parsing;

import com.tempus_indicium.app.db.Measurement;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class MeasurementParser extends DefaultHandler {
    List<Measurement> measurements;
    String xmlStringIn;

    public MeasurementParser(String xmlStringIn) {
        this.xmlStringIn = xmlStringIn;
        measurements = new LinkedList<>(); // @TODO: check out differences between collection types again..
        this.parseXmlString();
    }

    private void parseXmlString() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            // @TODO rewrite application to directly supply the inputStream to the xml parser
            System.out.println("parsing of xml is unfinished");
//            parser.parse(this.xmlInputStream, this);
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }


}
