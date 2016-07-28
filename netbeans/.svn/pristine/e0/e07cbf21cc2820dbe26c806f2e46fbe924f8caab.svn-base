package br.com.bb.sso.session;

import java.nio.charset.Charset;
import java.util.Objects;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.Md5Crypt;



/**
 * Hash MD5 a partir de duas Strings.
 * @author Juno Roesler - F6036477
 */
public class Hash {
	
	private final String hash;
	
	
	/**
	 * Construtor padrão, recebe o ID de sessão do tomcat e o token SSO.
	 * @param id ID de sessão do tomcat
	 * @param token Token e autenticação SSO
	 */
	public Hash(String id, String token) {
		if(id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"ID Inválido: "+ id
			);
		}
		if(token == null || token.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Token Inválido: "+ token
			);
		}
		this.hash = createHash(id, token);
	}
	
	
	/**
	 * Construtor que recebe o próprio hash gerado.
	 * @param hash o próprio hash gerado.
	 */
	public Hash(String hash) {
		if(hash == null || hash.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Hash Inválido: "+ hash
			);
		}
		this.hash = hash;
	}
	
	
	/**
	 * Método estático para retornar um novo objeto Hash 
	 * a partir dos argumentos informados.
	 * @param id ID de sessão do tomcat.
	 * @param token Token SSO.
	 * @return Novo objeto Hash.
	 */
	public static Hash of(String id, String token) {
		return new Hash(id, token);
	}
	
	
	/**
	 * Método estático para retornar um novo objeto Hash 
	 * a partir dos argumentos informados.
	 * @param hash Hash MD5 gerado em outro momento.
	 * @return Novo objeto Hash.
	 */
	public static Hash of(String hash) {
		return new Hash(hash);
	}
	
	
	/**
	 * Retorna o hash MD5 gerado.
	 * @return hash MD5 gerado.
	 */
	public String hash() {
		return hash;
	}
	
	
	/**
	 * Cria o hash MD5 a partir do ID de sessão do tomcat 
	 * e do token de autenticação do servidor SSO.
	 * @param id ID de sessão do tomcat.
	 * @param token token de autenticação do servidor SSO.
	 * @return Hash MD5
	 */
	private String createHash(String id, String token) {
		Charset utf = Charset.forName("UTF-8");
		byte[] bid = id.getBytes(utf);
		byte[] btk = token.getBytes(utf);
		byte[] bs = new byte[bid.length + btk.length];
		System.arraycopy(bid, 0, bs, 0, bid.length);
		System.arraycopy(btk, 0, bs, bid.length, btk.length);
		String hash = Md5Crypt.md5Crypt(bs, "$1$"+ id);
		hash = Hex.encodeHexString(hash.getBytes(utf)).toUpperCase();
		return hash;
	}


	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + Objects.hashCode(this.hash);
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
		final Hash other = (Hash) obj;
		if(!Objects.equals(this.hash, other.hash)) {
			return false;
		}
		return true;
	}


	@Override
	public String toString() {
		return "Hash(" + hash + ")";
	}
	
	
	public static void main(String[] args) {
		System.out.println(Hash.of(
				"99C1F4F038D9C610720841077D9D2982", 
				"AQIC5wM2LY4SfcxyabqX4Ffby4vCyeb7i9j5EMBvA7jYZQM."
						+ "*AAJTSQACMDMAAlNLABM4OTM2MzU1MDM2MTA5MzgzMDY2AAJTMQACMDc.*")
		);
		System.out.println(Hash.of(
				"99C1F4F038D9C610720841077D9D2982", 
				"AQIC5wM2LY4SfcxyabqX4Ffby4vCyeb7i9j5EMBvA7jYZQM."
						+ "*AAJTSQACMDMAAlNLABM4OTM2MzU1MDM2MTA5MzgzMDY2AAJTMQACMDc.0")
		);
	}
	
}
