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

package us.pserver.redfs;

import com.jpower.date.DateDiff;
import com.jpower.date.SimpleDate;
import com.jpower.log.LogProvider;
import com.jpower.log.Logger;
import java.nio.file.Paths;
import java.util.List;
import us.pserver.rob.NetConnector;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 28/11/2013
 */
public class TestClient2 {

  
  public static void main(String[] args) throws Exception {
    RemoteFileSystem rfs = new RemoteFileSystem(
        //new NetConnector("172.24.75.19", 11011)
        //new NetConnector("10.100.0.102", 11011)
        new NetConnector()
        .setAutoCloseConnetcion(false));
    
    Logger log = LogProvider.getLogger();
    log.info("* current = "+ rfs.getCurrent());
    log.info("* cd [/home/juno]="+ rfs.cd("/home/juno"));
    //log.info("* cd [Downloads]="+ rfs.cd("Downloads"));
    //log.info("* cd [Talitah Badra]="+ rfs.cd("Talitah Badra"));
    //log.info("* cd [d:/]="+ rfs.cd("d:/"));
    
    log.info("* remote.ls");
    List<RemoteFile> ls = rfs.ls();
    for(RemoteFile rf : ls) {
      log.info("  - "+ rf);
    }
    
    System.out.println();
    //RemoteFile r = rfs.getFile("ubuntu.zip");
    //RemoteFile r = rfs.getFile("C:/Users/Talitah Badra/Downloads/S0mbras.da.Noite.omelhordatelona.biz.rmvb");
    //RemoteFile r = new RemoteFile("apps/bitkinex323.exe");
    RemoteFile r = new RemoteFile("/media/warehouse/iso/SYSRESTORE.zip");
    log.info("r = "+ r);
    SimpleDate sd = new SimpleDate();
    int CON = 1;
    IOData data = new IOData()
        .setRemoteFile(r)
        .addListener(new SimpleListener())
        .setPath(Paths.get("/home/juno/copy"));
    System.out.println("* start = "+ sd.format(SimpleDate.HHMMSSNNN));
    //System.out.println("copyAsync( "+ r+ ", /home/juno, "+ CON+ " ): "+
      //  rfs.copyAsync(r, "/home/juno", CON, null));
    System.out.println("copyAsync( "+ r+ ", d:/, "+ CON+ " ): "+
        rfs.asyncCopyFile(data, CON));
    //System.out.println("readFile( "+ r+ ", d:/ ): "+
      //  rfs.readFile(r, "/home/juno"));
    SimpleDate ed = new SimpleDate();
    System.out.println("* end = "+ ed.format(SimpleDate.HHMMSSNNN));
    System.out.println("* elapsed = "+ new DateDiff(sd, ed));
  }
  
}
