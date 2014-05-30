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

package com.jpower.worker;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 07/05/2013
 */
public class JobContext {

  private Worker worker;
  
  private Object data;
  
  
  public JobContext(Worker w) {
    if(w == null) 
      throw new IllegalArgumentException(
          "Invalid Worker instance: "+ w);
    worker = w;
    data = null;
  }
  
  
  public JobContext(Worker w, Object data) {
    this(w);
    this.data = data;
  }


  public Worker getWorker() {
    return worker;
  }


  public JobContext setWorker(Worker worker) {
    this.worker = worker;
    return this;
  }


  public Object getData() {
    return data;
  }


  public JobContext setData(Object data) {
    this.data = data;
    return this;
  }
  
}
