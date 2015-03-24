package br.com.bb.dinop.arqRedeDinop.aspectos;

import java.util.ResourceBundle;

/**
 * O aspceto LogSql. aspecto que grava todos os SQL executados nas classes do
 * pacote br.com.bb.dinop.arqRedeDinop.persistencia em System.err.
 */
public aspect LogSql {
	/** indicador de atividade do aspecto. */
	protected boolean aspectoAtivo = false;

	/**
	 * Log sql. inicializa aspecto checando ResourceBundle
	 * br/com/bb/logistica/arqRedeDinop/aspectos/aspectos.properties para chave
	 * LogSql. o valor <code>true</code> ativa o aspecto. a alteração do valor
	 * da chave exige o reinício do container.
	 */
	public LogSql() {
		try {
			aspectoAtivo = ResourceBundle.getBundle(
					"br.com.bb.dinop.arqRedeDinop.aspectos.aspectos")
					.getString("LogSql").equalsIgnoreCase("true");
		} catch (Exception e) {
			aspectoAtivo = false;
		}
	}

	/**
	 * Execute query. pointcut para todas as chamadas
	 * java.sql.Statement.executeQuery(String) ou
	 * java.sql.Connection.prepareStatement(String) dentro das classes do pacote
	 * br.com.bb.dinop.arqRedeDinop.persistencia. o parametro String é
	 * passado em args(sql).
	 *
	 * @param sql
	 *            o sql passado aos métodos inteceptados.
	 */
	pointcut executeQuery(String sql):(call (* java.sql.Statement.executeQuery(String))
		|| call (* java.sql.Connection.prepareStatement(String)))
		&& within(br.com.bb.dinop.arqRedeDinop.persistencia.*)
		&& args(sql);

	/**
	 * Before. adendo executado antes das chamadas de método especificadas em
	 * executeQuery(String). escre em System.err o sql passado aos métodos
	 * interceptados.
	 *
	 * @param sql
	 *            o sql passado aos métodos interceptados.
	 */
	before(String sql):executeQuery(sql){
		if (aspectoAtivo) {
			System.err.println("\nAjLog: " + (new java.util.Date()) + "\n"
					+ thisJoinPoint.getSourceLocation() + "\n"
					+ thisJoinPoint.toLongString() + "\n" + sql);
		}
	}
}
