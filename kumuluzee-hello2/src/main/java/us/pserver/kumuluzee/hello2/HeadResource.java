
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/12/2017
 */
public class HeadResource {
 
  public static final java.nio.file.Path DEV_WEBAPP_PATH = Paths.get("./src/main/webapp");
 
  public static final java.nio.file.Path WEBAPP_PATH = Paths.get("./webapp");
 
  public static final String DEFAULT_RESOURCE = "index.html";
 
  public static final String HEADER_CONTENT_LENGTH = "Content-Length";
 
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
 
  public static final String HEADER_IF_MATCH = "If-Match";
 
  public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
 
  public static final String HEADER_ETAG = "ETag";
 
  public static final String SHA1_HASH = "SHA1";
 
  public static final String DQUOTE = "\"";
 
 
  protected final java.nio.file.Path basePath;
 
  protected final FileMimeType mimeType;
 
  protected final MessageDigest sha1;
 
  protected java.nio.file.Path resource;
  
  protected String etag;
 
 
  public HeadResource() {
    this.basePath = DEV_WEBAPP_PATH;
    this.mimeType = new FileMimeType();
    this.resource = basePath;
    this.etag = "";
    try {
      sha1 = MessageDigest.getInstance(SHA1_HASH);
    } catch(NoSuchAlgorithmException e) {
      throw new RuntimeException(e.toString(), e);
    }
  }
 
 
  protected Path getPath(UriInfo uri) {
    Path res = Paths.get("./");
    List<PathSegment> paths = uri.getPathSegments();
    if(isEmpty(paths)) {
      res = basePath.resolve(res.resolve(DEFAULT_RESOURCE));
    }
    else {
      for(PathSegment p : paths) {
        res = res.resolve(p.getPath());
      }
      res = basePath.resolve(res);
    }
    return res;
  }
 
 
  public Status checkResource(UriInfo uri, String ifMatch, String ifNoneMatch) throws IOException {
    resource = getPath(uri);
    if(!isParent(basePath, resource)) {
      return Status.BAD_REQUEST;
    }
    etag = createETag(resource);
    return ifMatch != null && !ifMatch.isEmpty() 
        ? getStatusIfMatch(splitEtags(ifMatch))
        : getStatusIfNoneMatch(splitEtags(ifNoneMatch));
  }
  
  
  protected boolean isParent(Path parent, Path child) {
    try {
      return child.toRealPath().startsWith(
          parent.toRealPath()
      );
    }
    catch(IOException e) {
      return false;
    }
  }
  
  
  protected Status getStatusIfMatch(List<String> etags) {
    return etags.contains(etag) 
        ? Status.OK
        : Status.PRECONDITION_FAILED;
  }
  
  
  protected Status getStatusIfNoneMatch(List<String> etags) {
    return etags.contains(etag) 
        ? Status.NOT_MODIFIED
        : Status.OK;
  }
  
  
  protected List<String> splitEtags(String str) {
    Stream<String> etags = Stream.empty();
    if(str != null && !str.isEmpty()) {
      etags = Arrays.asList(str.split(",")).stream();
    }
    return etags.map(String::trim).collect(Collectors.toList());
  }
 
 
  protected String createETag(Path path) throws IOException {
    sha1.reset();
    sha1.update(asBytes(path.toAbsolutePath().toString()));
    boolean exist = Files.exists(path);
    sha1.update(asBytes(String.valueOf(exist)));
    if(exist) {
      sha1.update(asBytes(String.valueOf(Files.size(path))));
      sha1.update(asBytes(String.valueOf(Files.getLastModifiedTime(path).toInstant())));
    }
    return DQUOTE.concat(Base64.getEncoder()
        .encodeToString(sha1.digest())
    ).concat(DQUOTE);
  }
 
 
  protected byte[] asBytes(String str) {
    return str.getBytes(StandardCharsets.UTF_8);
  }
 
  
  protected boolean isEmpty(List<PathSegment> paths) {
    return paths.isEmpty()
        || (paths.size() == 1
        && paths.get(0).getPath().isEmpty());
  }
 
}

