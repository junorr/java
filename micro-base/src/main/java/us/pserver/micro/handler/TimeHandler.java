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

package us.pserver.micro.handler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Retorna a hora local do servidor.
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/08/2016
 */
public class TimeHandler implements HttpHandler {
  
  /**
   * Envia a hora local do servidor.
   * @param hse Exchanger de resquisição e resposta do servidor
   * @throws Exception 
   */
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    hse.getQueryParameters().entrySet().forEach(e->System.out.println("- "+ e.getKey() + "="+ e.getValue()));
    String szone = hse.getQueryParameters().get("zoneOffset").getFirst();
    if(szone.startsWith("+") || szone.startsWith("-")) {
      ZoneOffset zof = ZoneOffset.of(szone);
      szone = Instant.now().atOffset(zof).toString();
    }
    else {
      if(hse.getQueryParameters().containsKey("zoneid")) {
        szone += "/" + hse.getQueryParameters().get("zoneid").getFirst();
      } else {
        szone = szone.replace("-", "/");
      }
      ZoneId zid = ZoneId.of(szone);
      szone = Instant.now().atZone(zid).toString();
    }
    hse.getResponseSender().send(szone);
    hse.endExchange();
  }
  
}
