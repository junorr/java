/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class FormGUIController implements Initializable {
	
	@FXML private Text msg;
	
	@FXML private TextField user;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}	
	

	@FXML
	public void handleLogin(ActionEvent e) {
		msg.setText("Logged as "+ 
				(user.getText() == null 
						|| user.getText().isEmpty() 
						? "Anonymous" : user.getText())
		);
	}
	

}
