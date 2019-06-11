/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.screept;

import java.util.Optional;


/**
 *
 * @author juno
 */
public interface Statement<T> extends Priority {
  
  public Optional<T> resolve(Memory m, Stack s);
  
}
