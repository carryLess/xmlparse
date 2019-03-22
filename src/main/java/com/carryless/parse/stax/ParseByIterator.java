package com.carryless.parse.stax;

import com.alibaba.fastjson.JSONObject;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author carryLess
 * @Date 2019/3/22 15:11
 * @Describe 基于指针
 */
public class ParseByIterator {

	private static List<JSONObject> bookList = new ArrayList<>();
	private static List<JSONObject> storeList = new ArrayList<>();

	public static void main(String[] args) throws XMLStreamException {
		// Create an input factory
		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		InputStream resourceAsStream = ParseByIterator.class.getClassLoader().getResourceAsStream("test.xml");
		// Create an XML stream reader
		XMLStreamReader xmlr = xmlif.createXMLStreamReader(resourceAsStream);
		// Loop over XML input stream and process events
		while (xmlr.hasNext()) {
			processEvent(xmlr);
			xmlr.next();
		}
		System.out.println("书店：" + storeList);
		System.out.println("书籍：" + bookList);
	}
	/**
	 * Process a single event
	 * @param xmlr - the XML stream reader
	 */
	private static void processEvent(XMLStreamReader xmlr) throws XMLStreamException {
		int eventType = xmlr.getEventType();
		switch (eventType) {
			case XMLStreamConstants.START_ELEMENT :
				processName(xmlr);
				break;
			case XMLStreamConstants.END_ELEMENT :
				processName(xmlr);
				break;
		}
	}
	private static void processName(XMLStreamReader xmlr) throws XMLStreamException {
		String localName = xmlr.getLocalName();
		if("bookstore".equals(localName)){
			parseBookStoreEntity(xmlr);
		}else if("bookshelf".equals(localName)){
			parseBookshelfEntity(xmlr);
		}
	}

	/**
	 * 解析书店 <bookstore></bookstore>
	 * @param reader
	 * @throws XMLStreamException
	 */
	private static void parseBookStoreEntity(XMLStreamReader reader) throws XMLStreamException {
		while (reader.hasNext()){
			int nextType = reader.next();
			switch (nextType){
				case XMLStreamConstants.START_ELEMENT :
					String localName = reader.getLocalName();
					if("store".equals(localName)){
						parseStoreEntity(reader);
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if("bookstore".equals(reader.getLocalName())){
						return;
					}
					break;
			}
		}
	}

	/**
	 * 解析<store></store>
	 * @param reader
	 * @throws XMLStreamException
	 */
	private static void parseStoreEntity(XMLStreamReader reader) throws XMLStreamException {
		JSONObject oneStore = new JSONObject();
		while (reader.hasNext()){
			int nextType = reader.next();
			switch (nextType){
				case XMLStreamConstants.START_ELEMENT:
					String localName =reader.getLocalName();
					String elementText = reader.getElementText();
					oneStore.put(localName,elementText);
					break;
				case XMLStreamConstants.END_ELEMENT:
					if("store".equals(reader.getLocalName())){
						storeList.add(oneStore);
						return;
					}
					break;
			}
		}
	}

	/**
	 * 解析<bookshelf></bookshelf>
	 * @param reader
	 * @throws XMLStreamException
	 */
	private static void parseBookshelfEntity(XMLStreamReader reader) throws XMLStreamException {
		while (reader.hasNext()){
			int nextType = reader.next();
			switch (nextType){
				case XMLStreamConstants.START_ELEMENT:
					String localName = reader.getLocalName();
					if("book".equals(localName)){
						parseBookEntity(reader);
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if("bookshelf".equals(reader.getLocalName())){
						return;
					}
					break;
			}
		}
	}

	/**
	 * 解析<book></book>
	 * @param reader
	 * @throws XMLStreamException
	 */
	private static void parseBookEntity(XMLStreamReader reader) throws XMLStreamException {
		JSONObject oneBook = new JSONObject();
		//获取book标签上的属性
		int attributeCount = reader.getAttributeCount();
		for(int i = 0; i < attributeCount; i++){
			oneBook.put(reader.getAttributeName(i).toString(),reader.getAttributeValue(i));
		}
		while(reader.hasNext()){
			int nextType = reader.next();
			switch (nextType){
				case XMLStreamConstants.START_ELEMENT:
					String localName = reader.getLocalName();
					String elementText = reader.getElementText();
					oneBook.put(localName,elementText);
					break;
				case XMLStreamConstants.END_ELEMENT:
					if("book".equals(reader.getLocalName())){
						bookList.add(oneBook);
						return;
					}
					break;
			}
		}
	}

}
