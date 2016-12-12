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

package br.com.bb.disec.micros.handler;

import br.com.bb.disec.micros.handler.response.CachedResponse;
import br.com.bb.disec.micros.handler.response.DirectResponse;
import br.com.bb.disec.micros.coder.EncodingFormat;
import static br.com.bb.disec.micros.util.JsonConstants.CACHETTL;
import static br.com.bb.disec.micros.util.JsonConstants.FORMAT;
import static br.com.bb.disec.micros.util.JsonConstants.NAME;
import com.google.gson.JsonObject;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/09/2016
 */
public class SqlExportHandler extends HashDownloadHandler {
  
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    try {
      super.handleRequest(hse);
      if(Methods.GET.equals(hse.getRequestMethod())) {
        this.execute(hse, this.getJson());
      }
      else if(Methods.POST.equals(hse.getRequestMethod())) {
        hse.getResponseSender().send(this.getHash());
      }
    } 
    catch(Exception e) {
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request: "+ e.getMessage()
      );
    }
    hse.endExchange();
  }
  
  
  
  private void execute(HttpServerExchange hse, JsonObject json) throws Exception {
    this.setContentDisposition(hse, json);
    if(json.has(CACHETTL)) {
      new CachedResponse(json).handleRequest(hse);
    } else {
      new DirectResponse(json).handleRequest(hse);
    }
  }
  
  
  private EncodingFormat getEncodingFormat(JsonObject json) {
    return (json.has(FORMAT) 
        ? EncodingFormat.from(
            json.get(FORMAT).getAsString()) 
        : EncodingFormat.JSON);
  }
  
  
  private void setContentDisposition(HttpServerExchange hse, JsonObject json) throws Exception {
    EncodingFormat fmt = this.getEncodingFormat(json);
    hse.getResponseHeaders().add(Headers.CONTENT_DISPOSITION, 
        "attachment; filename=\""
            + json.get(NAME).getAsString() + "." 
            + fmt.name().toLowerCase() + "\""
    );
  }
  
}