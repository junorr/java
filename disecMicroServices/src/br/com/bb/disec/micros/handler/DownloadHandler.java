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

import br.com.bb.disec.micro.util.URIParam;
import br.com.bb.disec.micros.conf.FileDownloadConfig;
import static br.com.bb.disec.micros.util.JsonConstants.FILE;
import static br.com.bb.disec.micros.util.JsonConstants.GROUP;
import static br.com.bb.disec.micros.util.JsonConstants.NAME;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 05/10/2016
 */
public class DownloadHandler extends HashDownloadHandler {

  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    try {
      super.handleRequest(hse);
      if(Methods.GET.equals(hse.getRequestMethod())) {
        this.get(hse);
      }
      else if(Methods.PUT.equals(hse.getRequestMethod())) {
        this.put(hse);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
      hse.setStatusCode(400).setReasonPhrase(
          "Bad Request: "+ e.getMessage()
      );
    }
    hse.endExchange();
  }
  
  
  private void get(HttpServerExchange hse) throws Exception {
    FileDownloadConfig conf = FileDownloadConfig
        .builder().load(
            json.get(GROUP).getAsString(), 
            json.get(NAME).getAsString()
        ).build();
    try {
      new FileStreamHandler(
          conf.getPath(), conf.getName()
      ).handleRequest(hse);
    }
    catch(Exception e) {
      e.printStackTrace();
      hse.setStatusCode(500);
      hse.endExchange();
    }
  }

  
  private void put(HttpServerExchange hse) throws Exception {
    if(!json.has(FILE)) {
      throw new IllegalArgumentException("Missing File Path");
    }
    URIParam pars = new URIParam(hse.getRequestURI());
    Path path = Paths.get(json.get(FILE).getAsString());
    String name = pars.length() > 1 
        ? pars.getParam(1) 
        : path.getFileName().toString();
    FileDownloadConfig.Builder bld = FileDownloadConfig.builder()
        .setPath(path)
        .setGroup(pars.getParam(0))
        .setName(name);
    bld.build().store();
  }

}
