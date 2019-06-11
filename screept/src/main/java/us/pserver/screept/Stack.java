/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.List;
import java.util.stream.Stream;


/**
 *
 * @author juno
 */
public interface Stack {
  
  public Stack put(Statement s);
  
  public Stream<Statement> statements();
  
  public Statement next();
  
  public Statement prev();
  
  public Statement current();
  
  public List<Statement> getArgs(Statement st);
  
  public void clear();
  
}
