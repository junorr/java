package br.com.bb.disec.aplic.bean;


/**
 * Bean para informações de Dependências.
 * @author Juno Roesler - F6036477
 */
public class Depe {
	
	private final int prefixo;
	
	private final int uor;
	
	private final String nome;
	
	
	/**
	 * Construtor padrão sem argumentos, cria um objeto Depe vazio.
	 */
	public Depe() {
		this.prefixo = 0;
		this.uor = 0;
		this.nome = null;
	}
	
	
	/**
	 * Construtor que recebe todos os campos do objeto Depe.
	 * @param prefixo Prefixo da dependência.
	 * @param uor UOR da depedência.
	 * @param nome Nome da dependência.
	 */
	public Depe(int prefixo, int uor, String nome) {
		this.prefixo = prefixo;
		this.uor = uor;
		this.nome = nome;
	}
	
	
	/**
	 * Retorna o prefixo da dependência;
	 * @return int
	 */
	public int getPrefixo() {
		return prefixo;
	}
	
	
	/**
	 * Retorna o UOR da dependência;
	 * @return int
	 */
	public int getUOR() {
		return uor;
	}
	
	
	/**
	 * Retorna o nome da dependência;
	 * @return String
	 */
	public String getNome() {
		return nome;
	}
	
	
	/**
	 * Cria um novo objeto Depe com o prefixo informado.
	 * @param prefixo Novo prefixo da dependência.
	 * @return Novo Objeto Depe com o parâmetro informado.
	 */
	public Depe novoPrefixo(int prefixo) {
		return new Depe(prefixo, uor, nome);
	}
	
	
	/**
	 * Cria um novo objeto Depe com o UOR informado.
	 * @param uor Novo UOR da dependência.
	 * @return Novo Objeto Depe com o parâmetro informado.
	 */
	public Depe novoUOR(int uor) {
		return new Depe(prefixo, uor, nome);
	}
	
	
	/**
	 * Cria um novo objeto Depe com o nome informado.
	 * @param nome Novo nome da dependência.
	 * @return Novo Objeto Depe com o parâmetro informado.
	 */
	public Depe novoNome(String nome) {
		return new Depe(prefixo, uor, nome);
	}


	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + this.prefixo;
		hash = 97 * hash + this.uor;
		return hash;
	}


	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final Depe other = (Depe) obj;
		if(this.prefixo != other.prefixo) {
			return false;
		}
		if(this.uor != other.uor) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "Depe{" + "prefixo=" + prefixo + ", uor=" + uor + ", nome=" + nome + '}';
	}
	
	
	@Override
	public Depe clone() throws CloneNotSupportedException {
		super.clone();
		return new Depe(prefixo, uor, nome);
	}
	
}
