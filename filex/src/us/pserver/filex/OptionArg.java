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

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 22/05/2018
 */
public class OptionArg {

  private final Option opt;
  
  private List<String> args;
  
  
  public OptionArg(Option opt, List<String> args) {
    this.opt = Objects.requireNonNull(opt, "Bad null Option");
    this.args = Objects.requireNonNull(args, "Bad null arguments list");
  }
  
  public Option getOption() {
    return opt;
  }
  
  public List<String> getArgs() {
    return args;
  }
  
  public String get(int n) {
    return args.get(n);
  }
  
  public int getInt(int n) {
    try {
      return Integer.parseInt(args.get(n));
    } catch(NumberFormatException e) {
      return -1;
    }
  }
  
  public long getLong(int n) {
    try {
      return Long.parseLong(args.get(n));
    } catch(NumberFormatException e) {
      return -1;
    }
  }
  
}
