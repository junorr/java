/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



/**
 *
 * @author juno
 */
public class FXMLForm extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(
				this.getClass().getResource(
						"/jfx/fxml/FormGUI.fxml")
		);
		Scene scene = new Scene(root, 300, 250);
		
		Image img = new Image(this.getClass()
				.getResourceAsStream("/jfx/img/lock_128.png")
		);
		
		stage.setTitle("FXML Login Form");
		stage.setScene(scene);
		stage.getIcons().add(img);
		stage.show();
	}
	
	
	public static void main(String[] args) {
		FXMLForm.launch(args);
	}
	
}
