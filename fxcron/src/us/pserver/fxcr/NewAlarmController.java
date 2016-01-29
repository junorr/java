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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import us.pserver.jc.Alarm;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class NewAlarmController implements Initializable, AlarmFactory {

	@FXML
	private Label nameLabel;

	@FXML
	private Label datetimeLabel;

	@FXML
	private HBox buttonBar;

	@FXML
	private Button cancelButton;

	@FXML
	private Button nextButton;
	
	private Alarm alarm;
	

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	
	
	public Alarm getAlarm() {
		return alarm;
	}

}
