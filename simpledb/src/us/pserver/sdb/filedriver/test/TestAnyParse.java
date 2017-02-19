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

package us.pserver.sdb.filedriver.test;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/02/2017
 */
public class TestAnyParse {

  public static long calcMem() {
    System.gc();
    System.gc();
    System.gc();
    return Runtime.getRuntime().freeMemory();
  }
  
  
  public static void main(String[] args) throws IOException {
    Timer tm = new Timer.Nanos();
    //ByteArrayInputStream bis = new ByteArrayInputStream(
        //UTF8String.from(new JsonCreator()
            //.withDepth(4).create()).getBytes()
    //);
    String str = new JsonCreator().withDepth(4).create();
    System.out.println(str);
    long mem = calcMem();
    System.out.println("* free: "+ new FSize(mem));
    tm.start();
    JsonIterator jit = JsonIterator.parse(str);
    Any any = null;
    ValueType t = null;
    while(true) {
      t = jit.whatIsNext();
      System.out.println(t);
      if(t == ValueType.ARRAY) {
        any = jit.readAny();
        break;
      } else {
        jit.skip();
      }
    }
    tm.stop();
    mem = calcMem();
    System.out.println(tm);
    System.out.println("* free: "+ new FSize(mem));
    System.out.println(any);
  }
  
}
