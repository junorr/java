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

package us.pserver.zip.test;

import java.io.IOException;
import java.util.zip.ZipEntry;
import us.pserver.listener.SimpleListener;
import us.pserver.zip.Unzip;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 15/08/2014
 */
public class TestUnzip {
  
  
  public static void print(ZipEntry ze) {
    System.out.println("* Zip ["+ ze.getName()+ "]: compressed="+ ze.getCompressedSize()+ ", size="+ ze.getSize()+ ", crc="+ ze.getCrc()+ ", isdir="+ ze.isDirectory());
  }

  
  public static void main(String[] args) throws IOException {
    Unzip uz = new Unzip();
    
    uz.input("c:/.local/test.zip");
    uz.output("c:/.local");
    
    //uz.input("c:/.local/cp_resid.zip");
    //uz.output("c:/.local/cp_resid.pdf");
    
    uz.listEntries().forEach(TestUnzip::print);
    uz.addListener(new SimpleListener());
    uz.run();
  }
  
}
