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

package us.pserver.zerojs.handler;

import java.util.concurrent.locks.ReentrantLock;
import us.pserver.zerojs.JsonHandler;
import us.pserver.zerojs.JsonParseException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/04/2016
 */
public class SyncJsonHandler implements JsonHandler {
  
  private final JsonHandler handler;
  
  private final ReentrantLock lock;
  
  
  public SyncJsonHandler(JsonHandler jh) {
    if(jh == null) {
      throw new IllegalArgumentException(
          "Invalid null JsonHandler"
      );
    }
    this.handler = jh;
    this.lock = new ReentrantLock();
  }
  

  @Override
  public void startObject() throws JsonParseException {
    lock.lock();
    try {
      handler.startObject();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public void endObject() throws JsonParseException {
    lock.lock();
    try {
      handler.endObject();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public void startArray() throws JsonParseException {
    lock.lock();
    try {
      handler.startArray();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public void endArray() throws JsonParseException {
    lock.lock();
    try {
      handler.endArray();
    } finally {
      lock.unlock();
    }
  }


  @Override
  public void name(String str) throws JsonParseException {
    lock.lock();
    try {
      handler.name(str);
    } finally {
      lock.unlock();
    }
  }


  @Override
  public void value(String str) throws JsonParseException {
    lock.lock();
    try {
      handler.value(str);
    } finally {
      lock.unlock();
    }
  }
  
  
  @Override
  public String toString() {
    lock.lock();
    try {
      return handler.toString();
    } finally {
      lock.unlock();
    }
  }
  
}
