/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfx;

import java.io.InputStream;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;



/**
 *
 * @author juno
 */
public class FormGUI extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("JavaFX Form");
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		Text welcome = new Text("Welcome");
		welcome.setId("welcome");
		//welcome.setFont(Font.font(
				//"Tahoma", FontWeight.NORMAL, 20)
		//);
		HBox wbox = new HBox(10, welcome);
		wbox.setAlignment(Pos.CENTER_LEFT);
		grid.add(wbox, 0, 0, 2, 1);
		
		Label userLabel = new Label("User:");
		grid.add(userLabel, 0, 1);
		
		TextField user = new TextField();
		grid.add(user, 1, 1);
		
		Label pwdLabel = new Label("Password:");
		grid.add(pwdLabel, 0, 2);
		
		PasswordField pwd = new PasswordField();
		grid.add(pwd, 1, 2);
		
		InputStream is = this.getClass().getResourceAsStream("/jfx/img/lock_128.png");
		Image ilock = new Image(is);
		ImageView ilockView = new ImageView(ilock);
		ilockView.setFitHeight(18);
		ilockView.setPreserveRatio(true);
		
		final Text msg = new Text("Hello!");
		msg.setId("msg");
		HBox mbox = new HBox(10, msg);
		mbox.setAlignment(Pos.CENTER);
		grid.add(msg, 0, 6, 2, 1);
		
		Button login = new Button("Login", ilockView);
		login.setMinWidth(80);
		login.setOnAction(e->msg.setText("Logged as: "+ user.getText()));
		HBox bbox = new HBox(10, login);
		bbox.setAlignment(Pos.CENTER_RIGHT);
		grid.add(bbox, 0, 4, 2, 1);
		
		Scene scene = new Scene(grid, 300, 250);
		scene.getStylesheets().add(
				this.getClass().getResource(
						"/jfx/css/FormGUI.css").toExternalForm()
		);
		
		stage.setScene(scene);
		stage.show();
	}
	
	
	public static void main(String[] args) {
		FormGUI.launch(args);
	}
	
}
