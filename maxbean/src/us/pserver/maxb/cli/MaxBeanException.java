/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.cli;


/**
 *
 * @author juno
 */
public class MaxBeanException extends Exception {

	public MaxBeanException(String message) {
		super(message);
	}


	public MaxBeanException(String message, Throwable cause) {
		super(message, cause);
	}


	public MaxBeanException(Throwable cause) {
		super(cause);
	}
	
}
