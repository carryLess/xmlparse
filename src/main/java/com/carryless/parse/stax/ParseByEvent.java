package com.carryless.parse.stax;

import com.alibaba.fastjson.JSONObject;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @Author carryLess
 * @Date 2019/3/22 14:42
 * @Describe
 */
public class ParseByEvent {

	private static String value = null;
	private static JSONObject oneBook = null;

	public static void main(String[] args) throws XMLStreamException {
		// Create the XML input factory
		XMLInputFactory factory = XMLInputFactory.newInstance();
		// Create the XML event reader
		//FileReader reader = new FileReader("test.xml");
		InputStream resourceAsStream = ParseByEvent.class.getClassLoader().getResourceAsStream("simpleTest.xml");
		XMLEventReader r = factory.createXMLEventReader(resourceAsStream);
		// Loop over XML input stream and process events
		while(r.hasNext()) {
			XMLEvent e = (XMLEvent) r.next();
			processEvent(e);
		}
	}
	/**
	 * Process a single XML event
	 * @param e - the event to be processed
	 */
	private static void processEvent(XMLEvent e) {
		if (e.isStartElement()) {
			String qname = ((StartElement) e).getName().toString();
			if("book".equals(qname)){
				oneBook = new JSONObject();
				Iterator iter = ((StartElement) e).getAttributes();
				while (iter.hasNext()) {
					Attribute attr = (Attribute) iter.next();
					QName attributeName = attr.getName();
					String attributeValue = attr.getValue();
					oneBook.put(attributeName.toString(),attributeValue);
				}
			}

		}
		if (e.isEndElement()) {
			String qname = ((EndElement) e).getName().toString();
			if (qname.equals("book")) {
				System.out.println(oneBook);
				value = null;
				oneBook = null;
			} else if (qname.equals("author")) {
				oneBook.put("author",value);
			} else if (qname.equals("year")) {
				oneBook.put("year",value);
			} else if (qname.equals("price")) {
				oneBook.put("price",value);
			} else if (qname.equals("language")) {
				oneBook.put("language",value);
			}
		}
		if (e.isCharacters()) {
			String text = ((Characters) e).getData();
			value = text;
		}
		if (e.isStartDocument()) {
			String version = ((StartDocument) e).getVersion();
			System.out.println("version:" + version);
			String encoding = ((StartDocument) e).getCharacterEncodingScheme();
			System.out.println("encoding:" + encoding);
			boolean isStandAlone = ((StartDocument) e).isStandalone();
			System.out.println("isStandAlone:" + isStandAlone);
		}
	}
}
