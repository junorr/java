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

package us.pserver.rob.channel;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;
import us.pserver.http.HttpConst;
import us.pserver.streams.IO;
import us.pserver.streams.MultiCoderBuffer;
import us.pserver.streams.ProtectedInputStream;
import us.pserver.streams.ProtectedOutputStream;
import us.pserver.streams.StreamCoderFactory;
import us.pserver.streams.StreamResult;
import us.pserver.streams.StreamUtils;
import static us.pserver.streams.StreamUtils.EOF;


/**
 * Implementa um canal de transmissão de objetos em rede
 * no formato XML, utilizando <code>XStream</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class TcpXmlChannel implements Channel {
  
  private Socket sock;
  
  private XStream xst;
  
  private boolean isvalid;
  
  private CryptKey key;
  
  private Base64StringCoder bcd;
  
  private StringByteConverter scv;
  
  private MultiCoderBuffer buffer;
  
  private boolean crypt, gzip;
  
  private CryptAlgorithm algo;
  
  
  /**
   * Construtor padrão que recebe um 
   * <code>Socket</code> para comunicação em rede.
   * @param sc <code>Socket</code>.
   */
  public TcpXmlChannel(Socket sc) {
    if(sc == null || sc.isClosed())
      throw new IllegalArgumentException(
          "Invalid Socket ["+ sc+ "]");
    sock = sc;
    isvalid = true;
    buffer = null;
    crypt = false;
    gzip = false;
    algo = CryptAlgorithm.AES_CBC_PKCS5;
    xst = new XStream();
    scv = new StringByteConverter();
    bcd = new Base64StringCoder();
    key = null;
  }
  
  
  public TcpXmlChannel setEncryptionEnabled(boolean enabled) {
    crypt = enabled;
    return this;
  }
  
  
  public boolean isEncryptionEnabled() {
    return crypt;
  }
  
  
  public TcpXmlChannel setGZipCompressionEnabled(boolean enabled) {
    gzip = enabled;
    return this;
  }
  
  
  public boolean isGZipCompressionEnabled() {
    return gzip;
  }
  
  
  public TcpXmlChannel setCryptAlgorithm(CryptAlgorithm ca) {
    nullarg(CryptAlgorithm.class, ca);
    algo = ca;
    return this;
  }
  
  
  public CryptAlgorithm getCryptAlgorithm() {
    return algo;
  }
  
  
  /**
   * Retorna o <code>Socket</code>.
   * @return <code>Socket</code>.
   */
  public Socket getSocket() {
    return sock;
  }
  
  
  private void writeKey(OutputStream os) throws IOException {
    System.out.println("TcpXmlChannel.writeKey");
    nullarg(OutputStream.class, os);
    
    StringBuffer sb = new StringBuffer();
    sb.append(HttpConst.BOUNDARY_XML_START)
        .append(HttpConst.BOUNDARY_CRYPT_KEY_START)
        .append(bcd.encode(key.toString()))
        .append(HttpConst.BOUNDARY_CRYPT_KEY_END);
    byte[] bs = scv.convert(sb.toString());
    System.out.println("StringByteConverter.reverseKey="+ scv.reverse(bs));
    os.write(scv.convert(sb.toString()));
    os.flush();
  }
  
  
  private void writeTransport(Transport t, OutputStream os) throws IOException {
    nullarg(Transport.class, t);
    nullarg(OutputStream.class, os);
    
    try {
      xst.toXML(t.getWriteVersion(), os);
      os.write(scv.convert(HttpConst.BOUNDARY_OBJECT_END));
      
      if(t.hasContentEmbedded()) {
        os.write(scv.convert(HttpConst.BOUNDARY_CONTENT_START));
        IO.tr(t.getInputStream(), os);
        os.write(scv.convert(HttpConst.BOUNDARY_CONTENT_END));
      }
      os.flush();
    } 
    catch(IOException e) {
      throw e;
    } 
    catch(Exception e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  @Override
  public void write(Transport trp) throws IOException {
    System.out.println("TcpXmlChannel.write");
    if(trp == null) return;
    if(buffer != null) buffer.close();
    ProtectedOutputStream pos = new ProtectedOutputStream(
        sock.getOutputStream());
    
    if(crypt) {
      if(key == null)
        key = CryptKey.createRandomKey(algo);
      writeKey(pos);
    }
    pos.write(scv.convert(HttpConst.BOUNDARY_OBJECT_START));
    
    OutputStream sout = pos;
    if(gzip || crypt)
      sout = StreamCoderFactory.getNew()
          .setGZipCoderEnabled(gzip)
          .setCryptCoderEnabled(crypt, key)
          .create(pos);
    writeTransport(trp, sout);
    sout.write(scv.convert(HttpConst.BOUNDARY_XML_END));
    sout.flush();
    sout.close();
      
    StreamUtils.writeEOF(pos);
    pos.flush();
  }
  
  
  private CryptKey readKey(InputStream is) throws IOException {
    System.out.println("TcpXmlChannel.readKey()");
    nullarg(InputStream.class, is);
    StreamResult sr = StreamUtils.readUntilOr(is, 
        HttpConst.BOUNDARY_CRYPT_KEY_START, 
        HttpConst.BOUNDARY_OBJECT_START);
    System.out.println("TcpXmlChannel.readKey: "+ sr);
    if(sr.token() != HttpConst.BOUNDARY_CRYPT_KEY_START) {
      return null;
    }
    
    sr = StreamUtils.readStringUntil(is, HttpConst.BOUNDARY_CRYPT_KEY_END);
    return CryptKey.fromString(bcd.decode(sr.content()));
  }
  
  
  private InputStream readContent(InputStream is) throws IOException {
    System.out.println("TcpXmlChannel.readContent()");
    nullarg(InputStream.class, is);
    if(buffer != null) buffer.close();
    buffer = new MultiCoderBuffer();
    StreamUtils.transferBetween(is, buffer.getOutputStream(), 
        HttpConst.BOUNDARY_CONTENT_START,
        HttpConst.BOUNDARY_CONTENT_END);
    return buffer.flip().getInputStream();
  }
  
  
  private Transport readTransport(InputStream is) throws IOException {
    System.out.println("TcpXmlChannel.readTransport()");
    nullarg(InputStream.class, is);
    
    try {
      StreamResult sr = StreamUtils.readStringUntil(is, 
          HttpConst.BOUNDARY_OBJECT_END);
      System.out.println("TcpXmlChannel.readObjectEnd="+ sr);
      if(sr.content() == null || sr.content().trim().isEmpty())
        return null;
      
      Transport t = (Transport) xst.fromXML(sr.content());
      if(t == null)
        throw new IOException("Can not read Transport ["+ t+ "]");
      if(t.hasContentEmbedded())
        t.setInputStream(readContent(is));
      
      return t;
    }
    catch(IOException e) {
      throw e;
    } 
    catch(Exception e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  
  
  @Override
  public Transport read() throws IOException {
    if(buffer != null) buffer.close();
    ProtectedInputStream pin = new ProtectedInputStream(
        sock.getInputStream());
    System.out.println("TcpXmlChannel.read()");
    
    key = readKey(pin);
    System.out.println("TcpXmlChannel.key="+ key);
    if(key != null) {
      System.out.println("TcpXmlChannel.readingObjectBoundary...");
      StreamUtils.readUntil(pin, HttpConst.BOUNDARY_OBJECT_START);
    }
    
    System.out.println("TcpXmlChannel.creatingCodecStream...");

    InputStream sin = pin;
    if(gzip || key != null)
      sin = StreamCoderFactory.getNew()
          .setGZipCoderEnabled(gzip)
          .setCryptCoderEnabled(key != null, key)
          .create(pin);
      
    Transport t = readTransport(sin);
    sin.close();
    return t;
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
      if(sock != null) {
        sock.shutdownInput();
        sock.shutdownOutput();
        sock.close();
      }
      if(buffer != null) buffer.close();
      isvalid = false;
    } catch(IOException e) {}
  }
  
}
