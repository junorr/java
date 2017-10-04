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

package us.pserver.ironbit.test;

import java.util.Arrays;
import java.util.List;
import us.pserver.ironbit.record.ComposeableSerialRecord;
import us.pserver.ironbit.record.DefaultSerialRecord;
import us.pserver.ironbit.record.SerialRecord;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/10/2017
 */
public class TestComposeableSerialRecord {

  
  public static void main(String[] args) {
    SerialRecord<Integer> sr1 = new DefaultSerialRecord("i1", 1);
    SerialRecord<Integer> sr2 = new DefaultSerialRecord("i2", 2);
    ComposeableSerialRecord<List<SerialRecord<Integer>>> comp = 
        new ComposeableSerialRecord("compose", Arrays.asList(sr1, sr2));
    System.out.println(sr1);
    System.out.println(Arrays.toString(sr1.toByteArray()));
    System.out.println(sr2);
    System.out.println(Arrays.toString(sr2.toByteArray()));
    System.out.println(comp);
    System.out.println(Arrays.toString(comp.toByteArray()));
    System.out.println(comp.getValue());
  }
  
}
