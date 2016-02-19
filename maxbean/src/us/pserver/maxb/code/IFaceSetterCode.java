/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.code;

import us.pserver.maxb.code.spec.IMethodCode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.util.CamelCaseName;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class IFaceSetterCode 
		extends AbstractColumnCode 
		implements IMethodCode {

	private final String returnClass;
	
	
	public IFaceSetterCode(IColumn<?> col, Ident ident, String returnClass) {
		super(col, ident);
		this.returnClass = returnClass;
	}
	
	
	public String getReturnClassName() {
		return returnClass;
	}
	
	
	@Override
	public String methodCode(boolean override) {
		String var = CamelCaseName.of(
				column.getName()
		).toCamelCase(false);
		SetterMethodComment cmt = new SetterMethodComment(column, ident);
		StringBuilder sb = new StringBuilder()
				.append(cmt.commentCode()).append("\n")
				.append(ident.getIdentation())
				.append((override ? "@Override\n" + ident.getIdentation() : ""))
				.append("public ");
		if(returnClass != null) {
			sb.append(returnClass);
		} else {
			sb.append("void");
		}
		return sb.append(" set")
				.append(CamelCaseName.of(column.getName()))
				.append("( ")
				.append(column.getType().getJavaClass().getName())
				.append(" ")
				.append(var)
				.append(" );").toString();
	}


	@Override
	public String getCode() {
		return methodCode(false);
	}
	
}
