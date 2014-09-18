/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jna_test;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;


/**
 *
 * @author juno
 */
public class JnaTest {
  
  public static void main(String[] args) {
    CUtils utils = (CUtils) Native.loadLibrary("cutils4j", CUtils.class);
    System.out.println("* Using cutils::pauseln()");
    utils.pauseln("Execution pause by cutils4j::pauseln()");
    
    int i = 78;
    System.out.println("* Using cutils::inc("+ i+ ")");
    System.out.println("cutils::inc="+utils.inc(i));
    System.out.println("i="+i);
    
    IntByReference ri = new IntByReference(88);
    System.out.println("* ri="+ ri.getValue());
    System.out.println("* Using cutils::ptr_inc("+ ri.getValue()+ ")");
    utils.ptr_inc(ri);
    System.out.println("* ri="+ ri.getValue());
    System.out.println("* Done!");
  }
  
}
