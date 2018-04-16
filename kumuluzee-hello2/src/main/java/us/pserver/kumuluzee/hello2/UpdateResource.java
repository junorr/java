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
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import static us.pserver.kumuluzee.hello2.HeadResource.HEADER_IF_MATCH;
import static us.pserver.kumuluzee.hello2.HeadResource.HEADER_IF_NONE_MATCH;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/12/2017
 */
@javax.ws.rs.Path("/update/{res:.*}")
public class UpdateResource extends HeadResource {

  public UpdateResource() {
    super();
  }
  
  @HEAD
  public Response head(@Context UriInfo uri,
      @HeaderParam(HEADER_IF_MATCH) String ifMatch,
      @HeaderParam(HEADER_IF_NONE_MATCH) String ifNoneMatch) throws IOException {
    return Response.status(this.checkResource(uri, ifMatch, ifNoneMatch)).build();
  }
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response upload(
      @Context UriInfo uri,
      @HeaderParam(HEADER_IF_MATCH) String ifMatch,
      @HeaderParam(HEADER_IF_NONE_MATCH) String ifNoneMatch,
      @FormDataParam("file") InputStream uploadedInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail) {
    
    return Response.ok().build();
  }
  
}
