/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gson.test;

import com.google.gson.Gson;


/**
 *
 * @author Juno
 */
public class GsonTest {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Example ex = new BeanBuilder()
        .put("id", "abc123")
        .put("execTime", System.currentTimeMillis())
        .put("run", new BeanBuilder()
            .put("id", "def456")
            .put("execTime", System.currentTimeMillis()+1)
            .build(Example.class)
        ).build(Example.class);
    System.out.println(ex);
    //ex = new BeanBuilder()
        //.put("id", "12345")
        //.put("execTime", System.currentTimeMillis() + 100)
        //.build(Example.class);
    System.out.println("* ex.toJson: "+ new Gson().toJson(ex));
    System.out.println("* ex.fromJson: "+ new Gson().fromJson(new Gson().toJson(ex), Example.class));
  }
  
}
