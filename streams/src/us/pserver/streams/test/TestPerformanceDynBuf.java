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
import java.io.OutputStream;
import us.pserver.streams.DynamicBuffer;
import us.pserver.streams.IO;
import us.pserver.streams.SequenceInputStream;
import us.pserver.streams.StreamCoderFactory;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 18/06/2015
 */
public class TestPerformanceDynBuf {

  
  public static void main(String[] args) throws IOException {
    System.out.println("* Using 4096 transfer buffer and 1.1MB in memory buffer");
    IO.BUFFER_SIZE = 4096;
    int membuf = 1024*1024+10;
    DynamicBuffer buffer = new DynamicBuffer(membuf);
    SequenceInputStream sin = new SequenceInputStream(1024*1024);
    OutputStream os = buffer.getOutputStream();
    System.out.println("* writing 1MB in memory plain data");
    long start = System.nanoTime();
    IO.tc(sin, os);
    long end = System.nanoTime();
    System.out.println("* Time: "+ (end - start)/1000000.0+ " ms");
    
    os = IO.uos(IO.p("/home/juno/sequence.dat"));
    sin = new SequenceInputStream(1024*1024);
    System.out.println("* writing 1MB file plain data");
    start = System.nanoTime();
    IO.tc(sin, os);
    end = System.nanoTime();
    System.out.println("* Time: "+ (end - start)/1000000.0+ " ms");
    
    buffer = new DynamicBuffer(membuf);
    buffer.setGZipCoderEnabled(true);
    os = buffer.getEncoderStream();
    sin = new SequenceInputStream(1024*1024);
    System.out.println("* writing 1MB in memory gzipped data");
    start = System.nanoTime();
    IO.tc(sin, os);
    end = System.nanoTime();
    System.out.println("* Time: "+ (end - start)/1000000.0+ " ms");
    
    StreamCoderFactory fact = StreamCoderFactory.getNew();
    fact.setGZipCoderEnabled(true);
    os = fact.create(IO.os(IO.p("/home/juno/sequence.gz")));
    sin = new SequenceInputStream(1024*1024);
    System.out.println("* writing 1MB file gzipped data");
    start = System.nanoTime();
    IO.tc(sin, os);
    end = System.nanoTime();
    System.out.println("* Time: "+ (end - start)/1000000.0+ " ms");
    
  }
  
}
