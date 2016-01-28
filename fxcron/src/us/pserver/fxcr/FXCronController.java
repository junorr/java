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
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import us.pserver.jc.Alarm;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class FXCronController implements Initializable {

	@FXML private FlowPane buttonBar;

	@FXML private Button newButton;
	
	@FXML private Button editButton;
	
	@FXML private Button removeButton;
	
	@FXML private Button termButton;
	
	@FXML private ListView<Alarm> alarmList;
	
	@FXML private StackPane content;
	
	private Pane nextPane;
	
	private Pane newPane;
	
	private Pane rptPane;
	
	private NextAlarmController nextController;
	
	private NewAlarmController newController;
	
	private RepeatAlarmController rptController;

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			FXMLLoader loader = new FXMLLoader(
					this.getClass().getResource(
							"/us/pserver/fxcr/fxml/RepeatAlarm.fxml"), 
					ResourceBundle.getBundle("bundles.fa")
			);
			rptPane = loader.load();
			rptController = (RepeatAlarmController) loader.getController();
			
			loader = new FXMLLoader(
					this.getClass().getResource(
							"/us/pserver/fxcr/fxml/NewAlarm.fxml"), 
					ResourceBundle.getBundle("bundles.fa")
			);
			newPane = loader.load();
			newController = (NewAlarmController) loader.getController();
			
			loader = new FXMLLoader(
					this.getClass().getResource(
							"/us/pserver/fxcr/fxml/NextAlarm.fxml"), 
					ResourceBundle.getBundle("bundles.fa")
			);
			nextPane = loader.load();
			nextController = (NextAlarmController) loader.getController();
			
			content.getChildren().clear();
			content.getChildren().add(nextPane);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		alarmList.setCellFactory(l->new AlarmListCell());
	}	
	
	
	private FXCronController setPane(Pane p) {
		content.getChildren().clear();
		content.getChildren().add(p);
		return this;
	}
	
	
	public FXCronController setNextAlarmPane() {
		return setPane(nextPane);
	}
	
	
	public FXCronController setNewAlarmPane() {
		return setPane(newPane);
	}
	
	
	public FXCronController setRepeatAlarmPane() {
		return setPane(rptPane);
	}
	
}
