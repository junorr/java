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
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.streams.DynamicBuffer;
import us.pserver.streams.SequenceInputStream;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 16/06/2015
 */
public class TestDynamicBuffer {

  
  public static void main(String[] args) throws IOException {
    SequenceInputStream in = new SequenceInputStream(100);
    DynamicBuffer buffer = new DynamicBuffer(10);
    CryptKey key = CryptKey.createRandomKey(CryptAlgorithm.AES_CBC_PKCS5);
    buffer
        //.size()
        .setGZipCoderEnabled(true)
        //.setCryptCoderEnabled(true, key)
        //.setBase64CoderEnabled(true)
        ;
    //OutputStream os = buffer.getOutputStream();
    OutputStream os = buffer.getEncoderStream();
    int read = -1;
    byte[] bs = new byte[7];
    while((read = in.read(bs)) > 0) {
      os.write(bs, 0, read);
    }
    os.close();
    
    List<Integer> list = new LinkedList<>();
    InputStream is = buffer.getInputStream();
    while((read = is.read()) != -1) {
      list.add(read);
    }
    System.out.println("* Sequence: "+ Arrays.toString(list.toArray()));
    System.out.println("* buffer.getUsedPages() = "+ buffer.getUsedPages());
    System.out.println("* buffer.size() = "+ buffer.size());
    
    
    System.out.println("* Test buffer reuse by buffer.reset()");
    in = new SequenceInputStream(100);
    buffer.reset();
    os = buffer.getEncoderStream();
    read = -1;
    bs = new byte[7];
    while((read = in.read(bs)) > 0) {
      os.write(bs, 0, read);
    }
    os.close();
    
    list.clear();
    is = buffer.getInputStream();
    while((read = is.read()) != -1) {
      list.add(read);
    }
    System.out.println("* Sequence: "+ Arrays.toString(list.toArray()));
    System.out.println("* buffer.getUsedPages() = "+ buffer.getUsedPages());
    System.out.println("* buffer.size() = "+ buffer.size());

    
    list.clear();
    is = buffer.rewind().getDecoderStream();
    while((read = is.read()) != -1) {
      list.add(read);
    }
    System.out.println("* list.size() = "+ list.size());
    System.out.println("* Sequence: "+ Arrays.toString(list.toArray()));
    is.close();
  }
  
}
