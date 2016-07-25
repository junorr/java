/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class NextAlarmController implements Initializable {

	@FXML
	private Label timeLabel;

	@FXML
	private Label nameLabel;

	@FXML
	private Label fileLabel;

	@FXML
	private Label lastLabel;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}


	public Label getTimeLabel() {
		return timeLabel;
	}


	public Label getNameLabel() {
		return nameLabel;
	}


	public Label getFileLabel() {
		return fileLabel;
	}


	public Label getLastLabel() {
		return lastLabel;
	}
	

}
