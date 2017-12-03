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

package us.pserver.kumuluzee.hello2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/12/2017
 */
@Path("/{res:.*}")
public class WebResource {
  
  public static final java.nio.file.Path DEV_WEBAPP_PATH = Paths.get("./src/main/webapp");
  
  public static final java.nio.file.Path WEBAPP_PATH = Paths.get("./webapp");
  
  public static final String DEFAULT_RESOURCE = "index.html";
  
  public static final String HEADER_CONTENT_LENGTH = "Content-Length";
  
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  
  
  private final java.nio.file.Path basePath;
  
  private final FileMimeType mimeType;
  
  
  public WebResource() {
    this.basePath = DEV_WEBAPP_PATH;
    this.mimeType = new FileMimeType();
  }
  
  
  @GET
  public Response getResource(@Context UriInfo uri) throws IOException {
    java.nio.file.Path resource = Paths.get("./");
    List<PathSegment> paths = uri.getPathSegments();
    if(isEmpty(paths)) {
      resource = basePath.resolve(resource.resolve(DEFAULT_RESOURCE));
    }
    else {
      for(PathSegment p : paths) {
        resource = resource.resolve(p.getPath());
      }
      resource = basePath.resolve(resource);
    }
    if(Files.exists(resource)) {
      return Response.ok(resource.toFile())
          .header(HEADER_CONTENT_LENGTH, Files.size(resource))
          .header(HEADER_CONTENT_TYPE, mimeType.getContentType(resource))
          .build();
    }
    else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  
  private boolean isEmpty(List<PathSegment> paths) {
    return paths.isEmpty() 
        || (paths.size() == 1 
        && paths.get(0).getPath().isEmpty());
  }
  
}
