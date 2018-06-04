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

package us.pserver.filex;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/05/2018
 */
public enum Option {
  
  HELP("-h", "--help"),

  INDEX_OF("-i", "--index-of"),

  GET("-g", "--get"),

  LINE("-l", "--line-unix"),

  LINE_WIN("-w", "--line-win"),

  BUFFER_SIZE("-b", "--buffer-size"),
  
  IGNORE_CASE("-c", "--ignore-case"),

  VERBOSE("-v", "--verbose");

  private Option(String opt, String longOpt) {
    this.opt = opt;
    this.longOpt = longOpt;
  }

  public String getOpt() {
    return opt;
  }

  public String getLongOpt() {
    return longOpt;
  }

  private final String opt;

  private final String longOpt;
  
  
  public static Option fromString(String str) {
    switch(str) {
      case "-h":
      case "--help":
        return HELP;
      case "-i":
      case "--index-of":
        return INDEX_OF;
      case "-g":
      case "--get":
        return GET;
      case "-l":
      case "--line-unix":
        return LINE;
      case "-w":
      case "--line-win":
        return LINE_WIN;
      case "-b":
      case "--buffer-size":
        return BUFFER_SIZE;
      case "-c":
      case "--ignore-case":
        return IGNORE_CASE;
      case "-v":
      case "--verbose":
        return VERBOSE;
      default:
        throw new IllegalArgumentException("Unknown Option: "+ str);
    }
  }

}
