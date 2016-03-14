/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.zeromap.mapper;

import java.util.Date;
import us.pserver.date.SimpleDate;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.impl.ONode;



/**
 *
 * @author juno
 */
public class DateMapper implements Mapper<Date> {

	@Override
	public Node map(Date t) {
		Node n = null;
		if(t != null) {
			n = new ONode(SimpleDate.from(t)
					.format(SimpleDate.YYYYMMDD_HHMMSS_DASH)
			);
		}
		return n;
	}


	@Override
	public Date unmap(Node n) {
		Date d = null;
		if(n != null) {
			d = SimpleDate.parseDate(n.value());
		}
		return d;
	}
	
}
