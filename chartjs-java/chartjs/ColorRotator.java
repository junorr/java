package br.com.bb.dinop.chartjs;

import java.awt.Color;


public class ColorRotator {

	public static final Color[] colors = {
		new Color( 19, 149, 186),
		new Color(192,  46,  29),
		new Color( 13,  60,  85),
		new Color(241, 108,  32),
		new Color(235, 200,  68),
		new Color(162, 184, 145),
		new Color( 17, 120, 153),
		new Color( 15,  91, 120),
		new Color(217,  78,  31),
		new Color(239, 139,  44),
		new Color(236, 170,  56),
		new Color( 92, 167, 147)
	};
	
	
	private int index;
	
	
	public ColorRotator() {
		index = -1;
	}
	
	
	public int size() {
		return colors.length;
	}
	
	
	public ColorRotator reset() {
		index = -1;
		return this;
	}
	
	
	public Color curr() {
		return colors[(index < 0 ? 0 : index)];
	}
	
	
	public Color next() {
		index = (index + 1 >= colors.length ? 0 : index + 1);
		return colors[index];
	}
	
	
	public Color prev() {
		index = (index - 1 < 0 ? colors.length -1 : index - 1);
		return colors[index];
	}
	
	
	public ColorRotator rotate(int times) {
		if(times > 0)
			for(int i = 0; i < times; i++) {
				next();
			}
		return this;
	}
	
	
	public ColorRotator shuffle() {
		return rotate((int) (Math.random() * 100 + 1));
	}
	
	
	public ColorRotator set(int idx) {
		if(idx >= 0 && idx < colors.length)
			index = idx;
		return this;
	}
	
	
	public Color get(int idx) {
		if(idx >= 0 && idx < colors.length)
			return colors[idx];
		return null;
	}
	
}
