package br.com.bb.dinop.chartjs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeriesData {
	
	public static ColorRotator rotator = new ColorRotator();
	

	private String label;
	
	private Color fillColor;
	
	private Color strokeColor;
	
	private Color pointColor;
	
	private Color pointStrokeColor;
	
	private Color pointHighlightFill;
	
	private Color pointHighlightStroke;
	
	private List data;
	
	
	public SeriesData() {
		label = null;
		fillColor = null;
		strokeColor = null;
		pointColor = null;
		pointStrokeColor = Color.WHITE;
		pointHighlightFill = Color.WHITE;
		pointHighlightStroke = null;
		data = new ArrayList();
		this.setStrokeColor(rotator.next())
				.setFillColor(JSColor.opacity(rotator.curr(), 0.5f));
	}
	
	
	public SeriesData(String label) {
		this();
		this.label = label;
	}
	
	
	public List data() {
		return data;
	}
	
	
	public SeriesData data(Object ... objs) {
		if(objs != null && objs.length > 0)
			data.addAll(Arrays.asList(objs));
		return this;
	}
	
	
	public SeriesData addData(Object o) {
		if(o != null)
			data.add(o);
		return this;
	}
	
	
	public void setData(List data) {
		if(data != null)
			this.data = data;
	}


	public String getLabel() {
		return label;
	}


	public SeriesData setLabel(String label) {
		this.label = label;
		return this;
	}


	public Color getFillColor() {
		return fillColor;
	}


	public SeriesData setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		return this;
	}
	
	
	public SeriesData disableAlphaFill() {
		if(fillColor != null)
			fillColor = JSColor.opacity(fillColor, 1f);
		return this;
	}


	public SeriesData enableAlphaFill() {
		if(fillColor != null)
			fillColor = JSColor.opacity(fillColor, 0.5f);
		return this;
	}


	public Color getStrokeColor() {
		return strokeColor;
	}


	public SeriesData setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
		this.pointColor = strokeColor;
		this.pointHighlightStroke = strokeColor;
		return this;
	}


	public Color getPointColor() {
		return pointColor;
	}


	public SeriesData setPointColor(Color pointColor) {
		this.pointColor = pointColor;
		return this;
	}


	public Color getPointStrokeColor() {
		return pointStrokeColor;
	}


	public SeriesData setPointStrokeColor(Color pointStrokeColor) {
		this.pointStrokeColor = pointStrokeColor;
		return this;
	}


	public Color getPointHighlightFill() {
		return pointHighlightFill;
	}


	public SeriesData setPointHighlightFill(Color pointHighlightFill) {
		this.pointHighlightFill = pointHighlightFill;
		return this;
	}


	public Color getPointHighlightStroke() {
		return pointHighlightStroke;
	}


	public SeriesData setPointHighlightStroke(Color pointHighlightStroke) {
		this.pointHighlightStroke = pointHighlightStroke;
		return this;
	}
	
	
	public String toJS() {
		StringBuffer sb = new StringBuffer();
		sb.append("{")
				.append("label: '").append(label).append("',")
				.append("fillColor: '").append(JSColor.toJS(fillColor)).append("',")
				.append("strokeColor: '").append(JSColor.toJS(strokeColor)).append("',")
				.append("pointColor: '").append(JSColor.toJS(pointColor)).append("',")
				.append("pointStrokeColor: '").append(JSColor.toJS(pointStrokeColor)).append("',")
				.append("pointHighlightFill: '").append(JSColor.toJS(pointHighlightFill)).append("',")
				.append("pointHighlightStroke: '").append(JSColor.toJS(pointHighlightStroke)).append("',")
				.append("data: [");
		for(int i = 0; i < data.size(); i++) {
			sb.append(data.get(i));
			if(i < data.size() -1)
				sb.append(", ");
		}
		sb.append("]}");
		return sb.toString();
	}
	
	
	public String toString() {
		return toJS();
	}
	
}
