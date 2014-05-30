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
import us.pserver.psf.func.IOLib;
import us.pserver.psf.func.ImportScriptLib;
import us.pserver.psf.func.StrLib;
import us.pserver.psf.func.TN3270Lib;
import us.pserver.psf.func.UILib;
import us.pserver.scronv6.SCronV6;

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
  
  
  public ScriptProcessor() {
    init(null);
  }
  
  
  public ScriptProcessor(SCronV6 cron) {
    init(cron);
  }
  
  
  private void init(SCronV6 cron) {
    fs = new BasicIO();
    FSFastExtension funcs = new FSFastExtension();
    FSReflectionExtension ref = new FSReflectionExtension();
    
    if(cron != null)
      cronlib = new CronLib(fs, cron);
    else
      cronlib = new CronLib(fs);
    strlib = new StrLib();
    iolib = new IOLib();
    CTypeLib ctype = new CTypeLib();
    DateLib dlib = new DateLib();
    UILib uilib = new UILib();
    ImportScriptLib implib = new ImportScriptLib(fs);
    tnlib = new TN3270Lib();
    
    tnlib.addTo(funcs);
    strlib.addTo(funcs);
    ctype.addTo(funcs);
    cronlib.addTo(funcs);
    implib.addTo(funcs);
    
    fs.registerExtension(ref);
    fs.registerExtension(funcs);
    fs.registerExtension(iolib);
    fs.registerExtension(dlib);
    fs.registerExtension(uilib);
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
        System.out.println("\n>> exit( "+ spr.execFile(args[0])+ " )");
      }
    } catch(FSException | IOException e) {
      e.printStackTrace();
    } finally {
      spr.finish();
    }
  }
  
}
