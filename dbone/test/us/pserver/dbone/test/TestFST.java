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

package us.pserver.dbone.test;

import java.io.IOException;
import java.util.Date;
import org.nustaq.serialization.FSTConfiguration;
import us.pserver.tools.mapper.MappedValue;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/09/2017
 */
public class TestFST {

  
  public static void main(String[] args) throws IOException {
    FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
    MappedValue val1 = MappedValue.of("Hello");
    MappedValue val2 = MappedValue.of(11);
    AObj a = new AObj("Hello", 11, new int[]{0,1,2,3,4}, null, new Date());
    System.out.println("* val1: "+ val1);
    System.out.println("* val2: "+ val2);
    System.out.println("* a   : "+ a);
    byte[] bs1 = conf.asByteArray(val1);
    byte[] bs2 = conf.asByteArray(val2);
    byte[] bsa = conf.asByteArray(a);
    System.out.println("* bs1.length: "+ bs1.length);
    System.out.println("* bs2.length: "+ bs2.length);
    System.out.println("* bsa.length: "+ bsa.length);
    val1 = (MappedValue) conf.asObject(bs1);
    val2 = (MappedValue) conf.asObject(bs2);
    a = (AObj) conf.asObject(bsa);
    System.out.println("* val1: "+ val1);
    System.out.println("* val2: "+ val2);
    System.out.println("* a   : "+ a);
  }
  
}
