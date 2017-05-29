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

import static br.com.bb.disec.micro.handler.JsonHandler.HEADER_VALUE_JSON;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.MimeMappings;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 17/05/2017
 */
public class PathHandler implements HttpHandler {
  
  public static final Path BASE_PATH = Paths.get("/");
  
  private final ResourceHandler resource;
  
  
  public PathHandler() {
    PathResourceManager prm = new PathResourceManager(BASE_PATH, 10);
    resource = Handlers.resource(prm).setDirectoryListingEnabled(true);
  }
  
  
  @Override
  public void handleRequest(HttpServerExchange ex) throws Exception {
    String uri = ex.getRequestURI();
    Path path = getPath(uri);
    if(path == null) {
      ex.setStatusCode(400).setReasonPhrase(
          "Bad Request: Bad Path"
      );
      return;
    }
    ex.getResponseHeaders().put(
        Headers.CONTENT_TYPE, Files.probeContentType(path)
    );
    resource.handleRequest(ex);
  }
  
  
  private Path getPath(String uri) {
    int i = uri.indexOf("/", 1);
    String spath = uri.substring(i);
    if(spath == null || spath.trim().isEmpty()) {
      return null;
    }
    return Paths.get(spath);
  }

}
