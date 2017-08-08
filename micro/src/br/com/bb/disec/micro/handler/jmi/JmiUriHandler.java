/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package br.com.bb.disec.micro.handler.jmi;

import br.com.bb.disec.micro.handler.jmi.get.JmiLsClassHandler;
import br.com.bb.disec.micro.handler.jmi.get.JmiLsJarsHandler;
import br.com.bb.disec.micro.handler.jmi.get.JmiCreateHandler;
import br.com.bb.disec.micro.handler.jmi.get.JmiLsCacheHandler;
import br.com.bb.disec.micro.handler.jmi.get.JmiSetHandler;
import br.com.bb.disec.micro.handler.jmi.get.JmiMethodHandler;
import br.com.bb.disec.micro.handler.jmi.get.JmiLsMethHandler;
import br.com.bb.disec.micro.handler.jmi.get.JmiGetHandler;
import br.com.bb.disec.micro.box.OpResult;
import br.com.bb.disec.micro.handler.JsonHandler;
import br.com.bb.disec.micro.util.URIParam;
import io.undertow.server.HttpServerExchange;

/**
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class JmiUriHandler extends JsonSendHandler {
  
  public static final String CREATE = "create";
  
  public static final String GET = "get";
  
  public static final String SET = "set";
  
  public static final String METHOD = "method";
  
  public static final String LS_METH = "lsmeth";
  
  public static final String LS_JARS = "lsjars";
  
  public static final String LS_CLASS = "lsclass";
  
  public static final String LS_CACHE = "lscache";
  
  
  /**
   * 
   * @param hse Exchanger de resquisição e resposta do servidor
   * @throws Exception 
   */
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam pars = new URIParam(hse.getRequestURI());
    OpResult response = null;
    switch(pars.getParam(0)) {
      case CREATE:
        new JmiCreateHandler().handleRequest(hse);
        return;
      case GET:
        new JmiGetHandler().handleRequest(hse);
        return;
      case METHOD:
        new JmiMethodHandler().handleRequest(hse);
        return;
      case SET:
        new JmiSetHandler().handleRequest(hse);
        return;
      case LS_CACHE:
        new JmiLsCacheHandler().handleRequest(hse);
        return;
      case LS_CLASS:
        new JmiLsClassHandler().handleRequest(hse);
        return;
      case LS_JARS:
        new JmiLsJarsHandler().handleRequest(hse);
        return;
      case LS_METH:
        new JmiLsMethHandler().handleRequest(hse);
        return;
      default:
        send(hse, OpResult.of(
            new UnsupportedOperationException(pars.getParam(0)))
        );
    }
  }
  
}
