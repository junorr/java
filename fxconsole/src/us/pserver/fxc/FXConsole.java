/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxc;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;


/**
 *
 * @author juno
 */
public class FXConsole extends FlowPane {

	private TextArea text;
	
	
	public FXConsole() {
		super(0, 0);
		this.setPadding(Insets.EMPTY);
		this.setWidth(200);
		this.setHeight(120);
		text = new TextArea();
		text.setId("text");
		this.getChildren().add(text);
	}

}
