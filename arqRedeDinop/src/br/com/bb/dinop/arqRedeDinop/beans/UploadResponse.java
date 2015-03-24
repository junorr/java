package br.com.bb.dinop.arqRedeDinop.beans;

import java.awt.Color;

public class UploadResponse {

	public static final String
		H3 = "h3",
		H4 = "h4",
		UL = "ul",
		LI = "li",
		SPAN = "span",
		END = ">",
		START = "<",
		CLOSE = "</", 
		STYLE = " style=",
		QT = "\"",
		BR = "</br>",
		COLOR = "color: rgba($red, $green, $blue, $alpha); ",
		FONT_FAMILY = "font-family: '$family'; ",
		FONT_SIZE = "font-size: $sizepx; ",
		FONT_STYLE = "font-style: italic;",
		FONT_WEIGHT = "font-weight: bold; ";
	
	
	private StringBuilder builder;
	
	
	public UploadResponse() {
		builder = new StringBuilder();
	}
	
	
	public UploadResponse clear() {
		builder.delete(0, builder.length());
		return this;
	}
	
	
	public UploadResponse addTitle(String title) {
		if(title != null && !title.trim().isEmpty()) {
			builder.append(START).append(H3).append(END)
					.append(title)
					.append(CLOSE).append(H3).append(END);
		}
		return this;
	}
		
	
	public UploadResponse addSubTitle(String title) {
		if(title != null && !title.trim().isEmpty()) {
			builder.append(START).append(H4).append(END)
					.append(title)
					.append(CLOSE).append(H4).append(END);
		}
		return this;
	}
		
	
	public UploadResponse openStyledText() {
		builder.append(START).append(SPAN)
				.append(STYLE).append(QT);
		return this;
	}
	
	
	public UploadResponse setStyleColor(Color c) {
		if(c != null) {
			builder.append(COLOR.replace("$red", String.valueOf(c.getRed()))
					.replace("$green", String.valueOf(c.getGreen()))
					.replace("$blue", String.valueOf(c.getBlue()))
					.replace("$alpha", String.valueOf(round(c.getAlpha() / 255.0, 3))));
		}
		return this;
	}
	
	
	public UploadResponse setStyleFontSize(int size) {
		if(size > 1) {
			builder.append(FONT_SIZE.replace("$size", String.valueOf(size)));
		}
		return this;
	}
		
	
	public UploadResponse setStyleFontFamily(String family) {
		if(family != null && !family.trim().isEmpty()) {
			builder.append(FONT_FAMILY.replace("$family", family));
		}
		return this;
	}
		
	
	public UploadResponse setStyleFontItalic() {
		builder.append(FONT_STYLE);
		return this;
	}
		
	
	public UploadResponse setStyleFontBold() {
		builder.append(FONT_WEIGHT);
		return this;
	}
	
	
	public UploadResponse setStyledText(String text) {
		if(text != null && !text.trim().isEmpty()) {
			builder.append(QT).append(END)
					.append(text)
					.append(CLOSE)
					.append(SPAN).append(END);
		}
		return this;
	}
	
	
	public UploadResponse openList() {
		builder.append(START).append(UL).append(END);
		return this;
	}
		
	
	public UploadResponse closeList() {
		builder.append(CLOSE).append(UL).append(END);
		return this;
	}
	
	
	public UploadResponse addListItem(String item) {
		if(item != null && !item.trim().isEmpty()) {
			builder.append(START).append(LI).append(END)
					.append(item)
					.append(CLOSE).append(LI).append(END);
		}
		return this;
	}
	
	
	public UploadResponse openListItem() {
		builder.append(START).append(LI).append(END);
		return this;
	}
	
	
	public UploadResponse closeListItem() {
		builder.append(CLOSE).append(LI).append(END);
		return this;
	}
	
	
	public UploadResponse addSimpleText(String text) {
		if(text != null && !text.trim().isEmpty()) {
			builder.append(text);
		}
		return this;
	}
	
	
	public UploadResponse setHidden(String name, String value) {
		if(name != null 
				&& !name.trim().isEmpty()
				&& value != null
				&& !value.trim().isEmpty()) {
			builder.append(START).append(SPAN)
					.append(" id='").append(name)
					.append("' name='").append(name).append("'")
					.append(" style='visibility: hidden;'")
					.append(END).append(value)
					.append(CLOSE).append(SPAN).append(END);
		}
		return this;
	}
	
	
	public UploadResponse breakLine() {
		builder.append(BR);
		return this;
	}
	
	
	public String toString() {
		return builder.toString();
	}
	
	
	public static double round(double num, int decsize) {
		int i = (int) num;
		double x = Math.pow(10, decsize);
		double d = Math.rint(num * x);
		return new Integer(i).doubleValue() + d / x;
	}
	
	
	public static void main(String[] args) {
		UploadResponse ur = new UploadResponse()
				.addTitle("Title")
				.addSubTitle("Subtitle")
				.breakLine()
				.openStyledText()
				.setStyleColor(new Color(255, 60, 60, 127))
				.setStyleFontFamily("Monospace")
				.setStyleFontSize(16)
				.setStyledText("Styled Text")
				.openList()
				.addListItem("Item 1")
				.addListItem("Item 2")
				.closeList();
		System.out.println(ur);
	}
}
