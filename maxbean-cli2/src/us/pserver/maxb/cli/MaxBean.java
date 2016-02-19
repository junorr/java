/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.cli;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import us.pserver.maxb.SchemaInspector;
import us.pserver.maxb.config.SchemaConfig;
import us.pserver.maxb.sql.Schema;
import us.pserver.maxb.sql.spec.ISchema;
import us.pserver.maxb.sql.spec.ITable;



/**
 *
 * @author juno
 */
public class MaxBean {
	
	private final MBOptions opt;
	
	
	public MaxBean(MBOptions opt) {
		if(opt == null) {
			throw new IllegalArgumentException(
					"MBOptions must be not null"
			);
		}
		this.opt = opt;
	}
	
	
	public ISchema createSchema() throws MaxBeanException {
		Properties insprop = opt.getProperties();
		ISchema schema = null;
		if(insprop != null) {
			schema = SchemaConfig.from(insprop).read();
		} else {
			schema = new Schema(opt.getSchema());
			Connection con = opt.createConnection();
			System.out.println("* Inspecting database...");
			SchemaInspector insp = new SchemaInspector(con, schema.getName());
			schema = insp.inspect();
			System.out.println("  Done!");
		}
		return schema;
	}
	
	
	public void execute() throws MaxBeanException {
		if(opt.isVersion()) {
			opt.printVersion();
			return;
		}
		try {
			ISchema schema = createSchema();
			
		}//try
		catch(Exception e) {
			throw new MaxBeanException(
					"Error executing MaxBean", e
			);
		}
	}
	
}
