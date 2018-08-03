package br.com.bb.disec.aplic.html;

import java.awt.Color;

public class Style implements HtmlElement {

	public static final String
			COLOR = "color: rgba($red, $green, $blue, $alpha); ",
			
			FONT_FAMILY = "font-family: '$family'; ",
			
			FONT_SIZE = "font-size: $sizepx; ",
			
			FONT_STYLE = "font-style: italic;",
			
			FONT_WEIGHT = "font-weight: bold; ";
	
	
	private StringBuilder style;
	
	
	public Style() {
		style = new StringBuilder();
	}
	
	
	public Style addRaw(String name, String value) {
		if(name != null && value != null) {
			style.append(name).append(": ")
					.append(value).append("; ");
		}
		return this;
	}

	
	public Style setColor(Color c) {
		if(c != null) {
			style.append(COLOR.replace("$red", String.valueOf(c.getRed()))
					.replace("$green", String.valueOf(c.getGreen()))
					.replace("$blue", String.valueOf(c.getBlue()))
					.replace("$alpha", String.valueOf(round(c.getAlpha() / 255.0, 3))));
		}
		return this;
	}
	
	
	public Style setFontSize(int size) {
		if(size > 1) {
			style.append(FONT_SIZE.replace("$size", String.valueOf(size)));
		}
		return this;
	}
		
	
	public Style setFontFamily(String family) {
		if(family != null && !family.trim().isEmpty()) {
			style.append(FONT_FAMILY.replace("$family", family));
		}
		return this;
	}
		
	
	public Style setFontItalic() {
		style.append(FONT_STYLE);
		return this;
	}
		
	
	public Style setFontBold() {
		style.append(FONT_WEIGHT);
		return this;
	}
	
	
	public static double round(double num, int decsize) {
		int i = (int) num;
		double x = Math.pow(10, decsize);
		double d = Math.rint(num * x);
		return new Integer(i).doubleValue() + d / x;
	}
	
	
	public String toHtml() {
		return style.toString();
	}
	
}
