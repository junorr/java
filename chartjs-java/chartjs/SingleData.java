package br.com.bb.dinop.chartjs;

import java.awt.Color;


public class SingleData {
	
	public static ColorRotator rotator = new ColorRotator(); 

	private String label;
	
	private Object value;
	
	private Color color;
	
	private Color highlight;
	
	
	public SingleData() {
		label = null;
		value = null;
		color = rotator.next();
		highlight = JSColor.opacity(rotator.curr(), 0.5f);
	}
	
	
	public SingleData(String label) {
		this();
		this.label = label;
	}


	public String getLabel() {
		return label;
	}


	public SingleData setLabel(String label) {
		this.label = label;
		return this;
	}


	public Object getValue() {
		return value;
	}


	public SingleData setValue(Object value) {
		this.value = value;
		return this;
	}


	public Color getColor() {
		return color;
	}


	public SingleData setColor(Color color) {
		this.color = color;
		if(color != null)
			this.highlight = JSColor.opacity(color, 0.5f);
		return this;
	}


	public Color getHighlight() {
		return highlight;
	}


	public SingleData setHighlight(Color highlight) {
		this.highlight = highlight;
		return this;
	}
	
	
	public String toJS() {
		StringBuffer sb = new StringBuffer();
		sb.append("{")
				.append("label: '").append(label).append("', ")
				.append("value: ").append(value).append(", ")
				.append("color: '").append(JSColor.toJS(color)).append("', ")
				.append("highlight: '").append(JSColor.toJS(highlight)).append("'")
				.append("}");
		return sb.toString();
	}
	
	
	public String toString() {
		return toJS();
	}
	
}
