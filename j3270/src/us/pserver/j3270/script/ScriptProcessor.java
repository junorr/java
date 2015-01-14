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

package us.pserver.j3270.script;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSReflectionExtension;
import us.pserver.j3270.JDriver;
import us.pserver.psf.func.CTypeLib;
import us.pserver.psf.func.CronLib;
import us.pserver.psf.func.DateLib;
import us.pserver.psf.func.IOLib;
import us.pserver.psf.func.StrLib;
import us.pserver.psf.func.UILib;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/03/2014
 */
public class ScriptProcessor {
  
  private final BasicIO fs;
  
  private final IOLib iolib;
  
  private StrLib strlib;
  
  
  public ScriptProcessor(JDriver driver) {
    fs = new BasicIO();
    FSFastExtension funcs = new FSFastExtension();
    FSReflectionExtension ref = new FSReflectionExtension();
    
    strlib = new StrLib();
    iolib = new IOLib();
    CTypeLib ctype = new CTypeLib();
    DateLib dlib = new DateLib();
    UILib uilib = new UILib();
    CronLib cronlib = new CronLib(fs);
    J3270Lib tnlib = new J3270Lib(driver);
    
    tnlib.addTo(funcs);
    strlib.addTo(funcs);
    ctype.addTo(funcs);
    cronlib.addTo(funcs);
    
    fs.registerExtension(ref);
    fs.registerExtension(funcs);
    fs.registerExtension(iolib);
    fs.registerExtension(dlib);
    fs.registerExtension(uilib);
  }
  
  
  public void setStdOut(PrintStream ps) {
    strlib.setStdOut(ps);
  }
  
  
  public void closeIO() throws FSException {
    iolib.frclose();
    iolib.fwclose();
  }
  
  
  public BasicIO processor() {
    return fs;
  }
  
  
  public void loadLine(String line) throws IOException {
    fs.loadLine(line);
  }
  
  
  public void exec() throws FSException {
    try { 
      fs.setRunning(true);
      fs.cont(); 
    }
    catch(IOException e) {
      throw new FSException(e.getMessage());
    }
  }
  
  
  public void execLine(String line) throws FSException, IOException {
    fs.loadLine(line);
    try { fs.cont(); }
    catch(IOException e) {
      throw new FSException(e.getMessage());
    }
  }
  
  
  public void execScript(String script) throws FSException {
    if(script == null || script.isEmpty())
      return;
    StringReader sr = new StringReader(script);
    try { 
      fs.load(sr); 
      fs.cont();
    } catch(IOException e) {
      throw new FSException(e.getMessage());
    }
  }
  
  
  public void execFile(File f) throws FSException {
    if(f == null || f.exists())
      throw new IllegalArgumentException("File do not exists ["+ f+ "]");
    try(FileReader fr = new FileReader(f);) { 
      fs.load(fr); 
      fs.cont();
    } catch(IOException e) {
      throw new FSException(e.getMessage());
    }
  }

}
