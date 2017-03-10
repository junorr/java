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

package us.pserver.jose.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.tools.UTF8String;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 10/03/2017
 */
public class TestTroughput {

  
  public static void main(String[] args) throws IOException {
    final int limit = 1024*1024*100;
    System.out.println("* Write 100Mb into ByteArrayOutputStream:");
    Timer tm = new Timer.Nanos().start();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte b = 0;
    for(int i = 0; i < limit; i++) {
      bos.write(++b);
    }
    tm.lap();
    System.out.println("* remaining(): "+ bos.size());
    String str = bos.toString();
    System.out.println("  "+ tm.stop());
    System.out.println("-------------------- str.length(): "+ str.length());
    
    System.out.println("* Write 100Mb into ByteBuffer:");
    tm.clear().start();
    ByteBuffer bb = ByteBuffer.allocate(limit);
    b = 0;
    for(int i = 0; i < limit; i++) {
      bb.put(++b);
    }
    tm.lap();
    bb.flip();
    System.out.println("* remaining(): "+ bb.remaining());
    str = StandardCharsets.UTF_8.decode(bb).toString();
    System.out.println("  "+ tm.stop());
    System.out.println("-------------------- str.length(): "+ str.length());
    
    System.out.println("* Write 100Mb into BufferCollection:");
    tm.clear().start();
    BufferCollection ms = BufferCollection.create();
    bb = ByteBuffer.allocate(4096);
    b = 0;
    for(int i = 0; i < limit; i++) {
      if(!bb.hasRemaining()) {
        bb.flip();
        ms.put(bb);
        bb.compact();
      }
      bb.put(++b);
    }
    tm.lap();
    ms.flip();
    str = ms.getUTF8();
    System.out.println("  "+ tm.stop());
    System.out.println("-------------------- str.length(): "+ str.length());
    
    System.out.println("* Write 100Mb into FileChannel:");
    tm.clear().start();
    ByteBuffer buf = ByteBuffer.allocate(4096);
    Path path = Paths.get("/storage/tmp_channel.test");
    SeekableByteChannel ch = Files.newByteChannel(
        path, 
        StandardOpenOption.WRITE, 
        //StandardOpenOption.DSYNC,
        StandardOpenOption.CREATE
    );
    b = 0;
    for(int i = 0; i < limit; i++) {
      if(!buf.hasRemaining()) {
        buf.flip();
        ch.write(buf);
        buf.compact();
      }
      buf.put(++b);
    }
    ch.close();
    tm.lap();
    str = UTF8String.from(Files.readAllBytes(path)).toString();
    System.out.println("  "+ tm.stop());
    System.out.println("-------------------- str.length(): "+ str.length());
  }
  
}
