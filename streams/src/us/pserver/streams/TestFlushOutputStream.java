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

package us.pserver.streams;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 11/08/2014
 */
public class TestFlushOutputStream {

  
  public static void main(String[] args) throws IOException {
    ProtectedOutputStream out = new ProtectedOutputStream(System.out);
    Base64OutputStream bos = new Base64OutputStream(out);
    /*
    DualOutputStream fos = new DualOutputStream(bos, out);
    
    System.out.print("* writePlain: ");
    fos.writePlain("Hello plain output".getBytes());
    System.out.println();
    
    System.out.print("* write: ");
    fos.write("(Hello B64 enc output)".getBytes());
    System.out.println();
    
    System.out.print("* flush: ");
    fos.flush();
    System.out.println();
    
    System.out.print("* forceFlush: ");
    fos.forceFlush();
    System.out.println();
    
    System.out.print("* write: ");
    fos.write("Should be plain again!!".getBytes());
    System.out.println();
    */
    
    System.out.print("* out.write: ");
    out.write("Hello plain output".getBytes());
    System.out.println();
    
    System.out.print("* b64.write: ");
    bos.write("(Hello B64 enc output)".getBytes());
    System.out.println();
    
    System.out.print("* b64.flush: ");
    bos.flush();
    System.out.println();
    
    System.out.print("* b64.close: ");
    bos.close();
    System.out.println();
    
    System.out.print("* out.write: ");
    out.write("Should be plain again!!".getBytes());
    System.out.println();
    
    System.out.print("* out.close: ");
    out.close();
    out.write("Should be plain again!!".getBytes());
    System.out.println();
  }
  
}
