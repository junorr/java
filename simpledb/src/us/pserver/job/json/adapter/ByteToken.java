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

  BOOL_FALSE(ByteValue.BOOL_FALSE),
  
  BOOL_TRUE(ByteValue.BOOL_TRUE),
  
  END_ARRAY(ByteValue.END_ARRAY),
  
  END_OBJECT(ByteValue.END_OBJECT),
  
  FIELD(ByteValue.FIELD),
  
  IGNORE(ByteValue.IGNORE),
  
  NUMBER(),
  
  START_ARRAY(ByteValue.START_ARRAY),
  
  START_OBJECT(ByteValue.START_OBJECT),
  
  STRING(ByteValue.STRING),
  
  VALUE(ByteValue.VALUE),
  
  NUM0(ByteValue.NUMBERS[0]),
  
  NUM1(ByteValue.NUMBERS[1]),
  
  NUM2(ByteValue.NUMBERS[2]),
  
  NUM3(ByteValue.NUMBERS[3]),
  
  NUM4(ByteValue.NUMBERS[4]),
  
  NUM5(ByteValue.NUMBERS[5]),
  
  NUM6(ByteValue.NUMBERS[6]),
  
  NUM7(ByteValue.NUMBERS[7]),
  
  NUM8(ByteValue.NUMBERS[8]),
  
  NUM9(ByteValue.NUMBERS[9]);
  
  
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
  
}
