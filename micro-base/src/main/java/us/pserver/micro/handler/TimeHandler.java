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
import java.time.temporal.TemporalAccessor;
import us.pserver.micro.util.TemplateParam;

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
    String zone = "zone";
    TypedMap map = TemplateParam.of(zone, hse).getAsTypedMap();
    TemporalAccessor time = Instant.now();
    if(map.containsKey(zone)) {
      if(map.isZoneOffset(zone)) {
        time = Instant.now().atOffset(map.getZoneOffset(zone));
      }
      else if(map.isZoneId(zone)) {
        time = Instant.now().atZone(map.getZoneId(zone));
      }
    }
    hse.getResponseSender().send(time.toString());
    hse.endExchange();
  }
  
}
