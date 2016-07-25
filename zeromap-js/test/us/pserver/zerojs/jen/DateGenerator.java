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

package us.pserver.zerojs.jen;

import java.util.Date;
import us.pserver.date.SimpleDate;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/04/2016
 */
public class DateGenerator implements Generator<Date> {

  private final IntegerGenerator gen60;
  
  private final IntegerGenerator gen12;
  
  
  public DateGenerator() {
    gen60 = new IntegerGenerator(60);
    gen12 = new IntegerGenerator(12);
  }
  
  
  @Override
  public Date generate() {
    return SimpleDate.now().date(
        gen60.generate() + gen60.generate() + 1900, //year
        gen12.generate() + 1,                       //month
        gen60.generate() / 2,                       //day
        gen12.generate() + gen12.generate(),        //hour
        gen60.generate(),                           //minute
        gen60.generate()                            //second
    );
  }
  
}
