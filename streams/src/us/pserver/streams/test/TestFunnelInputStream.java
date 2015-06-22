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

package us.pserver.streams.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import us.pserver.streams.DynamicBuffer;
import us.pserver.streams.FunnelInputStream;
import us.pserver.streams.SequenceInputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/06/2015
 */
public class TestFunnelInputStream {

  
  public static void main(String[] args) throws IOException {
    SequenceInputStream i0 = new SequenceInputStream(0, 10);
    System.out.println("i0.available() = "+ i0.available());
    SequenceInputStream i10 = new SequenceInputStream(10, 20);
    System.out.println("i10.available() = "+ i10.available());
    SequenceInputStream i20 = new SequenceInputStream(20, 30);
    System.out.println("i20.available() = "+ i20.available());
    FunnelInputStream is = new FunnelInputStream();
    is.append(i0).append(i10).append(i20);
    
    DynamicBuffer buffer = new DynamicBuffer(10);
    OutputStream os = buffer.getOutputStream();
    int read = -1;
    byte[] bs = new byte[7];
    while((read = is.read(bs)) > 0) {
      os.write(bs, 0, read);
    }
    
    List<Integer> list = new LinkedList<>();
    InputStream in = buffer.getInputStream();
    while((read = in.read()) != -1) {
      list.add(read);
    }
    System.out.println("* List.size() = "+ list.size());
    System.out.println("* Sequence: "+ Arrays.toString(list.toArray()));
    System.out.println("* listBuffer.getUsedPages() = "+ buffer.getUsedPages());
  }
  
}
