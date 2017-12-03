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

package us.pserver.dbone.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 29/11/2017
 */
public class TestRegex {

  
  public static void main(String[] args) {
    String url1 = "http://localhost.bb.com.br:8086/fale/exiba?cmdo=fale.indexCESUP&ogm=CESUP";
    String url2 = "http://localhost.bb.com.br:8086/fale/exiba?cmdo=fale.FiltroMsgGestao";
    String rgx = "cmdo=fale.index*";
    Pattern pt = Pattern.compile(rgx);
    Matcher mt = pt.matcher(url1);
    while(mt.find()) {
      System.out.println(url1.substring(mt.start(), mt.end()));
    }
  }
  
}
