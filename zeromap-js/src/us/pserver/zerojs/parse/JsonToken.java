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

package us.pserver.zerojs.parse;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/04/2016
 */
public enum JsonToken {
  
  START_OBJECT(JsonToken.CHAR_START_OBJECT),
  END_OBJECT(JsonToken.CHAR_END_OBJECT),
  START_ARRAY(JsonToken.CHAR_START_ARRAY),
  END_ARRAY(JsonToken.CHAR_END_ARRAY),
  QUOTE(JsonToken.CHAR_QUOTE),
  QUOTES(JsonToken.CHAR_QUOTES),
  COLON(JsonToken.CHAR_COLON),
  COMMA(JsonToken.CHAR_COMMA);
  
  private JsonToken(char ch) {
    this.ch = ch;
  }
  
  public char character() {
    return this.ch;
  }
  
  public static JsonToken forChar(char ch) {
    switch(ch) {
      case CHAR_COLON:
        return COLON;
      case CHAR_COMMA:
        return COMMA;
      case CHAR_END_ARRAY:
        return END_ARRAY;
      case CHAR_END_OBJECT:
        return END_OBJECT;
      case CHAR_QUOTE:
        return QUOTE;
      case CHAR_QUOTES:
        return QUOTES;
      case CHAR_START_ARRAY:
        return START_ARRAY;
      case CHAR_START_OBJECT:
        return START_OBJECT;
      default:
        throw new IllegalArgumentException(
            "Unknown JsonToken for char: "+ ch
        );
    }
  }
  
  private final char ch;
  

  public static final char CHAR_START_OBJECT = '{';
  
  public static final char CHAR_END_OBJECT = '}';
  
  public static final char CHAR_START_ARRAY = '[';
  
  public static final char CHAR_END_ARRAY = ']';
  
  public static final char CHAR_QUOTES = '"';
  
  public static final char CHAR_QUOTE = '\'';
  
  public static final char CHAR_COMMA = ',';
  
  public static final char CHAR_COLON = ':';

}
