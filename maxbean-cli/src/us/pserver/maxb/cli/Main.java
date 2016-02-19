/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.cli;

import java.sql.SQLException;
import java.util.Arrays;
import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;



/**
 *
 * @author juno
 */
public class Main {
	
	public static void printVersion() {
		System.out.println("|             MaxBean V1             |");
		System.out.println("|  Copyright (c) 2016 Juno Roesler   |");
		System.out.println("|      <juno.roesler@bb.com.br>      |");
		System.out.println("|   Distributed under GNU/LGPL V3    |");
		System.out.println("+------------------------------------+");
		System.out.println(" Notice:");
		System.out.println("  MaxBean uses JewelCLi 0.8.7 for arguments parsing (Apache License V2)");
		System.out.println(" Links:");
		//System.out.println("  MaxBean [http://github.com/junorr/maxbean");
		System.out.println("  GNU/LGPL V3       [http://www.gnu.org/licenses/lgpl-3.0.en.html]");
		System.out.println("  JewelCli          [http://jewelcli.lexicalscope.com]");
		System.out.println("  Apache License V2 [http://www.apache.org/licenses/LICENSE-2.0]");
		System.out.println();
	}
	
	public static void main(String[] args) {
		String header = 
				"+------------------------------------+\n" +
				"|  MaxBean - Database Beans Factory  |\n" +
				"+------------------------------------+";
		System.out.println(header);
		MaxBean maxb = null;
		try {
			MBOptions opt = CliFactory.parseArguments(MBOptions.class, args);
			if(opt.isVersion()) {
				printVersion();
				return;
			}
			maxb = new MaxBean(opt);
			maxb.execute();
		} 
		catch(ArgumentValidationException e) {
			String msg = e.getMessage();
			if(!msg.startsWith("Usage:") && !msg.startsWith("The options")) {
				System.out.println("# Error parsing arguments:");
			}
			System.out.println(msg);
		}
		catch(MaxBeanException e) {
			System.out.println("# "+ e.getMessage());
			if(e.getCause() != null) {
				System.out.println("# "+ e.getCause().getMessage());
			}
			System.out.println();
			e.printStackTrace();
		}
		finally {
			if(maxb != null && maxb.getConnection() != null) {
				try { maxb.getConnection().close(); }
				catch(SQLException e) {}
			}
		}
	}
	
}
