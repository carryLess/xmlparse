package com.carryless.parse.dom;

import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author carryLess
 * @Date 2019/3/22 10:20
 * @Describe 文件一次性读取，形成树结构，存于内存，可反复读取，不适合大的文件，容易造成内存溢出
 */
public class DomParser {

	public static void main(String[] args) {
		//创建一个DocumentBuilderFactory的对象
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		//创建一个DocumentBuilder的对象
		try {
			//创建DocumentBuilder对象
			DocumentBuilder db = dbf.newDocumentBuilder();
			//通过DocumentBuilder对象的parser方法加载test.xml文件到当前项目下
			//Document document = db.parse("test.xml");
			InputStream testXmlStream = DomParser.class.getClassLoader().getResourceAsStream("test.xml");
			Document document = db.parse(testXmlStream);

			List<JSONObject> bookStoreList = new ArrayList<>();
			//<bookstore> 节点的集合
			NodeList bookStoreNodeList = document.getElementsByTagName("bookstore");
			Element bookStoreNode = (Element) bookStoreNodeList.item(0);
			// <store> 节点的集合
			NodeList storeNodes = bookStoreNode.getElementsByTagName("store");
			//遍历store节点
			int length = storeNodes.getLength();
			for(int i = 0; i < length; i++){
				JSONObject oneStoreObject = new JSONObject();
				Node storeNode = storeNodes.item(i);
				NodeList storeInfNodes = storeNode.getChildNodes();
				for(int j = 0; j < storeInfNodes.getLength(); j++){
					Node oneOfStoreInf = storeInfNodes.item(j);
					short nodeType = oneOfStoreInf.getNodeType();
					if(nodeType == Element.ELEMENT_NODE){
						oneStoreObject.put(oneOfStoreInf.getNodeName(),oneOfStoreInf.getFirstChild().getNodeValue());
					}
				}
				bookStoreList.add(oneStoreObject);
			}

			List<JSONObject> books = new ArrayList<>();
			//<book> 节点的集合
			NodeList bookList = document.getElementsByTagName("book");
			//遍历每一个book节点
			for (int i = 0; i < bookList.getLength(); i++) {
				JSONObject oneBookObject = new JSONObject();
				Node book = bookList.item(i);
				//获取book节点的所有属性集合
				NamedNodeMap attrs = book.getAttributes();
				//遍历book的属性
				for (int j = 0; j < attrs.getLength(); j++) {
					Node attr = attrs.item(j);
					//属性名和属性值
					oneBookObject.put(attr.getNodeName(),attr.getNodeValue());
				}
				//解析book节点的子节点
				NodeList childNodes = book.getChildNodes();
				//遍历childNodes获取每个节点的节点名和节点值
				for (int k = 0; k < childNodes.getLength(); k++) {
					Node oneBookChildItem = childNodes.item(k);
					//区分出text类型的node以及element类型的node
					if (oneBookChildItem.getNodeType() == Node.ELEMENT_NODE) {
						oneBookObject.put(oneBookChildItem.getNodeName(),oneBookChildItem.getFirstChild().getNodeValue());
					}
				}
				books.add(oneBookObject);
			}
			System.out.print("书店信息：");
			System.out.println(bookStoreList);
			System.out.print("书籍信息：");
			System.out.println(books);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

}
