package org.nerve.core.domain;

import org.nerve.domain.UpdateDateEntity;
import org.nerve.enums.Form;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/**
 * PROJECT		jpkg-manage
 * PACKAGE		com.zeus.core.domain
 * FILE			Setting.java
 * Created by 	zengxm on 2018/7/11.
 */
@Document(collection = "sys_setting")
public class Setting extends UpdateDateEntity {
	String uuid;
	String title;
	String summary;
	String defaultContent;
	String content;
	Form form = Form.TEXT;
	String formValue;
	String category;

	public Setting(){}

	public Setting(String uuid){
		if(Objects.isNull(uuid))
			throw new RuntimeException("UUID must not be null!");
		this.uuid = uuid;
	}

	public Setting(String uuid, String category){
		this(uuid);
		this.category = category;
	}

	public String getUuid() {
		return uuid;
	}

	public Setting setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Setting setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getSummary() {
		return summary;
	}

	public Setting setSummary(String summary) {
		this.summary = summary;
		return this;
	}

	public String getDefaultContent() {
		return defaultContent;
	}

	public Setting setDefaultContent(String defaultContent) {
		this.defaultContent = defaultContent;
		return this;
	}

	public String getContent() {
		return content;
	}

	public Setting setContent(String content) {
		this.content = content;
		return this;
	}

	public Form getForm() {
		return form;
	}

	public Setting setForm(Form form) {
		this.form = form;
		return this;
	}

	public String getFormValue() {
		return formValue;
	}

	public Setting setFormValue(String formValue) {
		this.formValue = formValue;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public Setting setCategory(String category) {
		this.category = category;
		return this;
	}

	@Override
	public String toString() {
		return "Setting{" +
				"uuid='" + uuid + '\'' +
				", title='" + title + '\'' +
				", summary='" + summary + '\'' +
				", defaultContent='" + defaultContent + '\'' +
				", content='" + content + '\'' +
				", form=" + form +
				", formValue='" + formValue + '\'' +
				", category='" + category + '\'' +
				'}';
	}
}
