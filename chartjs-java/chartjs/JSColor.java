package br.com.bb.dinop.chartjs;

import java.awt.Color;

public class JSColor {

	public static String toJS(Color c) {
		if(c == null) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("rgba(")
				.append(c.getRed()).append(",")
				.append(c.getGreen()).append(",")
				.append(c.getBlue()).append(",")
				.append((float) (c.getAlpha() / 255.0)).append(")");
		return sb.toString();
	}
	
	
	public static Color fromJS(String jsc) {
		if(jsc == null || !jsc.startsWith("rgba("))
			return null;
		try {
			jsc = jsc.substring(5, jsc.length() -1);
			String[] vals = jsc.split(",");
			int r = Integer.parseInt(vals[0]);
			int g = Integer.parseInt(vals[1]);
			int b = Integer.parseInt(vals[2]);
			float a = Float.parseFloat(vals[3]);
			return new Color(r, g, b, Math.round(a * 255));
		}
		catch(Exception e) {
			return null;
		}
	}
	
	
	public static Color brighter(Color c, float f) {
		if(c == null) return null;
		if(f <= 0 || f > 1) return c;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = c.getAlpha();
		int max = Math.max(r, g);
		max = Math.max(max, b);
		int plus = Math.round(max * f);
		r = Math.min((r + plus), 255);
		g = Math.min((g + plus), 255);
		b = Math.min((b + plus), 255);
		return new Color(r, g, b, a);
	}
	
	
	public static Color darker(Color c, float f) {
		if(c == null) return null;
		if(f <= 0 || f > 1) return c;
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int a = c.getAlpha();
		int max = Math.max(r, g);
		max = Math.max(max, b);
		int plus = Math.round(max * f);
		r = Math.max((r - plus), 0);
		g = Math.max((g - plus), 0);
		b = Math.max((b - plus), 0);
		return new Color(r, g, b, a);
	}
	
	
	public static Color color(int r, int g, int b, float a) {
		if(r < 0 || r > 255
				|| g < 0 || g > 255
				|| b < 0 || b > 255
				|| a < 0 || a > 1)
			return null;
		int alpha = Math.round(a * 255.0f);
		alpha = Math.max(alpha, 0);
		alpha = Math.min(alpha, 255);
		return new Color(r, g, b, alpha);
	}
	
	
	public static Color opacity(Color c, float a) {
		if(c == null) return null;
		return color(c.getRed(), c.getGreen(), c.getBlue(), a);
	}
	
}
