package com.tempus_indicium.app.parsing;

import com.tempus_indicium.app.App;
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
import java.util.logging.Level;

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
    private SAXParserFactory factory;
    private SAXParser parser;

    public MeasurementExtractor(InputStream inputStream, MeasurementParser measurementParser) {
        this.inputStream = inputStream;
        this.measurementParser = measurementParser;
        this.measurementsData = new LinkedList<>();
        this.factory = SAXParserFactory.newInstance();
        try {
            this.parser = this.factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    public List<Measurement> extractDataFromXML() {
        try {
            this.measurementsData.clear();
            this.parser.parse(inputStream, this);
        } catch (EndOfMeasurementsException e) {
            return this.measurementsData; // @TODO: find prettier way to stop sax parsing :|
        } catch (SAXException | IOException e) {
            return null;
        }
        return null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // the text in an opening xml tag
        // use qName to identify the tag
        if (qName.equalsIgnoreCase("MEASUREMENT")) {
            this.newMeasurement = new Measurement();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // the text in a closing xml tag
        // use qName to identify the tag
        switch (qName.toUpperCase()) {
            // </MEASUREMENT>
            case "MEASUREMENT":
//                System.out.println("closing measurement tag and adding it to the list");
                this.measurementsData.add(newMeasurement);
                break;
            case "WEATHERDATA":
                // literally throw an exception to stop sax from parsing
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
                this.newMeasurement.setTemperature(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "DEWP":
                this.newMeasurement.setDew(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "STP":
                this.newMeasurement.setStationPressure(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "SLP":
                this.newMeasurement.setSeaPressure(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "VISIB":
                this.newMeasurement.setVisibility(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "WDSP":
                this.newMeasurement.setWindSpeed(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "PRCP":
                this.newMeasurement.setRainfall(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "SNDP":
                this.newMeasurement.setSnowfall(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "FRSHTT":
                // do per bit check + set true
                if (this.currentTagValue.length() < 6)
                    break;

                if (this.currentTagValue.charAt(0) == '1') {
                    this.newMeasurement.setFreeze(true);
                }
                if (this.currentTagValue.charAt(1) == '1') {
                    this.newMeasurement.setRain(true);
                }
                if (this.currentTagValue.charAt(2) == '1') {
                    this.newMeasurement.setSnow(true);
                }
                if (this.currentTagValue.charAt(3) == '1') {
                    this.newMeasurement.setHail(true);
                }
                if (this.currentTagValue.charAt(4) == '1') {
                    this.newMeasurement.setStorm(true);
                }
                if (this.currentTagValue.charAt(5) == '1') {
                    this.newMeasurement.setTornado(true);
                }
                break;
            case "CLDC":
                this.newMeasurement.setCloudiness(this.measurementParser.standardDouble(this.currentTagValue));
                break;
            case "WNDDIR":
                this.newMeasurement.setWindDirection(this.measurementParser.windDirection(this.currentTagValue));
                break;
//            default:
//                System.out.println(qName + ":"+ this.currentTagValue);
//                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // the characters after the startElement, or after the endElement
//        System.out.println("chars:"+new String(ch, start, length));
        this.currentTagValue = new String(ch, start, length);
    }
}
