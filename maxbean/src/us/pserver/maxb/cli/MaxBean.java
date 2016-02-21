/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.cli;

import us.pserver.maxb.cli.handler.Inspection;
import us.pserver.maxb.cli.handler.SourceCode;
import us.pserver.maxb.cli.parser.ColumnParser;
import us.pserver.maxb.cli.parser.SchemaParser;
import us.pserver.maxb.cli.parser.TablesParser;
import us.pserver.maxb.sql.spec.ISchema;



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
	
	
	public void execute() throws MaxBeanException {
		//-v
    if(opt.isVersion()) {
			System.out.println(MBVersion.v());
			return;
		}
    //-s, --db-*, -d, -f
		ISchema schema = SchemaParser.get(opt).parse();
    //-t
    if(opt.getTables() != null) {
      System.out.println("* Filtering tables...");
      schema = TablesParser.get(opt, schema).parse();
      System.out.println("  Done!");
    }
    //-c
    if(opt.getColumns() != null) {
      System.out.println("* Creating additional columns...");
      schema = ColumnParser.get(opt, schema).parse();
      System.out.println("  Done!");
    }
    //generate inspection properties
    if(opt.isInspect()) {
      System.out.println("* Generating inspection file...");
      Inspection.get(opt, schema).handle().save();
      System.out.println("  Done!");
    }
    //generate source code
    else {
      System.out.println("* Generating source code...");
      SourceCode.get(opt, schema).handle().save();
      System.out.println("  Done!");
    }
	}
  
}
