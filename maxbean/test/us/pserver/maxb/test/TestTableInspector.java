/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import us.pserver.maxb.TableInspector;
import us.pserver.maxb.code.BeanReaderFactory;
import us.pserver.maxb.code.ClassCode;
import us.pserver.maxb.code.EmptyConstructorCode;
import us.pserver.maxb.code.FullConstructorCode;
import us.pserver.maxb.code.IFaceCode;
import us.pserver.maxb.code.spec.IConstructorCode;
import us.pserver.maxb.sql.spec.IColumn;
import us.pserver.maxb.sql.spec.ITable;
import us.pserver.maxb.util.Ident;



/**
 *
 * @author juno
 */
public class TestTableInspector {
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		//Connection conn = DriverManager.getConnection("jdbc:mysql://172.29.14.107", "F6036477", "6036577");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "juno", "0988");
		TableInspector ti = new TableInspector(conn, "intranet", "dcr_ctu");
		ITable table = ti.inspect();
		System.out.println(table);
		for(IColumn c : table.getColumns()) {
			System.out.println(" - "+ c);
		}

		Ident id = new Ident(' ', 2);
		String pack = "us.pserver.ex.ifc";
		IFaceCode ic = new IFaceCode(table, id, pack);
		System.out.println("* Interface:");
		System.out.println(ic.getCode());
		System.out.println();
		
		ClassCode clc = new ClassCode(ic);
		System.out.println("* Class:");
		System.out.println(clc.getCode());
		System.out.println();
		
		BeanReaderFactory brf = new BeanReaderFactory(table, id, pack);
		System.out.println("* BeanFactory:");
		System.out.println(brf.getCode());
	}
	
}
