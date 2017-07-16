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

package br.com.bb.disec.micro.refl;

import br.com.bb.disec.micro.refl.impl.OperationResultImpl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/06/2017
 */
public interface OperationResult {
  
  public boolean isSuccessful();
  
  public Optional<Object> getReturnValue();
  
  public Optional<Throwable> getThrownException();
  
  public List<StackTraceElement> getStackTrace();
  
  
  public static OperationResult of(Throwable th) {
    if(th == null) throw new IllegalArgumentException("Invalid null throwable");
    return new OperationResultImpl(false, null, th, Arrays.asList(th.getStackTrace()));
  }
  
  
  public static OperationResult of(Object returnValue) {
    if(returnValue == null) throw new IllegalArgumentException("Invalid null return value");
    return new OperationResultImpl(true, returnValue, null, Collections.EMPTY_LIST);
  }
  
  
  public static OperationResult successful() {
    return new OperationResultImpl(true, null, null, Collections.EMPTY_LIST);
  }
  
}
