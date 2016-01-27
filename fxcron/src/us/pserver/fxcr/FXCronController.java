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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class FXCronController implements Initializable {

	@FXML private FlowPane buttonPane;

	@FXML private Button btnNew;
	
	private StackPane contentPane;
	
	private NextAlarmController nextCtrl;

	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}	
	
	
	public FXCronController setContentPane(StackPane pane) {
		if(pane != null) {
			contentPane = pane;
			try {
				FXMLLoader loader = new FXMLLoader(
						this.getClass().getResource(
								"/us/pserver/fxcr/fxml/NextAlarm.fxml"), 
						ResourceBundle.getBundle("bundles.fa")
				);
				Pane nextPane = loader.load();
				nextCtrl = (NextAlarmController) loader.getController();
				contentPane.getChildren().clear();
				contentPane.getChildren().add(nextPane);
				System.out.println("* nextPane added in controller");
			} catch(IOException e) {
				throw new RuntimeException(e);
			}
		}
		return this;
	}

}
