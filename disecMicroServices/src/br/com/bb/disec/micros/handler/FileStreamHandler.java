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

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/09/2016
 */
public class FileStreamHandler implements HttpHandler {

  private final Path path;
  
  private final String alias;
  
  
  public FileStreamHandler(Path path, String alias) {
    if(path == null) {
      throw new IllegalArgumentException("Bad Null Path");
    }
    if(!Files.exists(path)) {
      throw new IllegalArgumentException("Path Does Not Exists");
    }
    this.path = path;
    this.alias = alias;
  }
  
  
  public FileStreamHandler(Path path) {
    this(path, null);
  }
  
  
  public Path getPath() {
    return path;
  }
  
  
  public String getAlias() {
    return alias;
  }
  
  
  private void setHeaders(HttpServerExchange hse) throws IOException {
      hse.getResponseHeaders().add(
          Headers.CONTENT_TYPE, 
          "application/octet-stream"
      );
    hse.getResponseHeaders().add(
        Headers.CONTENT_DISPOSITION, 
        "attachment; filename=\"" + (alias != null 
            ? alias : path.getFileName().toString()) + "\""
    );
    hse.setResponseContentLength(Files.size(path));
  }


  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    this.setHeaders(hse);
    hse.startBlocking();
    Files.copy(path, hse.getOutputStream());
    hse.endExchange();
  }
  
}
