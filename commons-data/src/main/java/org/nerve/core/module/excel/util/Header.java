package org.nerve.core.module.excel.util;

/**
 * com.zeus.core.module.excel.util
 * Created by zengxm on 2017/8/24.
 */
public class Header {
	String text;    //标题
	String field;   //对应的POJO属性，不支持复合对象
	int width = 2000; //宽度，针对导出到Excel的情况
	boolean bold=true;//标题加粗，针对Excel

	public Header(){}

	public Header(String text){
		this.text = text;
	}

	public Header(String  text,String field){
		this(text);
		this.field = field;
	}

	public Header(String text,String field,int width){
		this(text, field);
		this.width = width;
	}

	public String getText() {
		return text;
	}

	public Header setText(String text) {
		this.text = text;
		return this;
	}

	public String getField() {
		return field;
	}

	public Header setField(String field) {
		this.field = field;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public Header setWidth(int width) {
		this.width = width;
		return this;
	}

	public boolean isBold() {
		return bold;
	}

	public Header setBold(boolean bold) {
		this.bold = bold;
		return this;
	}

	@Override
	public String toString() {
		return "Header{" +
				"text='" + text + '\'' +
				", field='" + field + '\'' +
				", width=" + width +
				'}';
	}
}
