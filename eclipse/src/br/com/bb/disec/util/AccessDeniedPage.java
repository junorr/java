package br.com.bb.disec.util;

import br.com.bb.disec.bean.iface.IDcrCtu;
import br.com.bb.disec.bean.iface.IPflAcss;
import br.com.bb.sso.bean.User;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;



/**
 * Classe para recuperar a página html localizada em
 * /resources/acessoNegado.html, além de definir 
 * os parâmetros das informações exibidas na página.
 * @author Juno Roesler - F6036477
 */
public class AccessDeniedPage extends StringResource {
	
	public static final String TAG_USERNAME = "$USERNAME";
	
	public static final String TAG_CONTEXTO = "$CONTEXTO";
	
	public static final String TAG_NMAPLIC = "$NMAPLIC";
	
	public static final String TAG_CDAPLIC = "$CDAPLIC";
	
	public static final String TAG_TIPPERFIL = "$TIPPERFIL";
	
	public static final String TAG_NMPERFIL = "$NMPERFIL";
	
	public static final String TAG_CDPERFIL = "$CDPERFIL";
	
	public static final String TAG_GRPERFIL = "$GRPERFIL";
	
	public static final String TAG_CHAVE = "$CHAVE";
	
	public static final String TAG_CDCMSS = "$CDCMSS";
	
	public static final String TAG_PRFDEPE = "$PRFDEPE";
	
	public static final String TAG_UOREQP = "$UOREQP";
	
	public static final String TAG_UORDEPE = "$UORDEPE";
	
	public static final String TAG_GRPACSS = "$GRPACSS";
	
	public static final String PAGE = "/resources/acessoNegado.html";
	
	
	private final User user;
	
	private final List<IPflAcss> perfis;
	
	private final IDcrCtu ctu;
	
	private String content;
	
	
	/**
	 * Construtor padrão que recebe o usuário cujo acesso 
	 * foi negado e o perfil de acesso responsável.
	 * @param usu Usuário cujo acesso foi negado.
	 * @param ctu Conteúdo cujo acesso foi negado.
	 * @param acss Perfil que negou o acesso.
	 */
	public AccessDeniedPage(User usu, IDcrCtu ctu, List<IPflAcss> acss) {
		super(PAGE);
		if(usu == null) {
			throw new IllegalArgumentException(
					"Usuário inválido: "+ usu
			);
		}
		if(acss == null) {
			throw new IllegalArgumentException(
					"Perfil Acesso inválido: "+ acss
			);
		}
		if(ctu == null) {
			throw new IllegalArgumentException(
					"Conteúdo Aplicação inválido: "+ ctu
			);
		}
		this.user = usu;
		this.perfis = acss;
		this.ctu = ctu;
		this.content = null;
	}
	
	
	/**
	 * Retorna o usuário cujo acesso foi negado.
	 * @return User
	 */
	public User getUsuario() {
		return this.user;
	}
	
	
	/**
	 * Retorna o perfil de acesso.
	 * @return IPflAcss
	 */
	public List<IPflAcss> getPerfilAcesso() {
		return this.perfis;
	}
	
	
	/**
	 * Retorna o conteúdo cujo acesso foi negado.
	 * @return IDcrCtu
	 */
	public IDcrCtu getDcrCtu() {
		return ctu;
	}
	
	
	/**
	 * Define o valor de um parâmetro a ser exibido na 
	 * página de acesso negado.
	 * @param tag Nome do parâmetro.
	 * @param val Valor do parâmetro.
	 * @return Esta instância modificada de AccessDeniedPage.
	 */
	public AccessDeniedPage set(String tag, Object val) {
		if(tag != null && val != null 
				&& this.content != null
				&& this.content.contains(tag)) {
			this.content = this.content.replace(tag, Objects.toString(val));
		}
		return this;
	}
	
	
	/**
	 * Escreve o conteúdo da página html na resposta ao browser.
	 * @param response Resposta HTTP.
	 * @throws IOException In caso of erro writing to HTTP response.
	 */
	public void writeResponse(HttpServletResponse response) throws IOException {
		if(response == null) return;
		this.content = this.readResource().getContent();
    String nomePfl = "";
    String tipoPfl = "";
    String codPfl = "";
    String grupoPfl = "";
    for(int i = 0; i < perfis.size(); i++) {
      IPflAcss pa = perfis.get(i);
      nomePfl += pa.getNmPflAcss();
      tipoPfl += pa.getTipoPerfil().name();
      codPfl += pa.getCdPflAcss().toString();
      grupoPfl += pa.getCdGrpAcss().toString();
      if(i < perfis.size() -1) {
        nomePfl += ", ";
        tipoPfl += ", ";
        codPfl += ", ";
        grupoPfl += ", ";
      }
    }
		this.set(TAG_CDAPLIC, ctu.getCdCtu())
				.set(TAG_CDCMSS, user.getCodigoComissao())
				.set(TAG_CDPERFIL, codPfl)
				.set(TAG_CHAVE, user.getChave())
				.set(TAG_CONTEXTO, ctu.getTxUrlCtu())
				.set(TAG_NMAPLIC, ctu.getNmCtu())
				.set(TAG_PRFDEPE, user.getPrefixoDepe())
				.set(TAG_NMPERFIL, nomePfl)
				.set(TAG_TIPPERFIL, tipoPfl)
				.set(TAG_GRPERFIL, grupoPfl)
				.set(TAG_UORDEPE, user.getUorDepe())
				.set(TAG_UOREQP, user.getUorEquipe())
				.set(TAG_USERNAME, user.getNome());
		response.getWriter().println(this.content);
	}
	
}
