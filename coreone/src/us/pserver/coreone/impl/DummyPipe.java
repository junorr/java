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

package us.pserver.coreone.impl;

import java.util.function.Consumer;
import us.pserver.coreone.Pipe;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/10/2017
 */
public class DummyPipe implements Pipe<Void> {
  
  @Override public void onAvailable(Consumer<Void> cs) {}
  
  @Override public void onError(Consumer<Throwable> cs) {}
  
  @Override
  public Void pull(long timeout) throws InterruptedException {
    return null;
  }
  
  @Override
  public Void pull() {
    return null;
  }
  
  @Override public boolean push(Void val) { return false; }
  
  @Override public void error(Throwable th) {}
  
  @Override public boolean available() {return false;}

  @Override public boolean isClosed() { return true; }

  @Override public void close() {}
  
  @Override public void closeOnEmpty() {}
  
}
