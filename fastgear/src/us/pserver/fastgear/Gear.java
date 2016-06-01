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

package us.pserver.fastgear;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/05/2016
 */
public interface Gear<I,O> extends Runnable {

  public Running<I,O> start();
  
  public boolean isReady();
  
  public void suspend() throws InterruptedException;
  
  public void suspend(long timeout) throws InterruptedException;
  
  public void resume() throws InterruptedException;
  
  
  public static <A,B> Gear<A,B> of(Shift<A,B,? extends Exception> shf) {
    return null;
  }
  
  public static <B> Gear<Void,B> of(Producer<B,? extends Exception> prd) {
    return null;
  }
  
  public static <A,B> Gear<A,B> of(Function<A,B> fun) {
    return null;
  }
  
  public static <A> Gear<A,Void> of(Consumer<A> csm) {
    return null;
  }
  
  
  
  public static class DefGear<I,O> implements Gear<I,O> {

    @Override
    public Running<I, O> start() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void suspend() throws InterruptedException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void suspend(long timeout) throws InterruptedException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void resume() throws InterruptedException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public boolean isReady() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void run() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  }
  
}
