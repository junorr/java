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

package us.pserver.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/06/2014
 */
public class MultipartEntity implements HttpEntity {
  
  public static final String 
      HYFENS = "--",
      BOUNDARY = "9051914041544843365972754266",
      LF = "\n", CRLF = "\r\n";
  
  
  private List<HttpEntity> ents;
  
  
  public MultipartEntity() {
    ents = new LinkedList<>();
  }
  
  
  public List<HttpEntity> entities() {
    return ents;
  }
  
  
  public MultipartEntity addEntity(HttpEntity het) {
    if(het != null) ents.add(het);
    return this;
  }
  
  
  public boolean removeEntity(HttpEntity het) {
    if(het != null)
      return ents.remove(het);
    return false;
  }
  
  
  public int entitiesSize() {
    return ents.size();
  }


  @Override
  public boolean isRepeatable() {
    return false;
  }


  @Override
  public boolean isChunked() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public long getContentLength() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public Header getContentType() {
    return new BasicHeader(
        HttpConst.HD_CONTENT_TYPE, 
        HttpConst.VALUE_CONTENT_MULTIPART);
  }


  @Override
  public Header getContentEncoding() {
    return new BasicHeader(
        HttpConst.HD_CONTENT_ENCODING, 
        HttpConst.VALUE_ENCODING);
  }


  @Override
  public InputStream getContent() throws IOException, IllegalStateException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void writeTo(OutputStream out) throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public boolean isStreaming() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }


  @Override
  public void consumeContent() throws IOException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
