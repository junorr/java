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

import br.com.bb.disec.micro.util.StringPostParser;
import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.disec.micros.conf.FileDownloadConfig;
import static br.com.bb.disec.micros.util.JsonConstants.ALIAS;
import static br.com.bb.disec.micros.util.JsonConstants.FILE;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.io.IOException;
import java.nio.file.Paths;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/10/2016
 */
public class DownloadHandler implements HttpHandler {

  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    URIParam pars = new URIParam(hse.getRequestURI());
    if(pars.length() < 1) {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. Missing File Context")
          .endExchange();
    }
    else if(Methods.GET.equals(hse.getRequestMethod())) {
      this.get(hse, pars);
    }
    else if(Methods.POST.equals(hse.getRequestMethod())) {
      this.post(hse, pars);
    }
    else {
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request. Invalid Method \""
              + hse.getRequestMethod().toString()+ "\""
      );
    }
  }
  
  
  private void get(HttpServerExchange hse, URIParam pars) throws Exception {
    try {
      FileDownloadConfig conf = FileDownloadConfig.builder()
          .load(pars.getParam(0)).build();
      new FileStreamHandler(conf.getPath(), conf.getAlias())
          .handleRequest(hse);
    } 
    catch(IOException e) {
      hse.setStatusCode(404)
          .setReasonPhrase("Not Found ("+ pars.getParam(0)+ "). ["
              + e.getClass().getSimpleName()+ "]-> "+ e.getMessage())
          .endExchange();
    }
  }

  
  private void post(HttpServerExchange hse, URIParam pars) throws Exception {
    JsonObject json = new JsonParser().parse(
        new StringPostParser().parseHttp(hse)
    ).getAsJsonObject();
    try {
      if(!json.has(FILE)) {
        throw new IllegalArgumentException("Missing File Path");
      }
      FileDownloadConfig.Builder bld = FileDownloadConfig.builder()
          .setPath(Paths.get(json.get(FILE).getAsString()))
          .setContext(pars.getParam(0));
      if(json.has(ALIAS)) {
        bld.setAlias(json.get(ALIAS).getAsString());
      }
      bld.build().store();
    }
    catch(IllegalArgumentException | IOException e) {
      hse.setStatusCode(400)
          .setReasonPhrase("Bad Request. ["+ e.getClass()
              .getSimpleName()+ "]-> "+ e.getMessage())
          .endExchange();
    }
  }

}
