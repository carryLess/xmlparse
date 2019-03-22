package com.carryless.parse.sax;

import lombok.Data;

/**
 * @Author carryLess
 * @Date 2019/3/22 11:20
 * @Describe
 */
@Data
public class Book {
	private String id;
	private String name;
	private String author;
	private String year;
	private String language;
	private String price;
}
