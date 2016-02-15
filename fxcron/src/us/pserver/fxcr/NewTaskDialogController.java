/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr;

import us.pserver.fxcr.task.TaskFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import us.pserver.fxcr.task.DialogTask;
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
	private ComboBox<Integer> dialogTypeCombo;

	@FXML
	private TextField textField;

  
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		dialogTypeCombo.getItems().addAll(
        JOptionPane.PLAIN_MESSAGE, 
        JOptionPane.INFORMATION_MESSAGE,
        JOptionPane.WARNING_MESSAGE,
        JOptionPane.ERROR_MESSAGE
    );
	}	
	
	
	@Override
	public Task getTask() {
		return new DialogTask(dialogTypeCombo
        .getSelectionModel().getSelectedItem(),
        titleField.getText(),
        textField.getText()
    );
	}
	

}
