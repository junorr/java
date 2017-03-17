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

package us.pserver.jose.json;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/02/2017
 */
public enum JsonType {

  BOOLEAN(ByteType.BOOL_FALSE),
  
  END_ARRAY(ByteType.END_ARRAY),
  
  END_OBJECT(ByteType.END_OBJECT),
  
  FIELD(ByteType.FIELD),
  
  IGNORE(ByteType.IGNORE),
  
  NUMBER(),
  
  START_ARRAY(ByteType.START_ARRAY),
  
  START_OBJECT(ByteType.START_OBJECT),
  
  STRING(ByteType.STRING),
  
  VALUE(ByteType.VALUE),
  
  NULL(ByteType.NULL),
  
  NO_MORE_DATA(),
  
  UNKNOWN(Byte.MIN_VALUE);
  
  
  public byte value() {
    return this.value;
  }
  
  private JsonType(byte b) {
    this.value = b;
  }
  
  private JsonType() {
    this.value = 0;
  }
  
  private final byte value;
  
  
  public static JsonType of(byte b) {
    switch(b) {
      case ByteType.BOOL_FALSE:
      case ByteType.BOOL_UFALSE:
      case ByteType.BOOL_TRUE:
      case ByteType.BOOL_UTRUE:
        return BOOLEAN;
      case ByteType.END_ARRAY:
        return END_ARRAY;
      case ByteType.END_OBJECT:
        return END_OBJECT;
      case ByteType.FIELD:
        return FIELD;
      case ByteType.IGNORE:
        return IGNORE;
      case ByteType.START_ARRAY:
        return START_ARRAY;
      case ByteType.START_OBJECT:
        return START_OBJECT;
      case ByteType.STRING:
        return STRING;
      case ByteType.VALUE:
        return VALUE;
      case ByteType.NUM0:
        return NUMBER;
      case ByteType.NUM1:
        return NUMBER;
      case ByteType.NUM2:
        return NUMBER;
      case ByteType.NUM3:
        return NUMBER;
      case ByteType.NUM4:
        return NUMBER;
      case ByteType.NUM5:
        return NUMBER;
      case ByteType.NUM6:
        return NUMBER;
      case ByteType.NUM7:
        return NUMBER;
      case ByteType.NUM8:
        return NUMBER;
      case ByteType.NUM9:
        return NUMBER;
      case ByteType.MINUS:
        return NUMBER;
      case ByteType.NULL:
      case ByteType.UNULL:
        return NULL;
      default:
        return UNKNOWN;
    }
  }
  
}
