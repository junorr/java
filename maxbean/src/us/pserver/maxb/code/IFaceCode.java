/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.ICode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class IFaceCode extends AbstractTableCode implements ICode {
	
	private final String pack;
	
	
	public IFaceCode(ITable table, Ident ident, String packageName) {
		super(table, ident);
		if(packageName == null || packageName.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Package name must be not null"
			);
		}
		this.pack = packageName;
	}
	
	
	public String getPackageName() {
		return pack;
	}
	
	
	public String interfaceCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("package ")
				.append(pack)
				.append(".iface;\n\n");
		ClassCommentCode cm = new ClassCommentCode(table, ident);
		sb.append(cm.getCode()).append("\n")
				.append("public interface I")
				.append(CamelCaseName.of(table.getName()))
				.append(" {\n\n");
		for(IColumn<?> c : table.getColumns()) {
      sb.append(ident)
          .append("/**\n")
          .append(ident)
          .append(" * Table column name [")
          .append(c.getName())
          .append(": ")
          .append(c.getType().getName())
          .append("].\n")
          .append(ident)
          .append(" */\n")
          .append(ident)
          .append("public static final String COLUMN_")
          .append(c.getName().toUpperCase())
          .append(" = \"").append(c.getName())
          .append("\";\n\n");
    }
    sb.append("\n");
		for(IColumn<?> c : table.getColumns()) {
			IFaceGetterCode get = new IFaceGetterCode(c, ident);
			sb.append(get.getCode()).append("\n\n");
			IFaceSetterCode set = new IFaceSetterCode(
					c, ident, "I"+ CamelCaseName.of(table.getName()).toString()
			);
			sb.append(set.getCode()).append("\n\n\n");
		}
		return sb.append("}").toString();
	}


	@Override
	public String getCode() {
		return interfaceCode();
	}
	
}
