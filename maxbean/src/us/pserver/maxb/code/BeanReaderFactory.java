/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import java.util.Iterator;
import us.pserver.maxb.code.spec.ICode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class BeanReaderFactory extends AbstractTableCode implements ICode {

	public final String pack;
	
	
	public BeanReaderFactory(ITable col, Ident ident, String packageName) {
		super(col, ident);
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
	

	@Override
	public String getCode() {
		CamelCaseName tcc = CamelCaseName.of(table.getName());
		BeanReaderComment cmt = new BeanReaderComment(table, ident);
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(pack)
				.append(".reader;\n\n")
				.append("import java.sql.*;\n")
				.append("import ")
				.append(pack).append(".*")
				.append(";\n")
				.append("import ")
				.append(pack).append(".iface.*")
				.append(";\n\n")
				.append(cmt.commentCode()).append("\n")
				.append("public class ")
				.append(tcc)
				.append("Reader {\n\n")
				.append(ident)
				.append("private final ResultSet rset;\n\n")
				.append(ident).append("/**\n")
				.append(ident).append(" * ")
				.append("Default constructor receiving the ResultSet.\n")
				.append(ident).append(" * ")
				.append("@param rs ResultSet containing the ")
				.append(tcc).append(" information.\n")
				.append(ident).append(" */\n")
				.append(ident)
				.append("public ")
				.append(tcc)
				.append("Reader( ResultSet rs ) {\n")
				.append(ident.increment())
				.append("if(rs == null) {\n")
				.append(ident.increment())
				.append("throw new IllegalArgumentException(\"ResultSet must be not null\");\n")
				.append(ident.decrement())
				.append("}\n")
				.append(ident)
				.append("this.rset = rs;\n")
				.append(ident.decrement())
				.append("}\n\n")
				.append(ident).append("/**\n")
				.append(ident).append(" * ")
				.append("Get the ResultSet of this BeanReader.\n")
				.append(ident).append(" * ")
				.append("@return java.sql.ResultSet.\n")
				.append(ident).append(" */\n")
				.append(ident)
				.append("public ResultSet getResultSet() {\n")
				.append(ident.increment())
				.append("return this.rset;\n")
				.append(ident.decrement())
				.append("}\n\n")
				.append(ident).append("/**\n")
				.append(ident).append(" * ")
				.append("Identifies if the ResultSet contains a column with the given name.\n")
				.append(ident).append(" * ")
				.append("@param col The column name to be verified.\n")
				.append(ident).append(" * ")
				.append("@return [true] if the ResultSet contains the column, [false] otherwise.\n")
				.append(ident).append(" */\n")
				.append(ident)
				.append("private boolean contains( String col ) {\n")
				.append(ident.increment())
				.append("try {\n")
				.append(ident.increment())
				.append("return rset.findColumn( col ) > 0;\n")
				.append(ident.decrement())
				.append("} catch( SQLException e ) {\n")
				.append(ident.increment())
				.append("return false;\n")
				.append(ident.decrement())
				.append("}\n")
				.append(ident.decrement())
				.append("}\n\n")
				.append(ident).append("/**\n")
				.append(ident).append(" * ")
				.append("Create a ").append(tcc).append(" bean with the ResultSet information.\n")
				.append(ident).append(" * ")
				.append("@return The created ").append(tcc).append(" bean.\n")
				.append(ident).append(" */\n")
				.append(ident)
				.append("public ")
				.append("I").append(tcc)
				.append(" readBean() throws SQLException {\n")
				.append(ident.increment())
				.append("I").append(tcc).append(" bean = new ")
				.append(tcc).append("();\n");
		Iterator<IColumn<?>> it = table.getColumns().iterator();
		while(it.hasNext()) {
			IColumn<?> c = it.next();
			String cname = c.getName();
			CamelCaseName cc = CamelCaseName.of(cname);
			sb.append(ident)
					.append("if(this.contains( \"")
					.append(cname).append("\" )) {\n")
					.append(ident.increment())
					.append("bean.set").append(cc)
					.append("( rset.get").append(getMethod(c))
					.append("(\"").append(cname).append("\") );\n")
					.append(ident.decrement())
					.append("}\n");
		}
		return sb.append(ident)
				.append("return bean;\n")
				.append(ident.decrement())
				.append("}\n\n}")
				.toString();
	}
	
	
	private String getMethod(IColumn<?> c) {
		if(c == null) return null;
		String method = c.getType()
				.getJavaClass().getSimpleName();
		if(method.equals("Integer"))
			method = "Int";
		return method;
	}
		
}
