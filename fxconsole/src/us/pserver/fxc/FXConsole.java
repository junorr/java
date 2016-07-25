/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxc;

import java.io.PrintStream;
import java.net.URL;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;


/**
 *
 * @author juno
 */
public class FXConsole extends ScrollPane {

	public static final String 
			CSS_BG_COLOR = "fxc-bg-color",
			CSS_STD_COLOR = "fxc-std-color",
			CSS_ERR_COLOR = "fxc-err-color",
			CSS_FONT = "fxc-font";
	
	
	private TextFlow flow;
	
	private PrintStream stdout;
	
	private PrintStream errout;
	
	
	public FXConsole() {
		super();
		flow = new TextFlow();
		flow.getStylesheets().clear();
		flow.getStylesheets().add(
				this.getClass().getResource(
						"/us/pserver/fxc/css/fxconsole.css")
						.toExternalForm()
		);
		flow.setOnScroll(e->{
			double v = (e.getDeltaY() > 0 ? 0.05 : -0.05);
			this.setVvalue(this.getVvalue() - v);
		});
		flow.getStyleClass().add(CSS_BG_COLOR);
		stdout = new PrintStream(
				new TextFlowOutputStream(flow, CSS_FONT, CSS_STD_COLOR)
		);
		errout = new PrintStream(
				new TextFlowOutputStream(flow, CSS_FONT, CSS_ERR_COLOR)
		);
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setContent(flow);
		flow.heightProperty().addListener((o,v,n)->{
			this.setVvalue(flow.getHeight());
		});
		this.widthProperty().addListener(
				(o,v,n)->flow.setMinWidth(n.doubleValue() -2)
		);
		this.heightProperty().addListener(
				(o,v,n)->flow.setMinHeight(n.doubleValue() -2)
		);
	}
	
	
	public PrintStream getStdOut() {
		return stdout;
	}
	
	
	public PrintStream getErrOut() {
		return errout;
	}
	
	
	public void clear() {
		flow.getChildren().clear();
	}
	
	
	public void setStylesheet(String src) {
		if(src != null) {
			flow.getStylesheets().clear();
			flow.getStylesheets().add(src);
		}
	}
	
}
