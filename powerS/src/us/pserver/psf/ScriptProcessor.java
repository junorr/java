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

package us.pserver.psf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSReflectionExtension;
import us.pserver.psf.func.CTypeLib;
import us.pserver.psf.func.CronLib;
import us.pserver.psf.func.DateLib;
import us.pserver.psf.func.Globals;
import us.pserver.psf.func.IOLib;
import us.pserver.psf.func.ListLib;
import us.pserver.psf.func.MysqlLib;
import us.pserver.psf.func.NetLib;
import us.pserver.psf.func.StrLib;
import us.pserver.psf.func.TN3270Lib;
import us.pserver.psf.func.UILib;
import us.pserver.scron.SCron;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/01/2014
 */
public class ScriptProcessor {
  
  public static final String QUIT = "quit";
  
  
  private BasicIO fs;
  
  private IOLib iolib;
  
  private TN3270Lib tnlib;
  
  private StrLib strlib;
  
  private CronLib cronlib;
  
  private NetLib netlib;
  
  
  public ScriptProcessor() {
    init(null);
  }
  
  
  public ScriptProcessor(SCron cron) {
    init(cron);
  }
  
  
  private void init(SCron cron) {
    fs = new BasicIO();
    FSFastExtension funcs = new FSFastExtension();
    FSReflectionExtension ref = new FSReflectionExtension();
    
    if(cron != null)
      cronlib = new CronLib(fs, cron);
    else
      cronlib = new CronLib(fs);
    strlib = new StrLib();
    iolib = new IOLib();
    netlib = new NetLib(fs);
    CTypeLib ctype = new CTypeLib();
    DateLib dlib = new DateLib();
    UILib uilib = new UILib(fs);
    tnlib = new TN3270Lib();
    Globals glob = new Globals();
    MysqlLib mlib = new MysqlLib();
    ListLib lslib = new ListLib(fs);
    
    glob.addTo(funcs);
    cronlib.addTo(funcs);
    strlib.addTo(funcs);
    iolib.addTo(funcs);
    netlib.addTo(funcs);
    dlib.addTo(funcs);
    uilib.addTo(funcs);
    tnlib.addTo(funcs);
    mlib.addTo(funcs);
    lslib.addTo(funcs);
    
    fs.registerExtension(ref);
    fs.registerExtension(funcs);
    fs.registerExtension(ctype);
  }
  
  
  public void setStdOut(PrintStream ps) {
    strlib.setStdOut(ps);
  }
  
  
  public BasicIO executor() {
    return fs;
  }
  
  
  public TN3270Lib getTN3270Lib() {
    return tnlib;
  }
  
  
  public IOLib getIOLib() {
    return iolib;
  }
  
  
  public void finish() {
    try {
      iolib.frclose();
      iolib.fwclose();
      tnlib.session().close();
    } catch(Exception e) {}
  }
  
  
  public void printWelcome() {
    System.out.println(" Welcome to PowerScript");
    System.out.println("    Interactive Shell");
    System.out.println("-------------------------");
    System.out.println("  (type \"quit\" to exit)");
  }
  
  
  public void interactiveShell() throws IOException, FSException {
    printWelcome();
    System.out.println();
    
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));
    String line = "";
    while(!line.equals(QUIT)) {
      System.out.print("[pfs]>> ");
      line = reader.readLine();
      boolean process = false;
      if(line.endsWith(";")) {
        process = true;
        line = line.substring(0, line.length()-1);
      }
      fs.loadLine(line);
      if(process) {
        fs.cont();
        System.out.println();
      }
    }
  }
  
  
  public Object execFile(String path) throws IOException, FSException {
    fs.reset();
    fs.load(new FileReader(path));
    return fs.run();
  }

  
  public static void main(String[] args) throws IOException, FSException {
    ScriptProcessor spr = new ScriptProcessor();
    
    try {
      if(args == null || args.length < 1) {
        spr.interactiveShell();
      }
      else {
        Object ret = spr.execFile(args[0]);
        if(ret != null)
          System.out.println(ret);
      }
    } catch(FSException | IOException e) {
      e.printStackTrace();
    } finally {
      spr.finish();
    }
  }
  
}
