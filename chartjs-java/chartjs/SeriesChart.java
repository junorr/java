package br.com.bb.dinop.chartjs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SeriesChart {
	
	private List<String> labels;
	
	private List<SeriesData> dataset;
	
	
	public SeriesChart() {
		labels = new ArrayList<String>();
		dataset = new ArrayList<SeriesData>();
	}
	
	
	public List<String> labels() {
		return labels;
	}
	
	
	public List<SeriesData> dataset() {
		return dataset;
	}
	
	
	public SeriesChart addLabel(String lbl) {
		if(lbl != null)
			labels.add(lbl);
		return this;
	}
	
	
	public SeriesChart addData(SeriesData sd) {
		if(sd != null)
			dataset.add(sd);
		return this;
	}
	
	
	public SeriesChart labels(String ... lbls) {
		if(lbls != null && lbls.length > 0)
			labels.addAll(Arrays.asList(lbls));
		return this;
	}
	
	
	public SeriesChart disableAlphaFill() {
		for(int i = 0; i < dataset.size(); i++) {
			dataset.get(i).disableAlphaFill();
		}
		return this;
	}
	
	
	public SeriesChart enableAlphaFill() {
		for(int i = 0; i < dataset.size(); i++) {
			dataset.get(i).enableAlphaFill();
		}
		return this;
	}
	
	
	public SeriesChart disableFillColor() {
		for(int i = 0; i < dataset.size(); i++) {
			Color fill = dataset.get(i).getFillColor();
			if(fill != null)
				dataset.get(i).setFillColor(JSColor.opacity(fill, 0.0f));
		}
		return this;
	}
	
	
	public SeriesChart enableFillColor() {
		for(int i = 0; i < dataset.size(); i++) {
			Color fill = dataset.get(i).getFillColor();
			if(fill != null)
				dataset.get(i).setFillColor(JSColor.opacity(fill, 0.5f));
		}
		return this;
	}
	
	
	public String toJS() {
		StringBuffer sb = new StringBuffer();
		sb.append("{")
				.append("labels: [");
		for(int i = 0; i < labels.size(); i++) {
			sb.append("'").append(labels.get(i)).append("'");
			if(i < labels.size() -1)
				sb.append(", ");
		}
		sb.append("],")
				.append("datasets: ").append(dataset.toString()).append("}");
		return sb.toString();
	}
	
	
	public String toString() {
		return toJS();
	}
	
}
