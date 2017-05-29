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

package us.pserver.jose.json.iterator;

import java.util.Iterator;
import us.pserver.jose.json.JsonType;
import us.pserver.jose.json.JsonValue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/02/2017
 */
public interface ByteIterator extends Iterator<JsonType> {

  public String readString();
  
  public boolean readBoolean();
  
  public Number readNumber();
  
  public Object readNull();
  
  public String readField();
  
  public JsonValue read(JsonType tkn);
  
  public JsonValue readNext();
  
  public ByteIterator unreadByte();
  
  public JsonType nextValueType();
  
  public ByteIterator skip();
  
  public boolean isInsideArray();
  
  public int getObjectLevel();
  
  public int getArrayLevel();
  
  public String getCurrentField();
  
  public byte getCurrentByte();
  
}
