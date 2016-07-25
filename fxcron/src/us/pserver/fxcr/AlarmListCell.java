/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import us.pserver.jc.Alarm;
import us.pserver.jc.rules.RecurrentRule;
import us.pserver.jc.util.DateTime;



/**
 *
 * @author juno
 */
public class AlarmListCell extends ListCell<Alarm> {
	
	private Alarm alarm;
	
	private Label alarmIcon;
	
	private Label calendarIcon;
	
	private Label clockIcon;
	
	private Label repeatIcon;
	
	private Label date, time;
	
	private GridPane grid;
	
	
	public AlarmListCell() {
		super();
		alarmIcon = new Label("\uf0f3");
		alarmIcon.setFont(FXCron.FONT_AWESOME);
		calendarIcon = new Label("\uf073");
		calendarIcon.setFont(FXCron.FONT_AWESOME);
		clockIcon = new Label("\uf017");
		clockIcon.setFont(FXCron.FONT_AWESOME);
		repeatIcon = new Label("\uf01e");
		repeatIcon.setFont(FXCron.FONT_AWESOME);
		
		grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setHgap(5);
		grid.setVgap(5);
		
		HBox h = new HBox(alarmIcon);
		h.setAlignment(Pos.CENTER);
		grid.add(h, 0, 0, 1, 2);
		
		h = new HBox(10, calendarIcon, date);
		h.setAlignment(Pos.CENTER_LEFT);
		grid.add(h, 1, 0);
		
		h = new HBox(10, clockIcon, time);
		h.setAlignment(Pos.CENTER_LEFT);
		grid.add(h, 1, 1);
		
		h = new HBox(repeatIcon);
		h.setAlignment(Pos.CENTER);
		grid.add(h, 2, 0, 1, 2);
		this.setAlignment(Pos.TOP_LEFT);
	}
	
	
	@Override
	protected void updateItem(Alarm al, boolean empty) {
		this.getChildren().clear();
		if(al == null || empty) {
			return;
		}
		if(!al.isActive()) {
			alarmIcon.setText("\uf1f6");
			alarmIcon.setTextFill(Color.LIGHTGRAY);
		} else {
			alarmIcon.setText("\uf0f3");
			alarmIcon.setTextFill(Color.NAVY);
		}
		if(alarm.wakeRule() instanceof RecurrentRule) {
			repeatIcon.setTextFill(Color.LIGHTGRAY);
		} else {
			repeatIcon.setTextFill(Color.DARKMAGENTA);
		}
		DateTime dt = DateTime.of(alarm.at());
		date.setText(dt.format("yyyy-MM-dd"));
		time.setText(dt.format("HH:mm:ss"));
		this.getChildren().add(grid);
	}
	
}
