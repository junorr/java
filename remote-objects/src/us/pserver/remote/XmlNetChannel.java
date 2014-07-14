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

package us.pserver.remote;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import us.pserver.streams.StreamUtils;


/**
 * Implementa um canal de transmissão de objetos em rede
 * no formato XML, utilizando <code>XStream</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class XmlNetChannel implements Channel {
  
  private Socket sock;
  
  private XStream xst;
  
  private boolean isvalid;
  
  
  /**
   * Construtor padrão que recebe um 
   * <code>Socket</code> para comunicação em rede.
   * @param sc <code>Socket</code>.
   */
  public XmlNetChannel(Socket sc) {
    if(sc == null || sc.isClosed())
      throw new IllegalArgumentException(
          "Invalid Socket ["+ sc+ "]");
    sock = sc;
    xst = new XStream();
    isvalid = true;
  }
  
  
  /**
   * Retorna o <code>Socket</code>.
   * @return <code>Socket</code>.
   */
  public Socket getSocket() {
    return sock;
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    if(trp == null) return;
    OutputStream out = sock.getOutputStream();
    try {
      xst.toXML(trp.getWriteVersion(), out);
      if(trp.getInputStream() != null) {
        StreamUtils.transfer(trp.getInputStream(), out);
        out.write(StreamUtils.BYTES_EOF);
      }
      out.flush();
    } 
    catch(IOException e) {
      throw e;
    } catch(Exception e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  @Override
  public Transport read() throws IOException {
    InputStream in = sock.getInputStream();
    try {
      Object obj = xst.fromXML(in);
      if(obj == null || !Transport.class
          .isAssignableFrom(obj.getClass()))
        return null;
      Transport t = (Transport) obj;
      if(t.hasContentEmbedded())
        t.setInputStream(in);
      return t;
    } 
    catch(Exception e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  /**
   * <code>XmlNetChannel</code> é válido 
   * até que seja explicitamente fechado
   * através do método <code>close()</code>.
   * Pode ser utilizado entre diversos ciclos de
   * leitura e escrita.
   * @return <code>true</code> se não tiver sido 
   * fechado através do método <code>close()</code>.
   */
  @Override
  public boolean isValid() {
    return isvalid;
  }
  
  
  @Override
  public void close() {
    try {
      sock.shutdownInput();
      sock.shutdownOutput();
      sock.close();
      isvalid = false;
    } catch(IOException e) {}
  }
  
}
