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

package tests.of.tests;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import oodb.tests.beans.IFSize.Unit;
import static oodb.tests.beans.IFSize.Unit.BYTE;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/12/2016
 */
public class TestUnit {

  
  public static void main(String[] args) {
    NumberFormat nf = new DecimalFormat("#,##0");
    Unit unit = BYTE;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
    unit = Unit.KB;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
    unit = Unit.MB;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
    unit = Unit.GB;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
    unit = Unit.TB;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
    unit = Unit.PB;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
    unit = Unit.EB;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
    unit = Unit.MAX;
    System.out.println(unit.name() + "=" + nf.format(unit.bytes()));
  }
  
}
