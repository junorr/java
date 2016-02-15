/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.fxcr;

import us.pserver.fxcr.task.TaskFactory;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import us.pserver.fxcr.task.SystemCommandTask;
import us.pserver.jc.Task;


/**
 * FXML Controller class
 *
 * @author juno
 */
public class NewTaskCommandController implements Initializable, TaskFactory {

	@FXML
	private TextField commandField;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}
  
  
  private String[] parseStringCommand() {
    String scmd = commandField.getText();
    boolean token = false;
    char TKS = ' ', TKQ = '"';
    byte itkq = 0;
    char[] chars = scmd.toCharArray();
    List<String> lcmd = new LinkedList<>();
    StringBuilder buf = new StringBuilder();
    for(char c : chars) {
      if(c == TKS && itkq == 0) token = true;
      else if(c == TKQ && ++itkq == 2) {
        itkq = 0;
        buf.append(c);
        token = true;
      }
      else {
        buf.append(c);
      }
      if(token && buf.length() > 0) {
        token = false;
        lcmd.add(buf.toString());
        buf = new StringBuilder();
      }
    }
    return lcmd.toArray(new String[lcmd.size()]);
  }


  @Override
  public Task getTask() {
    return new SystemCommandTask(parseStringCommand());
  }
	

}
