/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept.statement;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


/**
 *
 * @author juno
 */
public interface Memory {
  
  public Memory put(String name, Statement stm);
  
  public boolean contains(String name);
  
  public Optional<Statement> get(String name);
  
  public Optional<Statement> remove(String name);
  
  public Optional<Statement> replace(String name, Statement stm);
  
  public Stream<Map.Entry<String,Statement>> entries();
  
  public Memory clear();
  
}
