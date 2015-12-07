/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.log;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author juno
 */
public class TestLog {
	
	
	public static void main(String[] args) {
		Logging.configureLogger();
		LogHelper log = Logging.getConfigured(TestLog.class);
		Logger lg = log.logger();
		lg.log(Level.FINE, "This is a test log");
		lg.log(Level.INFO, "This is a test log");
    lg.log(Level.WARNING, "This is a test log");
    lg.log(Level.SEVERE, "This is a test log");
	}
	
}
