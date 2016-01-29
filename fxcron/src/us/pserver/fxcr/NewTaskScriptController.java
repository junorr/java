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
import javafx.scene.control.TextField;
import us.pserver.jc.Task;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class NewTaskScriptController implements Initializable, TaskFactory {

	@FXML
	private TextField fileField;

	@FXML
	private Button openFileButton;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	
	
	public Task getTask() {
		return null;
	}
	

}
