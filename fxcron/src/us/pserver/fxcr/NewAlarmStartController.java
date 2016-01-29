/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import us.pserver.jc.Task;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class NewAlarmStartController implements Initializable, TaskFactory {
	
	public static enum TaskType {
		DIALOG(), SCRIPT(), SYSCOMMAND();
		public String toString() {
			switch(this) {
				case DIALOG:
					return "Dialog Taks";
				case SCRIPT:
					return "Script Task";
				case SYSCOMMAND:
					return "System Command Task";
				default:
					return "Not Supported Task";
			}
		}
	}

	@FXML
	private TextField nameField;

	@FXML
	private ComboBox<TaskType> taskTypeCombo;

	@FXML
	private StackPane contentPane;
	
	private Pane dialogPane;
	
	private Pane scriptPane;
	
	private NewTaskDialogController dialogController;
	
	private NewTaskCommandController commandController;
	
	private TaskFactory factory;
	

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			FXMLLoader fxl = new FXMLLoader(
					this.getClass().getResource(
							"/us/pserver/fxcr/fxml/NewTaskDialog.fxml")
					, rb
			);
			dialogPane = (Pane) fxl.load();
			dialogController = fxl.getController();
			fxl = new FXMLLoader(
					this.getClass().getResource(
							"/us/pserver/fxcr/fxml/NewTaskScript.fxml")
					, rb
			);
			scriptPane = (Pane) fxl.load();
			commandController = fxl.getController();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		taskTypeCombo.getItems().addAll(
				TaskType.DIALOG,
				TaskType.SCRIPT,
				TaskType.SYSCOMMAND
		);
	}
	
	
	public void onTaskTypeChanged(ActionEvent e) {
		TaskType type = taskTypeCombo.getSelectionModel()
				.getSelectedItem();
		contentPane.getChildren().clear();
		switch(type) {
			case DIALOG:
				contentPane.getChildren().add(dialogPane);
				factory = dialogController;
				break;
			case SCRIPT:
				contentPane.getChildren().add(scriptPane);
				factory = commandController;
				break;
			default:
				return;
		}
	}
	
	
	public Task getTask() {
		if(factory == null)
			return null;
		return factory.getTask();
	}
	

}
