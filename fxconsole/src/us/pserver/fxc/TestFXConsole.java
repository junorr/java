/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 *
 * @author juno
 */
public class TestFXConsole extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Scene s = new Scene(new FXConsole(), 200, 120);
		stage.setScene(s);
		stage.show();
	}
	
	
	public static void main(String[] args) {
		TestFXConsole.launch(args);
	}
	
}
