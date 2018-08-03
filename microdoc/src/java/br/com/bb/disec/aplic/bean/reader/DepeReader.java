package br.com.bb.disec.aplic.bean.reader;

import br.com.bb.disec.aplic.bean.Depe;
import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Classe para criar beans <code>Depe</code> 
 * a partir de um <code>ResultSet</code>.
 * @author Juno Roesler - F6036477
 */
public class DepeReader extends Reader<Depe> {
	
	
	public static enum Fields {
		CD_PRF, CD_UOR, NM_UOR_CMPL;
	}

	
	public DepeReader(ResultSet rs) {
		super(rs);
	}
	
	
	@Override
	public Depe readBean() throws SQLException {
		Depe depe = new Depe();
		if(contains(Fields.CD_PRF)) {
			depe = depe.novoPrefixo(
					rset.getInt(Fields.CD_PRF.name())
			);
		}
		if(contains(Fields.CD_UOR)) {
			depe = depe.novoUOR(
					rset.getInt(Fields.CD_UOR.name())
			);
		}
		if(contains(Fields.NM_UOR_CMPL)) {
			depe = depe.novoNome(
					rset.getString(Fields.NM_UOR_CMPL.name())
			);
		}
		return depe;
	}
	
}
