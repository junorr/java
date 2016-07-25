/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxc.test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import us.pserver.fxc.FXConsole;


/**
 *
 * @author juno
 */
public class TestFXConsole extends Application {
	
	static FXConsole console;
	
	@Override
	public void start(Stage stage) {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(25));
		grid.setHgap(10);
		grid.setVgap(10);
		
		console = new FXConsole();
		console.setPrefSize(300, 100);
		grid.add(console, 0, 0, 2, 1);
		
		TextField fstd = new TextField();
		fstd.setPromptText("stdout");
		fstd.setOnKeyPressed(k->{
			if(k.getCode() == KeyCode.ENTER) {
				console.getStdOut().println(fstd.getText());
				fstd.setText("");
			}
			if(k.isControlDown() && k.getCode() == KeyCode.L) {
				console.clear();
			}
		});
		grid.add(fstd, 0, 1, 2, 1);
		
		TextField ferr = new TextField();
		ferr.setPromptText("errout");
		ferr.setOnKeyPressed(k->{
			if(k.getCode() == KeyCode.ENTER) {
				console.getErrOut().println(ferr.getText());
				ferr.setText("");
			}
			if(k.isControlDown() && k.getCode() == KeyCode.L) {
				console.clear();
			}
		});
		grid.add(ferr, 0, 2, 2, 1);
		
		Scene scene = new Scene(grid);//, 300, 250);
		stage.setTitle("Test FXConsole");
		stage.setScene(scene);
		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
	

}
