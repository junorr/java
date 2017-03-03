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

package us.pserver.job.json.adapter;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/02/2017
 */
public enum ByteToken {

  BOOLEAN(ByteValue.BOOL_FALSE),
  
  END_ARRAY(ByteValue.END_ARRAY),
  
  END_OBJECT(ByteValue.END_OBJECT),
  
  FIELD(ByteValue.FIELD),
  
  IGNORE(ByteValue.IGNORE),
  
  NUMBER(),
  
  START_ARRAY(ByteValue.START_ARRAY),
  
  START_OBJECT(ByteValue.START_OBJECT),
  
  STRING(ByteValue.STRING),
  
  VALUE(ByteValue.VALUE),
  
  NULL(ByteValue.NULL),
  
  UNKNOWN(Byte.MIN_VALUE);
  
  
  public byte value() {
    return this.value;
  }
  
  private ByteToken(byte b) {
    this.value = b;
  }
  
  private ByteToken() {
    this.value = 0;
  }
  
  private final byte value;
  
  
  public static ByteToken of(byte b) {
    switch(b) {
      case ByteValue.BOOL_FALSE:
      case ByteValue.BOOL_UFALSE:
      case ByteValue.BOOL_TRUE:
      case ByteValue.BOOL_UTRUE:
        return BOOLEAN;
      case ByteValue.END_ARRAY:
        return END_ARRAY;
      case ByteValue.END_OBJECT:
        return END_OBJECT;
      case ByteValue.FIELD:
        return FIELD;
      case ByteValue.IGNORE:
        return IGNORE;
      case ByteValue.START_ARRAY:
        return START_ARRAY;
      case ByteValue.START_OBJECT:
        return START_OBJECT;
      case ByteValue.STRING:
        return STRING;
      case ByteValue.VALUE:
        return VALUE;
      case ByteValue.NUM0:
        return NUMBER;
      case ByteValue.NUM1:
        return NUMBER;
      case ByteValue.NUM2:
        return NUMBER;
      case ByteValue.NUM3:
        return NUMBER;
      case ByteValue.NUM4:
        return NUMBER;
      case ByteValue.NUM5:
        return NUMBER;
      case ByteValue.NUM6:
        return NUMBER;
      case ByteValue.NUM7:
        return NUMBER;
      case ByteValue.NUM8:
        return NUMBER;
      case ByteValue.NUM9:
        return NUMBER;
      case ByteValue.MINUS:
        return NUMBER;
      case ByteValue.NULL:
      case ByteValue.UNULL:
        return NULL;
      default:
        return UNKNOWN;
    }
  }
  
}
