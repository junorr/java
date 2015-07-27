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

package us.pserver.xprops.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/07/2015
 */
public class TDate extends AbstractXmlTransformer<Date> {
  
  private static final String format = "yyy-MM-dd HH:mm:ss.SSS";
  
  private final SimpleDateFormat sdf;
  
  
  public TDate(String format) {
    sdf = new SimpleDateFormat(Valid.off(format)
        .getOrFail("Invalid Date Format: ")
    );
  }
  
  
  public TDate() {
    this(format);
  }
  
  
  public DateFormat dateFormat() {
    return sdf;
  }
  
  
  @Override
  public Date apply(String str) throws IllegalArgumentException {
    try {
      return sdf.parse(Valid.off(str)
          .getOrFail("Invalid String to Transform: "+ str)
      );
    } catch(ParseException e) {
      throw new IllegalArgumentException(e.toString(), e);
    }
  }


  @Override
  public String back(Date dt) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return df.format(dt);
  }
  
}
