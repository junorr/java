/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxc;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;



/**
 *
 * @author juno
 */
public class TextFlowOutputStream extends OutputStream {
	
	private final List<String> cssList;
	
	private final Pane output;
	
	private final Charset cset;
	
	
	public TextFlowOutputStream(Pane output) {
		if(output == null)
			throw new IllegalArgumentException(
					"Invalid output Pane: "+ output);
		this.output = output;
		this.cssList = new LinkedList<>();
		this.cset = Charset.forName("UTF-8");
	}
	
	
	public TextFlowOutputStream(Pane output, String ... css) {
		this(output);
		if(css != null && css.length > 0) {
			this.cssList.addAll(Arrays.asList(css));
		}
	}
	
	
	public List<String> getStyleClass() {
		return this.cssList;
	}
	
	
	public TextFlowOutputStream addStyleClass(String css) {
		if(css != null && !css.trim().isEmpty()) {
			cssList.add(css);
		}
		return this;
	}
	
	
	@Override
	public void write(int b) {
		write(new byte[]{(byte)b});
	}
	
	
	@Override
	public void write(byte[] bs, int off, int len) {
		if(bs == null || bs.length == 0 
				|| off < 0 || len < 1 
				|| off+len > bs.length) {
			return;
		}
		Text t = new Text(new String(bs, off, len, cset));
		cssList.forEach(s->t.getStyleClass().add(s));
		output.getChildren().add(t);
	}
	
	
	@Override
	public void write(byte[] bs) {
		if(bs == null || bs.length < 1)
			return;
		write(bs, 0, bs.length);
	}
	
}
