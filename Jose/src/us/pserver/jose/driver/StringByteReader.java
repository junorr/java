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

package us.pserver.jose.driver;

import java.util.stream.Stream;
import us.pserver.jose.Region;
import us.pserver.jose.json.JsonType;
import us.pserver.jose.json.iterator.ByteIterator;
import us.pserver.tools.UTF8String;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/03/2017
 */
public interface StringByteReader extends ByteReader<String> {

  public int indexOf(String str);
  
  public Region regionOf(String off, String until);
  
  @Override
  public String read(Region reg);
  
  
  
  public static StringByteReader of(ByteReader rdr) {
    return null;
  }
  
  
  
  
  
  public static final class StringByteReaderImpl implements StringByteReader {
    
    private final ByteReader<byte[]> reader;
    
    
    public StringByteReaderImpl(ByteReader<byte[]> rdr) {
      if(rdr == null) {
        throw new IllegalArgumentException("Bad Null ByteReader");
      }
      this.reader = rdr;
    }


    @Override
    public int indexOf(String str) {
      return reader.indexOf(UTF8String.from(str).getBytes());
    }


    @Override
    public Region regionOf(String off, String until) {
      return reader.regionOf(
          UTF8String.from(off).getBytes(), 
          UTF8String.from(until).getBytes()
      );
    }


    @Override
    public String read(Region reg) {
      return UTF8String.from(reader.read(reg)).toString();
    }


    @Override
    public ByteIterator iterator() {
      return reader.iterator();
    }


    @Override
    public Stream<JsonType> stream() {
      return reader.stream();
    }


    @Override
    public int indexOf(byte[] val) {
      return reader.indexOf(val);
    }


    @Override
    public Region regionOf(byte[] off, byte[] until) {
      return reader.regionOf(off, until);
    }
    
  }
  
}
