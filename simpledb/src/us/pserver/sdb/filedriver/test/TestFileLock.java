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

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.tools.StringPad;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 19/12/2016
 */
public class TestFileLock {

  
  public static void main(String[] args) throws IOException, InterruptedException {
    //Path path = Paths.get("d:/test.lok");
    //Path path = Paths.get("/home/juno/consultaspdf.zip");
    Path path = Paths.get("/storage/fases_sislog.xls");
    System.out.print(StringPad.of("* Opening File "+ path+ "...").rpad(" ", 50));
    FileChannel ch = FileChannel.open(path, 
        StandardOpenOption.CREATE, 
        StandardOpenOption.READ, 
        StandardOpenOption.WRITE
    );
    System.out.println("[OK]");
    FileLock rlock = null;
    FileLock wlock = null;
    FileLock r2lock = null;
    
    long start = 0;
    long length = 1024;
    System.out.print(StringPad.of("* Acquiring Shared Lock For "+ start+ "-"+ length+ "...").rpad(" ", 50));
    Timer tm = new Timer.Nanos().start();
    rlock = ch.lock(start, length, true);
    System.out.println("[OK]\n   "+ tm.stop());

    //start = 1024;
    //System.out.print(StringPad.of("* Acquiring Exclusive Lock For "+ start+ "-"+ length+ "...").rpad(" ", 50));
    //tm.clear().start();
    //wlock = ch.lock(start, length, false);
    //System.out.println("[OK]\n   "+ tm.stop());
//
    start = 1280;
    System.out.print(StringPad.of("* Acquiring Shared Lock For "+ start+ "-"+ length+ "...").rpad(" ", 50));
    tm.clear().start();
    ch.lock(start, length, true);
    System.out.println("[OK]\n   "+ tm.stop());
    
    long time = 20000;
    System.out.print(StringPad.of("* Slepping For "+ time+ " ms...").rpad(" ", 50));
    Thread.sleep(20000);
    System.out.println("[OK]");

    if(rlock != null) rlock.release();
    if(r2lock != null) r2lock.release();
    if(wlock != null) wlock.release();
  }
  
}
