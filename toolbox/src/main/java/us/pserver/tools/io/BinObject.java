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

package us.pserver.tools.io;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Set;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/12/2018
 */
public interface BinObject {

  public String getClassName();
  
  public long getClassID();
  
  public int size();
  
  public <T> T get(String name);
  
  public byte getByte(String name);
  
  public char getChar(String name);
  
  public short getShort(String name);
  
  public int getInt(String name);
  
  public float getFloat(String name);
  
  public double getDouble(String name);
  
  public long getLong(String name);
  
  public String getString(String name);
  
  public String getString(String name, Charset cs);
  
  public Set<String> getProperties();
  
  public boolean contains(String name);
  
  public <T> T remove(String name);
  
  public String sha256sum();
  
  public <T> BinObject put(String name, T value);
  
  public ByteBuffer toByteBuffer();
  
}
