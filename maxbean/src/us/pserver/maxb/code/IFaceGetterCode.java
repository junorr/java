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
public class IFaceGetterCode 
		extends AbstractColumnCode 
		implements IMethodCode {

	
	public IFaceGetterCode(IColumn<?> col, Ident ident) {
		super(col, ident);
	}
	
	
	@Override
	public String methodCode(boolean override) {
		String var = CamelCaseName.of(
				column.getName()
		).toCamelCase(false);
		GetterMethodComment cmt = new GetterMethodComment(column, ident);
		return new StringBuilder()
				.append(cmt.commentCode()).append("\n")
				.append(ident.getIdentation())
				.append((override ? "@Override\n" + ident.getIdentation() : ""))
				.append("public ")
				.append(column.getType().getJavaClass().getName())
				.append(" get")
				.append(CamelCaseName.of(column.getName()))
				.append("();").toString();
	}


	@Override
	public String getCode() {
		return methodCode(false);
	}
	
}
