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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import us.pserver.jc.Task;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class NewTaskDialogController implements Initializable, TaskFactory {

	@FXML
	private TextField titleField;

	@FXML
	private ComboBox<?> dialogTypeCombo;

	@FXML
	private TextField headerField;

	@FXML
	private TextField textField;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	
	
	@Override
	public Task getTask() {
		return null;
	}
	

}
