package com.tempus_indicium.app.parsing;

import com.tempus_indicium.app.db.Measurement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by peterzen on 2017-01-02.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 *
 * MeasurementExtractor:
 * Extracts the measurement data from an XML InputStream
 */
public class MeasurementExtractor extends DefaultHandler {
    private InputStream inputStream;
    private MeasurementParser measurementParser;
    private List<Measurement> measurementsData;
    private Measurement newMeasurement = null;
    private String currentTagValue = null;

    public MeasurementExtractor(InputStream inputStream, MeasurementParser measurementParser) {
        this.inputStream = inputStream;
        this.measurementParser = measurementParser;
        this.measurementsData = new LinkedList<>();
    }

    public List<Measurement> extractDataFromXML() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(inputStream, this);
        } catch (EndOfMeasurementsException e) {
            return this.measurementsData; // @TODO: find prettier way to stop sax parsing :|
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // the text in an opening xml tag
        // use qName to identify the tag
        if (qName.equalsIgnoreCase("measurement")) {
            this.newMeasurement = new Measurement();
            System.out.println("starting new Measurement");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // the text in a closing xml tag
        // use qName to identify the tag
        switch (qName.toUpperCase()) {
            // </MEASUREMENT>
            case "MEASUREMENT":
                System.out.println("closing measurement tag and adding it to the list");
                this.measurementsData.add(newMeasurement);
                break;
            case "WEATHERDATA":
                // do something to stop sax from parsing
                throw new EndOfMeasurementsException();
            case "STN":
                this.newMeasurement.setStn(this.measurementParser.stn(this.currentTagValue));
                break;
            case "DATE":
                this.newMeasurement.setAcquisitionDate(this.measurementParser.acquisitionDate(this.currentTagValue));
                break;
            case "TIME":
                this.newMeasurement.setAcquisitionTime(this.measurementParser.acquisitionTime(this.currentTagValue));
                break;
            case "TEMP":
                this.newMeasurement.setTemperature(this.measurementParser.temperature(this.currentTagValue));
                break;
//            case "DEWP":
//                this.newMeasurement.setAcquisitionDate(this.measurementParser.(this.currentTagValue));
//                break;
            default:
                System.out.println(qName + ":"+ this.currentTagValue);
                break;

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // the characters after the startElement, or after the endElement
        System.out.println("chars:"+new String(ch, start, length));
        this.currentTagValue = new String(ch, start, length);
    }
}
