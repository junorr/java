/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.Map;
import java.util.stream.Stream;


/**
 *
 * @author juno
 */
public interface Stack {
  
  public AbstractStatement peek();
  
  public AbstractStatement pop();
  
  public Stack put(AbstractStatement stm);
  
  public Stream<AbstractStatement> statements();
  
  public void clear();
  
  public Map<String,Statement> memory();
  
}
