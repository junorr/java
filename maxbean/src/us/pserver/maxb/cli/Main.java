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

package us.pserver.maxb.cli;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ParserProperties;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/02/2016
 */
public class Main {

  
  public static void main(String[] args) {
    //args = new String[]{};
    /*args = new String[]{
      "--db-driver", "com.mysql.jdbc.Driver", 
      "--db-url", "jdbc:mysql://localhost:3306",
      "--db-user", "juno",
      "--db-password", "0988",
      //"-s", "intranet",
      "-o", "./gen",
      "-k", "us.pserver.mbex",
      "-f", "./gen/intranet.properties"
      //"-t", "asnt_ctu,sit_ctu",
      //"-c", "sit_ctu.nm_usu=varchar,sit_ctu.cd_usu=char"
      //"-i"
    };*/
    MBOptions opt = new MBOptions();
    CmdLineParser cmd = new CmdLineParser(opt, ParserProperties
        .defaults().withUsageWidth(80)
    );
    try {
      System.out.println(new MBHeader());
      cmd.parseArgument(args);
      if(opt.getHelp()) {
        System.out.println(new MBHelp());
      }
      else {
        MaxBean mb = new MaxBean(opt);
        mb.execute();
      }
    }
    catch(CmdLineException ce) {
      System.err.println("# "+ ce.getMessage());
      System.err.println();
      cmd.printUsage(System.err);
    }
    catch(Exception e) {
      System.err.println("# "+ e.getMessage());
      if(e.getCause() != null) {
        System.err.println("# "+ e.getCause().getMessage());
      }
      System.err.println();
      e.printStackTrace(System.err);
    }
  }
  
}
