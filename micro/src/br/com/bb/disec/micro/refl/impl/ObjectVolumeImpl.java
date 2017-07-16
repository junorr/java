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

package br.com.bb.disec.micro.refl.impl;

import br.com.bb.disec.micro.refl.ObjectVolume;
import br.com.bb.disec.micro.refl.Operation;
import br.com.bb.disec.micro.refl.OperationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import us.pserver.dyna.DynaLoader;
import us.pserver.dyna.DynaLoaderInstance;
import us.pserver.tools.rfl.Reflector;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/06/2017
 */
public class ObjectVolumeImpl implements ObjectVolume {
  
  private final Object obj;
  
  private ReentrantLock lock;
  
  
  public ObjectVolumeImpl(Object obj) {
    if(obj == null) {
      throw new IllegalArgumentException("Invalid null object");
    }
    this.obj = obj;
    this.lock = new ReentrantLock();
  }
  
  
  @Override
  public OperationResult execute(Operation op) {
    if(op == null 
        || op.getName() == null 
        || op.getType() == null) 
    {
      throw new IllegalArgumentException("Invalid Operation");
    }
    lock.lock();
    try {
      switch(op.getType()) {
        case GET_FIELD:
          return getField(op);
        case SET_FIELD:
          return setField(op);
        case INVOKE_METHOD:
          return method(op);
        default:
          throw new UnsupportedOperationException(op.getType().name());
      }
    }
    catch(Exception ex) {
      return OperationResult.of(ex);
    }
    finally {
      lock.unlock();
    }
  }
  
  
  private OperationResult getField(Operation op) throws Exception {
    Reflector ref = new Reflector(obj);
    return OperationResult.of(ref.selectField(op.getName()).get());
  }
  
  
  private OperationResult setField(Operation op) throws Exception {
    Object arg = (op.getArguments().isEmpty() 
        ? null : op.getArguments().get(0)
    );
    Reflector ref = new Reflector(obj);
    ref.selectField(op.getName()).set(arg);
    return OperationResult.successful();
  }
  
  
  private OperationResult method(Operation op) throws Exception {
    Reflector ref = new Reflector(obj);
    if(!op.getArgumentTypes().isEmpty()) {
      ref.selectMethod(op.getName(), getTypes(op.getArgumentTypes()));
    }
    else {
      ref.selectMethod(op.getName());
    }
    Object ret = null;
    if(op.getArguments().isEmpty()) {
      ret = ref.invoke();
    }
    else {
      ret = ref.invoke(op.getArguments().toArray());
    }
    return OperationResult.of(ret);
  }
  
  
  private Class[] getTypes(List<String> stp) {
    if(stp.isEmpty()) return null;
    DynaLoader dnl = new DynaLoaderInstance();
    List<Class> cls = new ArrayList<>(stp.size());
    stp.forEach(s->cls.add(dnl.load(s)));
    return cls.toArray(new Class[stp.size()]);
  }
  
}
