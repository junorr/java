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

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import oodb.tests.beans.FSize;
import oodb.tests.beans.IFPath;
import oodb.tests.beans.StringPad;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/12/2016
 */
public class TestMappedBuffer {

  
  public static void main(String[] args) throws IOException, InterruptedException {
    Path path = Paths.get("/storage/backup/systems/windows8-new-sas.fsa");
    IFPath fpath = IFPath.from(path);
    System.out.println(fpath);
    System.out.print(StringPad.of("* Opening FileChannel...").rpad(" ", 50));
    FileChannel ch = FileChannel.open(path, StandardOpenOption.READ);
    System.out.println("[OK]");
    List<MappedByteBuffer> bufs = new ArrayList<>();
    long pos = 0;
    long len = fpath.size().bytes();
      System.out.println(StringPad.of(
          StringPad.of("* Size Remaining:").rpad(" ", 30) 
          + StringPad.of(new FSize(len).toString()).lpad(" ", 15)).rpad(" ", 50));
    while(len > 0) {
      int min = (int) Math.min(Integer.MAX_VALUE, len);
      System.out.print(StringPad.of(
          StringPad.of("* Allocating Buffer").rpad(" ", 30) 
          + StringPad.of(new FSize(min).toString()).lpad(" ", 15)
          + "...").rpad(" ", 50));
      MappedByteBuffer buf = ch.map(FileChannel.MapMode.READ_ONLY, pos, min);
      System.out.println("[OK]");
      bufs.add(buf);
      pos += min;
      len -= min;
      System.out.println(StringPad.of(
          StringPad.of("* Size Remaining:").rpad(" ", 30) 
          + StringPad.of(new FSize(len).toString()).lpad(" ", 15)).rpad(" ", 50));
    }
    ch.close();
    bufs.clear();
    System.out.println("* Everithing OK!");
    System.out.print(StringPad.of("* Sleeping for 10 sec...").rpad(" ", 50));
    Thread.sleep(10000);
    System.out.println("[OK]");
  }
  
}
