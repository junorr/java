/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 *
 * @author juno
 */
public class FXCron extends Application {
	
	@Override
	public void start(Stage stage) throws IOException {
		Font.loadFont(this.getClass().getResource(
				"/us/pserver/fxcr/font/fontawesome-webfont.ttf")
				.toExternalForm(), 12
		);
		FXMLLoader loader = new FXMLLoader(
				this.getClass().getResource(
						"/us/pserver/fxcr/fxml/FXCron.fxml"),
				ResourceBundle.getBundle("bundles.fa")
		);
		Region root = loader.load();
		FXCronController ctrl = (FXCronController) loader.getController();
		StackPane content = (StackPane) root.lookup("#contentPane");
		ctrl.setContentPane(content);
		
		Scene scene = new Scene(root, 800, 500);
		stage.setTitle("FXCron");
		stage.setScene(scene);
		stage.show();
	}


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	

}
