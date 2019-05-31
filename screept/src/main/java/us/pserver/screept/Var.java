/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;


/**
 *
 * @author juno
 */
public interface Var extends Operation, Comparable<Var> {
  
  public String getName();
  
  public Value getValue();
  
}
