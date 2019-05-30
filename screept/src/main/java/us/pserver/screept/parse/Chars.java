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

package us.pserver.screept.parse;

import java.util.stream.IntStream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30 de mai de 2019
 */
public class Chars {

  public static final int SPACE = ' ';
  
  public static final int QUOTEDBL = '"';
  
  public static final int LN = '\n';
  
  public static final int OPEN_PAR = '(';
  
  public static final int CLOSE_PAR = ')';
  
  public static final int OPEN_CURLY = '{';
  
  public static final int CLOSE_CURLY = '}';
  
  public static final int COMMA = ',';
  
  public static final int EQUALS = '=';
  
  public static final int OP_SUM = '+';
  
  public static final int OP_SUB = '-';
  
  public static final int OP_MULT = '*';
  
  public static final int OP_DIV = '/';
  
  public static final int OP_POW = '^';
  
  public static final int OP_AND = '&';
  
  public static final int OP_OR = '|';
  
  public static int[] DELIMITER_CHARS = {SPACE, QUOTEDBL, LN, OPEN_PAR, CLOSE_PAR, OPEN_CURLY, CLOSE_CURLY, COMMA, EQUALS, OP_SUM, OP_SUB, OP_MULT, OP_DIV, OP_POW, OP_AND, OP_OR};
  
  public static int[] OP_CHARS = {OP_SUM, OP_SUB, OP_MULT, OP_DIV, OP_POW, OP_AND, OP_OR};
  
  public static int[] NUMERIC_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'};
  
  public static int[] BOOLEAN_CHARS = {'t', 'r', 'u', 'e', 'f', 'a', 'l', 's', 'e'};
  
  public static int[] VAR_CHARS = {'v', 'a', 'r'};
  
  public static int[] FUNC_CHARS = {'f', 'u', 'n', 'c', 't', 'i', 'o', 'n'};
  
  public static int[] IF_CHARS = {'i', 'f', 'e', 'l', 's', 'e'};
  
  public static int[] WHILE_CHARS = {'w', 'h', 'i', 'l', 'e'};
  
  
  public static boolean isDelimiter(int c) {
    return IntStream.of(DELIMITER_CHARS).anyMatch(i -> i == c);
  }
  
  public static boolean isNumeric(int c) {
    return IntStream.of(NUMERIC_CHARS).anyMatch(i -> i == c);
  }
  
  public static boolean isNumber(String s) {
    return s.chars().allMatch(Chars::isNumeric);
  }
  
  public static boolean isBoolean(int c) {
    return IntStream.of(NUMERIC_CHARS).anyMatch(i -> i == c);
  }
  
  public static boolean isBoolean(String s) {
    return s.chars().allMatch(Chars::isBoolean);
  }
  
  public static boolean isOperation(int c) {
    return IntStream.of(OP_CHARS).anyMatch(i -> i == c);
  }
  
}
