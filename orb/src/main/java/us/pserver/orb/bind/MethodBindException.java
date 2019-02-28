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

package us.pserver.orb.bind;

import us.pserver.orb.OrbException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 25/02/2019
 */
public class MethodBindException extends OrbException {
  
  public MethodBindException() {
    super();
  }
  
  public MethodBindException(String msg) {
    super(msg);
  }
  
  public MethodBindException(Throwable root) {
    super(root.toString(), root);
  }
  
  public MethodBindException(String msg, Throwable root) {
    super(msg, root);
  }
  
  public MethodBindException(String msg, Object... args) {
    super(String.format(msg, args));
  }
  
  public MethodBindException(String msg, Throwable root, Object... args) {
    super(String.format(msg, args), root);
  }
  
  
  
  public static void call(Call c) throws MethodBindException {
    try {
      c.call();
    }
    catch(Exception e) {
      throw new MethodBindException(e);
    }
  }
  
  
  public static <T> T compute(Compute<T> c) throws MethodBindException {
    try {
      return c.call();
    }
    catch(Exception e) {
      throw new MethodBindException(e);
    }
  }
  
}
