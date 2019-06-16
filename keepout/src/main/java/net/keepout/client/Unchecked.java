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

package net.keepout.client;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 03/01/2019
 */
public class Unchecked {

    /**
     * throws {@code exception} as call exception, without wrapping exception.
     *
     * @return will never return anything, return type is set to {@code exception} only to be able to write <code>throw call(exception)</code>
     * @throws T {@code exception} as call exception
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T unchecked(Throwable exception) throws T {
        throw (T) exception;
    }
    
    
    @FunctionalInterface
    public interface UncheckedSupplier<R> {
        R get() throws Throwable;
    }

    /**
     * Executes given function, catches and rethrows checked exceptions as call exceptions, without wrapping exception.
     * @return result of function
     * @see #unchecked(Exception)
     */
    public static <R> R call(UncheckedSupplier<R> supplier) {
        try {
            return supplier.get();
        } catch (Throwable e) {
            throw Unchecked.<RuntimeException>unchecked(e);
        }
    }


    @FunctionalInterface
    public interface UncheckedCall {
        void call() throws Throwable;
    }

    /**
     * Executes given method, catches and rethrows checked exceptions as call exceptions, without wrapping exception.
     * @see #unchecked(Exception)
     */
    public static void call(UncheckedCall method) {
      try {
        method.call();
      } catch (Throwable e) {
        throw Unchecked.<RuntimeException>unchecked(e);
      }
    }

}