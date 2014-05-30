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

package us.pserver.remote.test;

import java.io.IOException;
import us.pserver.remote.MethodInvocationException;
import us.pserver.remote.RemoteMethod;
import us.pserver.remote.RemoteObject;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 21/01/2014
 */
public class TestClient2 {

  
  public static void main(String[] args) throws IOException, MethodInvocationException {
    RemoteObject rob = new RemoteObject();
    RemoteMethod rmt = new RemoteMethod()
        .forObject("UpperEcho")
        .method("toUpperEcho")
        .argTypes(String.class)
        .args("string to be echoed in upper case.");
    
    System.out.println("* Connecting: "+ rob.getNetConnector());
    
    System.out.println("\n* Writing invocation");
    System.out.println("* "+ rmt+ " = "+ rob.invoke(rmt));
    
    
    rmt.forObject("Calculator")
        .method("sub")
        .argTypes(int.class, int.class)
        .args(300, 132);
    System.out.println("\n* Writing invocation");
    System.out.println("* "+ rmt+ " = "+ rob.invoke(rmt));
  }
  
}
