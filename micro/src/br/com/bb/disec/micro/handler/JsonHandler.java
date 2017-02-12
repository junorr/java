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

package br.com.bb.disec.micro.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * Uma interface que pode ser usada para definir o header de respostas que serão
 * do tipo JSON.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/07/2016
 */
public interface JsonHandler extends HttpHandler {
  
  public static final String HEADER_VALUE_JSON = "application/json; charset=utf-8";

  /**
   * Adiciona header de JSON na resposta do Exchanger.
   * @param hse Exchanger de resquisição e resposta do servidor
   */
  public default void putJsonHeader(HttpServerExchange hse) {
    hse.getResponseHeaders().put(
        Headers.CONTENT_TYPE, HEADER_VALUE_JSON
    );
  }
  
}