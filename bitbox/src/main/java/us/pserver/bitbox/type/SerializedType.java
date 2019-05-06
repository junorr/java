/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.bitbox.type;

import java.util.Optional;


/**
 *
 * @author juno
 */
@FunctionalInterface
public interface SerializedType {
  
  public Optional<Class> serialType();
  
}
