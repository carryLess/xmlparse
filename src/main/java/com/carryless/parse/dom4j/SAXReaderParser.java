package com.carryless.parse.dom4j;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author carryLess
 * @Date 2019/3/22 13:28
 * @Describe
 */
public class SAXReaderParser {

	public static List<JSONObject> bookList = new ArrayList<>();
	public static List<JSONObject> storeList = new ArrayList<>();

	public static void main(String[] args) throws Exception {

		InputStream resourceAsStream = SAXReaderParser.class.getClassLoader().getResourceAsStream("test.xml");
		SAXReader reader = new SAXReader();
		//建立 MyElementHandler 的实例
		ElementHandler bookHandler = new BookElementHandler();
		BookStoreHandler storeHandler = new BookStoreHandler();
		// 节点
		reader.addHandler("/inf/bookshelf", bookHandler);
		reader.addHandler("/inf/bookstore",storeHandler);
		Document read = reader.read(resourceAsStream);
		System.out.println(bookList);
		System.out.println(storeList);

		/**
		 * 以上是通过handler方法解析
		 * 还可以通过以下方式逐步获取节点解析
		 */
		/*
		Element rootElement = read.getRootElement();
		Iterator<Element> elementIterator = rootElement.elementIterator();
		while (elementIterator.hasNext()){
			Element next = elementIterator.next();
			//...
		}
		*/

	}
}

class BookElementHandler implements ElementHandler {
	public void onStart(ElementPath ep) {}
	public void onEnd(ElementPath ep) {
		// 获得当前节点
		Element object = ep.getCurrent();
		//获取book节点
		for (Element element : (List<Element>) object.elements()) {
			JSONObject jsonObject = new JSONObject();
			Attribute id = element.attribute("id");
			if(id != null){
				jsonObject.put("id",id.getData());
			}
			String bookElement = element.getName();
			if("book".equals(bookElement)){
				List<Element> elements = element.elements();
				for (Element e:elements) {
					jsonObject.put(e.getName(),e.getData());
				}
			}
			SAXReaderParser.bookList.add(jsonObject);
		}
		// 处理完当前节点后，将其从dom树中剪除
		object.detach();
	}
}

class BookStoreHandler implements ElementHandler {

	@Override
	public void onStart(ElementPath elementPath) {

	}

	@Override
	public void onEnd(ElementPath ep) {
		// 获得当前节点
		Element object = ep.getCurrent();
		//获取book节点
		for (Element element : (List<Element>) object.elements()) {
			JSONObject jsonObject = new JSONObject();
			String storeElement = element.getName();
			if("store".equals(storeElement)){
				List<Element> elements = element.elements();
				for (Element e:elements) {
					jsonObject.put(e.getName(),e.getData());
				}
			}
			SAXReaderParser.storeList.add(jsonObject);
		}
		// 处理完当前节点后，将其从dom树中剪除
		object.detach();
	}
}