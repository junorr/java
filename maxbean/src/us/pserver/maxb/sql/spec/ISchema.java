/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.sql.spec;

import java.util.List;



/**
 *
 * @author juno
 */
public interface ISchema extends IList<ITable>, Named {
	
	public List<ITable> getTables();
	
	public ISchema addTable(ITable t);
	
	public ISchema addTables(ITable ... tbs);
	
}
