/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package us.pserver.smail.internal;


/**
 * <p style="font-size: medium;">
 * Lock implementa uma trava ou monitor seguro para Threads.
 * </p>
 * 
 * @see java.io.InputStream
 * @see javax.activation.DataSource
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public abstract class Lock {
  
  private final Object hold;
  
  private static boolean lock;
  
  private static boolean secured = false;
  
  private static Lock instance;
  
  
  /**
   * Construtor protegido, não deve ser chamado
   * diretamente.
   */
  protected Lock() {
    if(!secured)
      throw new IllegalStateException(
          "{Lock()}: Lock object never "
          + "should be instantiated!");
    
    lock = false;
    hold = new Object();
    secured = false;
  }
  
  
  /**
   * Retorna uma instância de Lock.
   * @return Instância de Lock.
   */
  public static Lock getLock() {
    if(instance == null) {
      Lock.secured = true;
      instance = new Lock() {};
    }
    return instance;
  }
  
  
  /**
   * Verifica se já está travado.
   * @return <code>true</code> se 
   * estiver travado, <code>false</code>
   * caso contrário.
   */
  public boolean isLocked() {
    synchronized(hold) {
      return lock;
    }
  }
  
  
  /**
   * Trava o objeto.
   * @throws IllegalStateException caso já esteja travado.
   */
  public void lock() {
    synchronized(hold) {
      if(lock) 
        throw new IllegalStateException("Already Locked!");
      
      lock = true;
    }
  }
  
  
  /**
   * Tenta obter a trava, retornando <code>boolean</code>
   * indicando o sucesso.
   * @return <code>true</code> se a trava seja
   * obtida com sucesso, <code>false</code>
   * caso contrário.
   */
  public boolean tryLock() {
    try {
      lock();
      return true;
    } catch(IllegalStateException ex) {
      return false;
    }
  }
  
  
  /**
   * Destrava o objeto.
   */
  public void unlock() {
    synchronized(hold) {
      lock = false;
      hold.notifyAll();
    }
  }
  
  
  /**
   * Aguarda pelo tempo máximo especificado
   * a obtenção da trava.
   * <code>[maxtime &lt;= 0]:</code> Tempo infinito.
   * @param maxtime Tempo máximo para
   * aguardar <code>[maxtime &lt;= 0]:</code> Tempo infinito.
   * @return <code>true</code> se a trava for obtida
   * com sucesso, <code>false</code> caso contrário.
   */
  public boolean waitLock(long maxtime) {
    if(maxtime <= 0) 
      return this.waitLock();
    
    try {
      synchronized(hold) {
        hold.wait(maxtime);
      }
      return this.tryLock();
    } catch(InterruptedException ex) {
      return false;
    }
  }
  
  
  /**
   * Aguarda por tempo indeterminado
   * a obtenção da trava.
   * @return <code>true</code> se a trava for obtida
   * com sucesso, <code>false</code> caso contrário.
   */
  public boolean waitLock() {
    try {
      synchronized(hold) {
        hold.wait();
      }
      return this.tryLock();
    } catch(InterruptedException ex) {
      return false;
    }
  }
  
  
  /**
   * <code>return "[Lock( " + this.isLocked() + " )]";</code>
   * @return String representando a trava.
   */
  @Override
  public String toString() {
    return "[Lock( " + this.isLocked() + " )]";
  }
  
}
