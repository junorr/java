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
public interface ITable extends IList<IColumn<?>>, Named {
	
	public List<IColumn<?>> getColumns();
	
	public ITable addColumn(IColumn<?> col);
	
	public ITable addColumns(IColumn<?> ... cols);
	
	public String getSchema();
	
}
