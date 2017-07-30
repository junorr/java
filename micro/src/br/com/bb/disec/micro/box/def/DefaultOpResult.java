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

package br.com.bb.disec.micro.box.def;

import br.com.bb.disec.micro.box.OpResult;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2017
 */
public class DefaultOpResult implements OpResult {
  
  private final boolean successful;
  
  private final Object retval;
  
  private final Throwable thrown;
  
  private final List<StackTraceElement> stackTrace;
  
  
  public DefaultOpResult(boolean successful, Object returnValue, Throwable ex, List<StackTraceElement> stack) {
    this.successful = successful;
    this.retval = returnValue;
    this.thrown = ex;
    this.stackTrace = stack;
  }
  

  @Override
  public boolean isSuccessful() {
    return successful;
  }


  @Override
  public Optional<Object> getReturnValue() {
    return Optional.ofNullable(retval);
  }


  @Override
  public Optional<Throwable> getThrownException() {
    return Optional.ofNullable(thrown);
  }


  @Override
  public List<StackTraceElement> getStackTrace() {
    return stackTrace;
  }


  @Override
  public String toString() {
    return "OpResult{\n" + "  successful=" + successful + ",\n  retval=" + retval + ",\n  thrown=" + thrown + "\n}";
  }

}
