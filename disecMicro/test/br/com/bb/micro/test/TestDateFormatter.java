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

package br.com.bb.micro.test;

import br.com.bb.disec.micro.util.DateFormatter;
import br.com.bb.disec.micro.util.DateParser;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 31/08/2016
 */
public class TestDateFormatter {

  
  public static void main(String[] args) {
    String str = "2016-08-31T17:38:40Z";
    DateParser dtp = DateParser.parser(str);
    System.out.println("* string: "+ str);
    System.out.println("* parsed: "+ dtp.toInstant());
    System.out.println("* format: "+ DateFormatter.of(dtp.toInstant()).toIsoDateTime());
    System.out.println();
    
    str = "2016-08-31";
    dtp = DateParser.parser(str);
    System.out.println("* string: "+ str);
    System.out.println("* parsed: "+ DateParser.parser(str).toInstant());
    System.out.println("* format: "+ DateFormatter.of(dtp.toInstant()).toIsoDate());
    System.out.println();
    
    str = "31/08/2016 17:38:40";
    dtp = DateParser.parser(str);
    System.out.println("* string: "+ str);
    System.out.println("* parsed: "+ DateParser.parser(str).toTimestamp());
    System.out.println("* format: "+ DateFormatter.of(dtp.toInstant()).toBRDateTime());
    System.out.println();
    
    str = "31/08/2016";
    dtp = DateParser.parser(str);
    System.out.println("* string: "+ str);
    System.out.println("* parsed: "+ DateParser.parser(str).toSqlDate());
    System.out.println("* format: "+ DateFormatter.of(dtp.toInstant()).toBRDate());
  }
  
}
