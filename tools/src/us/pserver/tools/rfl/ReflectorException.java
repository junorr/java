/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.tools.rfl;


/**
 *
 * @author juno
 */
public class ReflectorException extends RuntimeException {

	public ReflectorException() {}

	public ReflectorException(String message) {
		super(message);
	}

	public ReflectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReflectorException(Throwable cause) {
		super(cause);
	}
	
}
