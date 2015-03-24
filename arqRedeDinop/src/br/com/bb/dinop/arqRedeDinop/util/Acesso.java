package br.com.bb.dinop.arqRedeDinop.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.com.bb.sso.api.bean.Usuario;

/**
 * The Class Acesso.
 */
public class Acesso {
    /** resposta para quando o usuário não estiver autenticado. */
    public static int NAO_AUTENTICADO = 1;
    /** resposta para quando o usuário não estiver autorizado. */
    public static int NAO_AUTORIZADO = 2;
    /** resposta para quando o usuário estiver autorizado. */
    public static int AUTORIZADO = 3;

    /**
     * retorna uma das respostas baseado no recurso e no funcionï¿½rio fornecidos.
     * 
     * @param recurso
     *            prefixo da chave para recurso no bundle br/com/bb/logistica/imoveisnaouso/acessos.properties. serão
     *            buscadas as chaves compostas da chave inicialmente fornecida concatenada a "_depes" e "_chaves". a
     *            chave <code>recurso_depes</code> deve conter a lista de prefixos de dependências autorizadas
     *            autorizados no formado <code>0000 0000 0000</code>.a chave <code>recurso_chaves</code> deve conter a
     *            lista de chaves de funcionarios autorizados no formado <code>F000000 F0000000 F0000000</code>. A
     *            inexistância de ambas as chaves pressupõe que o acesso ao recurso é aberto a todos os usuários
     *            autenticados.
     * @param req
     *            the req
     * @return respostas conforme variáveis estáticas dessa classe.
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws SAXException
     *             the SAX exception
     * @throws ParserConfigurationException
     */
    public static int canAcess(final String recurso, final HttpServletRequest req) throws SAXException, IOException,
            ParserConfigurationException {
    	HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpSession httpSession = httpServletRequest.getSession(true);
        Usuario usuario = (Usuario) httpSession.getAttribute("usuario");
        final String chave = usuario.getChave();
        final String depe = usuario.getCodigoPrefixoDependencia();
        final String funcao = usuario.getCodigoComissao();
        
        final AcessoXmlHandler axh = new AcessoXmlHandler(recurso);
        final String funcoes = axh.getFuncoesAutorizadas();
        
        if (null == chave) {
        	//System.out.println("* Nao autenticado");
            return NAO_AUTENTICADO;
        } else {
            final String depes = axh.getDepesAutorizadas();
            final String chaves = axh.getChavesAutorizadas();
            if ((null == depes) && (null == chaves) && (null == funcoes)) {
            	//System.out.println("* Autorizado por null");
                return AUTORIZADO;
            }
            if ((null != depes) && (depes.indexOf(depe) >= 0)) {
            	//System.out.println("* Autorizado por depe: "+ depe);
                return AUTORIZADO;
            }
            if ((null != chaves) && (chaves.indexOf(chave) >= 0)) {
            	//System.out.println("* Autorizado por chave: "+ chave);
                return AUTORIZADO;
            }
            if ((null != funcoes) && (funcoes.indexOf(funcao) >= 0)) {
            	//System.out.println("* Autorizado por funcao: "+ funcao);
                return AUTORIZADO;
            }
        }
        //System.out.println("* Nao Autorizado (chave="+ chave+ ", funcao="+ funcao+ ", depe="+ depe+ ")");
        return NAO_AUTORIZADO;
    }
}
