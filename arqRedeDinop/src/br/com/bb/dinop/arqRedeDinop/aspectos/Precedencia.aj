package br.com.bb.dinop.arqRedeDinop.aspectos;

/**
 * The Class Precedencia.
 */

public aspect Precedencia {

	/**
	 * Declare precedence.
	 */
	declare precedence:
		br.com.bb.dinop.arqRedeDinop.aspectos.ControleExcecao,
		br.com.bb.dinop.arqRedeDinop.aspectos.ChecaAcesso,
		br.com.bb.dinop.arqRedeDinop.aspectos.ConexaoBD,
		br.com.bb.dinop.arqRedeDinop.aspectos.ChecaAtivo,
		br.com.bb.dinop.arqRedeDinop.aspectos.LogSql;
}