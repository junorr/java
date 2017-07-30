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
import br.com.bb.disec.micro.box.Operation;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/07/2017
 */
public abstract class SyncOp extends BaseOp { 

  transient final ReentrantLock lock;
  
  
  SyncOp(String name, Operation next) {
    super(name, next);
    this.lock = new ReentrantLock();
  }
  
  
  SyncOp(String name) {
    this(name, null);
  }
  
  
  OpResult lockedCall(Supplier<Object> exe) {
    if(exe == null) {
      throw new IllegalArgumentException("Bad null exe method");
    }
    lock.lock();
    try {
      return OpResult.of(exe.get());
    }
    catch(Throwable th) {
      return OpResult.of(th);
    }
    finally {
      lock.unlock();
    }
  }
  
  
  OpResult lockedCall(Runnable exe) {
    if(exe == null) {
      throw new IllegalArgumentException("Bad null exe method");
    }
    lock.lock();
    try {
      exe.run();
      return OpResult.successful();
    }
    catch(Throwable th) {
      return OpResult.of(th);
    }
    finally {
      lock.unlock();
    }
  }
  
}
