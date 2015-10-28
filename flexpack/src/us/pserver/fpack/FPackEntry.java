/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.fpack;

import com.cedarsoftware.util.io.JsonWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import us.pserver.tools.UTF8String;
import us.pserver.valid.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FPackEntry extends FPackHeader {

  
  private Map<String, Object> values;
  
  private transient byte[] buffer;
  
  
  protected FPackEntry() {
    super();
    values = new HashMap<>();
    buffer = null;
  }
  
  
  public FPackEntry(String name) {
    super(name);
    values = new HashMap<>();
    buffer = null;
  }
  
  
  public FPackEntry(String name, long pos, long size) {
    super(name, pos, size);
    values = new HashMap<>();
    buffer = null;
  }
  
  
  public Map<String, Object> valuesMap() {
    return values;
  }
  
  
  public FPackEntry put(String key, Object value) {
    if(key != null && value != null) {
      values.put(key, value);
      buffer = null;
    }
    return this;
  }
  
  
  public Object get(String key) {
    if(key != null) {
      return values.get(key);
    }
    return null;
  }


  public Map<String, Object> getValues() {
    return values;
  }


  public int getWriteSize() {
    if(buffer == null) {
      ByteArrayOutputStream bos = 
          new ByteArrayOutputStream();
      try { write(bos); }
      catch(IOException e){}
      buffer = bos.toByteArray();
    }
    return buffer.length;
  }
  
  
  @Override
  public FPackEntry setName(String name) {
    super.setName(name);
    buffer = null;
    return this;
  }
  
  
  @Override
  public FPackEntry setPosition(long pos) {
    super.setPosition(pos);
    buffer = null;
    return this;
  }
  
  
  @Override
  public FPackEntry setSize(long size) {
    super.setSize(size);
    buffer = null;
    return this;
  }
  
  
  @Override
  public void write(OutputStream out) throws IOException {
    Valid.off(out).forNull()
        .fail(OutputStream.class);
    if(buffer != null) {
      out.write(buffer);
    }
    else {
      byte[] bs = new UTF8String(
          JsonWriter.objectToJson(this)
      ).getBytes();
      out.write(bs);
      out.write(FPackConstants.ENTRY_END.getBytes());
      out.flush();
    }
  }
  
  
  public static void main(String[] args) throws IOException {
    FPackEntry entry = new FPackEntry("some entry name");
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    entry.write(bos);
    System.out.println("* entry.length="+ bos.size());
    System.out.write(bos.toByteArray());
  }
  
}
