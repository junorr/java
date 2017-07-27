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

package br.com.bb.disec.micro.box;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/07/2017
 */
public abstract class SyncOp extends BaseOp { 

  final ReentrantLock lock;
  
  OpResult result;
  
  
  SyncOp(String name, Operation next) {
    super(name, next);
    this.result = null;
    this.lock = new ReentrantLock();
  }
  
  SyncOp(String name) {
    this(name, null);
  }
  
  
  @Override
  public OpResult getOpResult() {
    return result;
  }
  
  
  SyncOp setOpResult(OpResult opr) {
    lock.lock();
    try {
      this.result = opr;
    } finally {
      lock.unlock();
    }
    return this;
  }
  
  
  SyncOp setOpResult(Object obj) {
    return setOpResult(OpResult.of(obj));
  }
  
  
  SyncOp setOpResult(Throwable exc) {
    return setOpResult(OpResult.of(exc));
  }
  
  
  SyncOp setSuccessful() {
    return setOpResult(OpResult.successful());
  }
  
  
  Operation lockedCall(Supplier<Object> exe) {
    if(exe == null) {
      throw new IllegalArgumentException("Bad null exe method");
    }
    lock.lock();
    try {
      setOpResult(exe.get());
    }
    catch(Throwable th) {
      setOpResult(th);
    }
    finally {
      lock.unlock();
    }
    return this;
  }
  
  
  Operation lockedCall(Runnable exe) {
    if(exe == null) {
      throw new IllegalArgumentException("Bad null method exe");
    }
    lock.lock();
    try {
      exe.run();
      setSuccessful();
      return this;
    }
    finally {
      lock.unlock();
    }
  }
  
}
