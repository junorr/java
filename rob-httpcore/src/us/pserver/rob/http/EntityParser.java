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

package us.pserver.rob.http;

import com.cedarsoftware.util.io.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.streams.IO;
import us.pserver.streams.MixedReadBuffer;
import us.pserver.streams.MixedWriteBuffer;
import us.pserver.streams.StreamResult;
import us.pserver.streams.StreamUtils;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 14/04/2015
 */
public class EntityParser {

  private MixedWriteBuffer buffer;
  
  private InputStream parseInput, input;
  
  private Object obj;
  
  private CryptKey key;
  
  
  public EntityParser(InputStream is) {
    if(is == null)
      throw new IllegalArgumentException("[EntityParser( InputStream )] "
          + "Invalid InputStream {is="+ is+ "}");
    parseInput = is;
    buffer = new MixedWriteBuffer();
  }
  
  
  public static EntityParser parser(InputStream is) {
    return new EntityParser(is);
  }
  
  
  public EntityParser enableCryptCoder(CryptKey key) {
    if(key != null) {
      buffer.getCoderFactory().setCryptCoderEnabled(true, key);
    }
    return this;
  }
  
  
  public EntityParser disableAllCoders() {
    buffer.getCoderFactory().clearCoders();
    return this;
  }
  
  
  public EntityParser disableCryptCoder() {
    buffer.getCoderFactory().setCryptCoderEnabled(false, null);
    return this;
  }
  
  
  public EntityParser enableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(true);
    return this;
  }
  
  
  public EntityParser disableGZipCoder() {
    buffer.getCoderFactory().setGZipCoderEnabled(false);
    return this;
  }
  
  
  public EntityParser enableBase64Coder() {
    buffer.getCoderFactory().setBase64CoderEnabled(true);
    return this;
  }
  
  
  public EntityParser disableBase64Coder() {
    buffer.getCoderFactory().setBase64CoderEnabled(false);
    return this;
  }
  
  
  public Object getObject() {
    return obj;
  }
  
  
  public CryptKey getCryptKey() {
    return key;
  }
  
  
  public InputStream getInputStream() {
    return input;
  }
  
  
  public EntityParser parse() throws IOException {
    buffer.clear();
    //System.out.println("parse(): buffer.coder="+ buffer.getCoderFactory());
    
    String five = StreamUtils.readString(parseInput, 5);
    System.out.println("* five='"+ five+ "'");
    if(!MessageConsts.START_XML.equals(five)) {
      throw new IOException("[EntityParser.parse()] "
          + "Invalid Content to Parse {expected=<xml>, read="+ five+ "}");
    }
    
    five = StreamUtils.readString(parseInput, 5);
    System.out.println("* five='"+ five+ "'");
    if(MessageConsts.START_CRYPT_KEY.contains(five)) {
      StreamUtils.readString(parseInput, 1);
      StreamResult sr = StreamUtils.readStringUntil(parseInput, MessageConsts.END_CRYPT_KEY);
      key = CryptKey.fromString(sr.content());
      this.enableCryptCoder(key);
      five = StreamUtils.readString(parseInput, 5);
      System.out.println("* five='"+ five+ "'");
  }
    
    if(!MessageConsts.START_CONTENT.equals(five)) {
      throw new IOException("[EntityParser.parse()] "
          + "Invalid Content to Parse {expected=<cnt>, read="+ five+ "}");
    }
    
    IO.tr(parseInput, buffer.getRawOutputStream());
    parseInput = buffer.getReadBuffer().getInputStream();
    
    five = StreamUtils.readString(parseInput, 5);
    System.out.println("* five='"+ five+ "'");
    if(MessageConsts.START_ROB.contains(five)) {
      StreamResult sr = StreamUtils.readStringUntil(parseInput, MessageConsts.END_ROB);
      String js = sr.content();
      obj = JsonReader.jsonToJava(js);
      five = StreamUtils.readString(parseInput, 5);
      System.out.println("* five='"+ five+ "'");
    }
    
    if(MessageConsts.START_STREAM.contains(five)) {
      MixedWriteBuffer inbuf = new MixedWriteBuffer();
      StreamUtils.readString(parseInput, 3);
      StreamUtils.transferUntil(parseInput, inbuf.getRawOutputStream(), MessageConsts.END_STREAM);
      input = inbuf.getReadBuffer().getRawInputStream();
    }
    return this;
  }
  
}
