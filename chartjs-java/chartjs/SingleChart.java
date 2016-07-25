package br.com.bb.dinop.chartjs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SingleChart {

	private List<SingleData> data;
	
	
	public SingleChart() {
		data = new ArrayList<SingleData>();
	}
	
	
	public List<SingleData> data() {
		return data;
	}
	
	
	public SingleChart data(SingleData ... dts) {
		if(dts != null && dts.length > 1) {
			data.addAll(Arrays.asList(dts));
		}
		return this;
	}
	
	
	public SingleChart addData(SingleData dt) {
		if(dt != null)
			data.add(dt);
		return this;
	}
	
	
	public String legendOption() {
		return "{legendTemplate: \"<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>\"}";
	}
	
	
	public String toJS() {
		return data.toString();
	}
	
	
	public String toString() {
		return toJS();
	}
	
}
