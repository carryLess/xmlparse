package com.carryless.parse.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @Author carryLess
 * @Date 2019/3/22 11:13
 * @Describe 事件驱动，内存耗费较少
 */
public class SAXParse {

	public static void main(String[] args) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			SAXParserHandler handler = new SAXParserHandler();
			InputStream resourceAsStream = SAXParse.class.getClassLoader().getResourceAsStream("simpleTest.xml");
			parser.parse(resourceAsStream, handler);

			System.out.println(handler.getBookList());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class SAXParserHandler extends DefaultHandler {
	String value = null;
	Book book = null;
	private ArrayList<Book> bookList = new ArrayList<Book>();
	public ArrayList<Book> getBookList() {
		return bookList;
	}
	int bookIndex = 0;

	/**
	 * 用来标识解析开始
	 */
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		System.out.println("SAX解析开始");
	}

	/**
	 * 用来标识解析结束
	 */
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		System.out.println("SAX解析结束");
	}

	/**
	 * 解析xml元素
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//调用DefaultHandler类的startElement方法
		super.startElement(uri, localName, qName, attributes);
		if (qName.equals("book")) {
			bookIndex++;
			//创建一个book对象
			book = new Book();
			//获取book节点的属性值
			int num = attributes.getLength();
			for(int i = 0; i < num; i++){
				if (attributes.getQName(i).equals("id")) {
					book.setId(attributes.getValue(i));
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		//调用DefaultHandler类的endElement方法
		super.endElement(uri, localName, qName);
		//判断是否针对一本书已经遍历结束
		if (qName.equals("book")) {
			bookList.add(book);
			book = null;
		} else if (qName.equals("author")) {
			book.setAuthor(value);
		} else if (qName.equals("year")) {
			book.setYear(value);
		} else if (qName.equals("price")) {
			book.setPrice(value);
		} else if (qName.equals("language")) {
			book.setLanguage(value);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		value = new String(ch, start, length);
	}
}
