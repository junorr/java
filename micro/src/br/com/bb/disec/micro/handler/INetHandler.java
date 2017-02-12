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

import com.google.gson.GsonBuilder;
import io.undertow.server.HttpServerExchange;
import java.util.List;
import us.pserver.ip.INet;
import us.pserver.ip.INetDiscovery;

/**
 * Um handler que pode ser usado para se obter a hora local do servidor.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class INetHandler implements JsonHandler {
  
  /**
   * Envia uma resposta com um JSON da hora local do servidor.
   * @param hse Exchanger de resquisição e resposta do servidor
   * @throws Exception 
   */
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    List<INet> nets = INetDiscovery.create().getInterfaces();
    hse.getResponseSender().send(
        new GsonBuilder().setPrettyPrinting().create().toJson(nets)
    );
    hse.endExchange();
  }
  
}