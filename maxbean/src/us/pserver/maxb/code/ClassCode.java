/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.ICode;
import us.pserver.maxb.code.spec.IConstructorCode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class ClassCode extends AbstractTableCode implements ICode {
	
	private final String pack;
	
	private final IFaceCode iface;
	
	
	private ClassCode(ITable table, Ident ident, String packageName, IFaceCode iface) {
		super(table, ident);
		if(packageName == null || packageName.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Package name must be not null"
			);
		}
		this.pack = packageName;
		this.iface = iface;
	}
	
	
	public ClassCode(ITable table, Ident ident, String packageName) {
		this(table, ident, packageName, null);
	}
	
	
	public ClassCode(IFaceCode iface) {
		this(iface.getTable(), iface.getIdent(), iface.getPackageName(), iface);
	}
	
	
	public String getPackageName() {
		return pack;
	}
	
	
	public String classCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("package ")
				.append(pack)
				.append(";\n\n");
		sb.append("import ")
				.append(pack).append(".iface.*")
				.append(";\n\n");
		ClassCommentCode cm = new ClassCommentCode(table, ident);
		sb.append(cm.getCode()).append("\n")
				.append("public class ")
				.append(CamelCaseName.of(table.getName()));
		if(iface != null) {
			sb.append(" implements I")
					.append(CamelCaseName.of(table.getName()));
		}
		sb.append(" {\n\n");
		for(IColumn<?> c : table.getColumns()) {
			CamelCaseName ccn = CamelCaseName.of(c.getName());
			sb.append(ident).append("private ")
					.append(c.getType().getJavaClass().getName())
					.append(" ")
					.append(ccn.toCamelCase(false))
					.append(";\n");
		}
		IConstructorCode cc = new EmptyConstructorCode(table, ident);
		sb.append("\n").append(cc.getCode())
				.append("\n");
		cc = new FullConstructorCode(table, ident);
		sb.append(cc.getCode())
				.append("\n");
		for(IColumn<?> c : table.getColumns()) {
			GetterMethodCode get = new GetterMethodCode(c, ident);
			sb.append(get.methodCode(iface != null)).append("\n\n");
			SetterMethodCode set = new SetterMethodCode(
					c, ident, CamelCaseName.of(table.getName()).toString()
			);
			sb.append(set.methodCode(iface != null)).append("\n\n\n");
		}
		return sb.append("}").toString();
	}


	@Override
	public String getCode() {
		return classCode();
	}
	
}
