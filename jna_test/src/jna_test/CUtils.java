/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jna_test;

import com.sun.jna.Library;
import com.sun.jna.ptr.IntByReference;


/**
 *
 * @author juno
 */
public interface CUtils extends Library {
  
  void println(String str);
  
  int pause();
  
  int pauseln(String str);
  
  int inc(int i);
  
  void ptr_inc(IntByReference i);
  
}
