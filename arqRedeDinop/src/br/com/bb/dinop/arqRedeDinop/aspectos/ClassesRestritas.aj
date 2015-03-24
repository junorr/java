package br.com.bb.dinop.arqRedeDinop.aspectos;



/**
 * The Class ClassesRestritas.
 */
public aspect ClassesRestritas {

//	/**
//	 * Declare warning: " obtenção de conexão a banco de dados deve ser feita
//	 * via aspecto".
//	 */
//	declare warning : call (* br.com.bb.pool.DBConnectionManager.getInstance())
//		&& within (br.com.bb.dinop.arqRedeDinop..*)
//		&& ! within (br.com.bb.dinop.arqRedeDinop.aspectos.*):
//			"Obtenção de conexão a banco de dados deve ser feita via aspecto";

	/**
	 * Declare warning: " esqueceu system.out.* para trás. use aspecto de log.".
	 */
	declare warning : (	get(*..PrintStream System.out)
			|| get(*..PrintStream System.err))
		&& within (br.com.bb.dinop.arqRedeDinop..*)
		&& ! within (br.com.bb.dinop.arqRedeDinop.aspectos.*):
			"Esqueceu System.out.* para trás. Use aspecto de log.";

}
