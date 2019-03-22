package com.carryless.parse.jdom;

import com.alibaba.fastjson.JSONObject;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author carryLess
 * @Date 2019/3/22 14:04
 * @Describe
 */
public class JDomParser {
	public static void main(String[] args) {
		List<JSONObject> bookList = new ArrayList<>();
		List<JSONObject> storeList = new ArrayList<>();
		// 1.创建一个SAXBuilder的对象
		SAXBuilder saxBuilder = new SAXBuilder();
		try {
			// 2.创建一个输入流，将xml文件加载到输入流中
			InputStream resourceAsStream = JDomParser.class.getClassLoader().getResourceAsStream("test.xml");
			// 3.通过saxBuilder的build方法，将输入流加载到saxBuilder中
			Document document = saxBuilder.build(resourceAsStream);
			// 4.通过document对象获取xml文件的根节点
			Element rootElement = document.getRootElement();
			// 5.获取根节点下的子节点的List集合
			List<Element> eLists = rootElement.getChildren();
			// 按节点解析
			for (Element element : eLists) {
				String elementName = element.getName();
				if("bookstore".equals(elementName)){
					//获取store节点
					List<Element> storeEList = element.getChildren();
					for (Element storeElement : storeEList) {
						JSONObject oneStore = new JSONObject();
						List<Element> storeInfList = storeElement.getChildren();
						for (Element storeInf : storeInfList) {
							oneStore.put(storeInf.getName(),storeInf.getValue());
						}
						storeList.add(oneStore);
					}
				}else if("bookshelf".equals(elementName)){
					List<Element> bookEList = element.getChildren();
					for (Element bookElement : bookEList) {
						JSONObject bookObj = new JSONObject();
						List<Attribute> attributes = bookElement.getAttributes();
						for (Attribute attr : attributes) {
							bookObj.put(attr.getName(),attr.getValue());
						}
						List<Element> bookInfEList = bookElement.getChildren();
						for (Element oneInf : bookInfEList) {
							bookObj.put(oneInf.getName(),oneInf.getValue());
						}
						bookList.add(bookObj);
					}
				}
			}
			System.out.println("书店：" + storeList);
			System.out.println("书籍：" + bookList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
