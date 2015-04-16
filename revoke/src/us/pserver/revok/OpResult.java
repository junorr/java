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

package us.pserver.revok;

import java.util.Objects;

/**
 * Encapsula informações sobre o resultado da
 * invocação de um método, indicando se a operação
 * foi bem sucedida, seu valor de retorno ou exceção 
 * lançada.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/11/2013
 */
public class OpResult {

  private boolean success;
  
  private Object ret;
  
  private MethodInvocationException error;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public OpResult() {
    success = true;
    ret = null;
    error = null;
  }


  /**
   * Verifica se a operação foi bem sucedida.
   * @return <code>true</code> se a operação foi
   * bem sucedida, <code>false</code> caso
   * contrário.
   */
  public boolean isSuccessOperation() {
    return success;
  }


  /**
   * Define se a operação foi bem sucedida.
   * @param success <code>boolean</code>.
   */
  public void setSuccessOperation(boolean success) {
    this.success = success;
  }
  
  
  /**
   * Verifica se a operação possui valor de retorno.
   * @return <code>true</code> se a operação possui
   * valor de retorno, <code>false</code> caso
   * contrário.
   */
  public boolean hasReturn() {
    return ret != null;
  }
  
  
  /**
   * Verifica se houve erro na operação.
   * @return <code>true</code> se houve erro
   * na operação, <code>false</code> caso contrário.
   */
  public boolean hasError() {
    return error != null;
  }


  /**
   * Retorna o valor de retorno da operação.
   * @return objeto de retorno da operação ou
   * <code>null</code> caso não existir.
   */
  public Object getReturn() {
    return ret;
  }


  /**
   * Define o valor de retorno da operação.
   * @param ret valor de retorno da operação.
   */
  public void setReturn(Object ret) {
    this.ret = ret;
  }


  /**
   * Retorna o erro da operação.
   * @return Exception lançada na operação
   * ou <code>null</code> se não existir.
   */
  public MethodInvocationException getError() {
    return error;
  }


  /**
   * Define erro da operação.
   * @param error Exception lançada.
   */
  public void setError(Exception error) {
    if(error != null) {
      if(MethodInvocationException.class
          .isAssignableFrom(error.getClass()))
        this.error = (MethodInvocationException) error;
      else
        this.error = new MethodInvocationException(
            error.getMessage(), error);
    }
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + (this.success ? 1 : 0);
    hash = 37 * hash + Objects.hashCode(this.ret);
    hash = 37 * hash + Objects.hashCode(this.error);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final OpResult other = (OpResult) obj;
    if (this.success != other.success)
      return false;
    if (!Objects.equals(this.ret, other.ret))
      return false;
    if (!Objects.equals(this.error, other.error))
      return false;
    return true;
  }


  @Override
  public String toString() {
    return "OpResult{ " + "success = " + success + ", return = " + ret + " }";
  }
  
}
