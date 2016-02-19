/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.cli.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.flamingpenguin.jewel.cli.ArgumentValidationException;
import uk.co.flamingpenguin.jewel.cli.CliFactory;
import us.pserver.maxb.cli.MBOptions;



/**
 *
 * @author juno
 */
public class TestMBOptions {
	
	
	public static void main(String[] args) {
		args = new String[]{"-c", "afile", "--inspect", "-I", "3", "--help"};
		try {
			MBOptions opt = CliFactory.parseArguments(MBOptions.class, args);
			System.out.println("+------------------------------------+");
			System.out.println("|  MaxBean - Database Beans Factory  |");
			System.out.println("+------------------------------------+");
			if(opt.getVersion()) {
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
				return;
			}
			/*
			System.out.println("opt.isConfigFile(): "+ opt.isDBConfig());
			System.out.println("opt.getConfigFile(): "+ opt.getDBConfig());
			System.out.println("opt.isInspect(): "+ opt.isInspect());
			System.out.println("opt.getInspect(): "+ opt.getInspect());
			System.out.println("opt.isIdentation(): "+ opt.isIdentation());
			System.out.println("opt.getIdentation(): "+ opt.getIdentation());
			System.out.println("opt.getHelp(): "+ opt.getHelp());
			System.out.println("opt.getDBDriver(): "+ opt.getDBDriver());*/
		} catch (ArgumentValidationException ex) {
			System.out.println("+------------------------------------+");
			System.out.println("|  MaxBean - Database Beans Factory  |");
			System.out.println("+------------------------------------+");
			String msg = ex.getMessage();
			if(!msg.startsWith("The options available")) {
				System.out.println("# Error parsing args:");
			}
			System.out.println(msg);
		}
	}
	
}
