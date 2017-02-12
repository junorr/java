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

package us.pserver.job.query.op;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/01/2017
 */
public final class DoubleBetween extends NumberOperation {
  
  private final Double value2;
  
  
  public DoubleBetween() { 
    super(); 
    this.value2 = Double.valueOf(0);
  }
  
  public DoubleBetween(Number n, Double n2) { 
    super(n);
    if(n2 == null) {
      throw new IllegalArgumentException("Bad Null Number 2");
    }
    this.value2 = n2;
  }
  
  Double value2() {
    return value2;
  }

  @Override
  public boolean apply(Number other) {
    return other != null 
        && other.doubleValue() >= value().doubleValue()
        && other.doubleValue() <= value2().doubleValue();
  }

}