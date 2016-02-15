/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
	
	@FXML
	private StackPane content;
  
  private Pane alarmPane;
  
  private NewAlarmStartController alarmCtrl;
	
	private Alarm alarm;
	

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
			FXMLLoader fxl = new FXMLLoader(
					this.getClass().getResource(
							"/us/pserver/fxcr/fxml/NewAlarmStart.fxml")
					, rb
			);
      try {
        alarmPane = (Pane) fxl.load();
        alarmCtrl = fxl.getController();
        content.getChildren().clear();
        content.getChildren().add(alarmPane);
      } catch(IOException e) {
        throw new RuntimeException(e);
      }
	}	
	
	
	public Alarm getAlarm() {
		return alarm;
	}

}
