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

package us.pserver.psf.func;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSExtension;
import murlen.util.fscript.FSObject;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/01/2014
 */
public class IOLib implements FSExtension {
  
  public static final byte[] BYTES_EOF = {69, 79, 70};

  public static final String
      FRCLOSE = "frclose",
      FREAD = "fread",
      FREADBYTES = "frbytes",
      FWCLOSE = "fwclose",
      FWRITE = "fwrite",
      FWRITEBYTES = "fwbytes",
      FWRITEIMAGE = "fwimage",
      FSIZE = "fsize",
      FREMOVE = "fremove",
      FCOPY = "fcopy",
      ISEOF = "isEOF",
      IREAD = "iread",
      LENGTH = "length",
      SYSCMD = "syscommand",
      
      EOF = "EOF",
      
      KEY_LN = "LN",
      KEY_CSV = "CSV",
      KEY_SPACE = "SPACE",
      
      VAL_LN = (File.separatorChar == '/' ? "\n" : "\r\n"),
      VAL_CSV = ";",
      VAL_SPACE = " ";
  
  
  private File filein;
  
  private File fileout;
  
  private InputStream input;
  
  private OutputStream output;
  
  
  public IOLib() {
    filein = null;
    fileout = null;
    input = null;
  }
  
  
  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    switch(string) {
      case FREAD:
        FUtils.checkLen(al, 1);
        return fread(FUtils.str(al, 0), 
            (al.size() > 1 ? FUtils.str(al, 1) 
             : null));
      case IREAD:
        return iread();
      case FREADBYTES:
        FUtils.checkLen(al, 2);
        return freadBytes(FUtils.str(al, 0), FUtils._int(al, 1));
      case FWRITE:
        FUtils.checkLen(al, 2);
        fwrite(FUtils.str(al, 0), FUtils.str(al, 1));
        return null;
      case FWRITEBYTES:
        FUtils.checkLen(al, 2);
        byte[] bs = FUtils.cast(al, 1);
        fwriteBytes(FUtils.str(al, 0), bs);
        return null;
      case FWRITEIMAGE:
        FUtils.checkLen(al, 2);
        fwriteImage(FUtils.str(al, 0), al.get(1));
        return null;
      case FRCLOSE:
        frclose();
        return null;
      case FWCLOSE:
        frclose();
        return null;
      case FSIZE:
        FUtils.checkLen(al, 1);
        return fsize(FUtils.str(al, 0));
      case FREMOVE:
        FUtils.checkLen(al, 1);
        fremove(FUtils.str(al, 0));
        return 1;
      case FCOPY:
        FUtils.checkLen(al, 2);
        fcopy(FUtils.str(al, 0), FUtils.str(al, 1));
        return 1;
      case ISEOF:
        FUtils.checkLen(al, 1);
        return isEOF(al.get(0));
      case LENGTH:
        FUtils.checkLen(al, 1);
        return length(al.get(0));
      case SYSCMD:
        FUtils.checkLen(al, 1);
        return syscmd(FUtils.str(al, 0));
      default:
        throw new FSUnsupportedException();
    }
  }
  
  
  public String syscmd(String cmd) throws FSException {
    try {
      SystemRun sr = new SystemRun().parseCommand(cmd);
      sr.setPullOutput(true)
          .setWaitFor(true)
          .setOutputLimit(3500);
      sr.exec();
      return sr.getOutput();
    } catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public static String readUntil(InputStream in, String str) throws FSException {
    StringBuilder sb = new StringBuilder();
    try {
      while(!sb.toString().contains(str)) {
        char c = (char) in.read();
        if(c == -1) break;
        sb.append(c);
      }
      if(sb.length() == 0)
        return EOF;
      
      String ret = sb.toString().substring(0, sb.indexOf(str));
      if(ret.contains("\r")) ret = ret.replace("\r", "");
      return ret;
    } 
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public File checkFile(String path) throws FSException {
    if(path == null)
      throw new FSException("Invalid file ["+ path+ "]");
    File f = new File(path);
    if(!f.exists())
      throw new FSException("File do not exists ["+ path+ "]");
    return f;
  }
  
  
  public void checkInput(File f) throws FSException {
    if(f == null) 
      throw new FSException("Invalid null file ["+ f+ "]");
    try {
      if(filein == null || !filein.equals(f)
          || input == null) {
        filein = f;
        input = new FileInputStream(filein);
      }
    }
    catch(FileNotFoundException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void checkOutput(File f) throws FSException {
    if(f == null) 
      throw new FSException("Invalid null file ["+ f+ "]");
    try {
      if(fileout == null || !fileout.equals(f)
          || output == null) {
        fileout = f;
        if(!fileout.exists())
          fileout.createNewFile();
        output = new FileOutputStream(fileout);
      }
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public int fsize(String path) throws FSException {
    if(path == null)
      throw new FSException("fsize( "+ path+ "): Invalid Path");
    Path p = Paths.get(path);
    if(p == null || !Files.exists(p))
      throw new FSException("fsize( "+ path+ "): Invalid Path");
    try {
      return (int) Files.size(p);
    } catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void fcopy(String src, String dst) throws FSException {
    if(src == null)
      throw new FSException("fcopy( "+ src+ "): Invalid src Path");
    if(dst == null)
      throw new FSException("fcopy( "+ dst+ "): Invalid dst Path");
    
    Path ps = Paths.get(src);
    if(ps == null || !Files.exists(ps))
      throw new FSException("fcopy( "+ ps+ "): Invalid src Path");
    
    Path pd = Paths.get(dst);
    if(pd == null || !Files.exists(pd))
      throw new FSException("fcopy( "+ pd+ "): Invalid dst Path");
    
    try {
      if(Files.isDirectory(ps))
        fcpdir(ps, pd);
      else
        Files.copy(ps, pd, 
            StandardCopyOption.COPY_ATTRIBUTES, 
            StandardCopyOption.REPLACE_EXISTING);
    } catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void fcpdir(Path src, Path dst) throws FSException {
    if(src == null || !Files.exists(src))
      throw new FSException("fcopy( "+ src+ "): Invalid src Path");
    if(dst == null)
      throw new FSException("fcopy( "+ dst+ "): Invalid dst Path");
    if(!Files.isDirectory(src))
      return;
    
    try {
      if(!Files.exists(dst))
        Files.createDirectories(dst);
      File[] fs = src.toFile().listFiles();
      for(File f : fs) {
        if(f.isDirectory())
          fcpdir(f.toPath(), dst.resolve(f.getName()));
        else
          Files.copy(f.toPath(), dst.resolve(f.getName()), 
              StandardCopyOption.COPY_ATTRIBUTES, 
              StandardCopyOption.REPLACE_EXISTING);
      }
    } catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void fremove(String path) throws FSException {
    if(path == null)
      throw new FSException("fremove( "+ path+ "): Invalid Path");
    
    Path ps = Paths.get(path);
    if(ps == null || !Files.exists(ps))
      throw new FSException("fremove( "+ path+ "): Invalid Path");
    
    if(Files.isDirectory(ps))
      frmdir(ps);
    else
      ps.toFile().delete();
  }
  
  
  public void frmdir(Path dir) throws FSException {
    if(dir == null)
      throw new FSException("fremove( "+ dir+ "): Invalid Path");
    if(!Files.isDirectory(dir))
      return;
    
    File[] fs = dir.toFile().listFiles();
    if(fs == null) return;
    for(File f : fs) {
      if(f.isDirectory())
        frmdir(f.toPath());
      else
        f.delete();
    }
  }
  
  
  public int isEOF(Object o) {
    if(o == null) return 0;
    if(o instanceof FSObject)
      o = ((FSObject)o).getObject();
    if(o instanceof byte[]) {
      byte[] bs = (byte[]) o;
      return (bs.length == 3
          && bs[0] == BYTES_EOF[0]
          && bs[1] == BYTES_EOF[1]
          && bs[2] == BYTES_EOF[2] 
          ? 1 : 0);
    }
    return 0;
  }
      
  
  public int length(Object o) {
    if(o == null) return 0;
    if(o instanceof FSObject)
      o = ((FSObject)o).getObject();
    if(o instanceof byte[])
      return ((byte[])o).length;
    return 0;
  }
  
  
  public String iread() throws FSException {
    return readUntil(System.in, VAL_LN);
  }
  
  
  public String fread(String path, String token) throws FSException {
    this.checkInput(checkFile(path));
    if(token == null) token = VAL_LN;
    return readUntil(input, token);
  }
  
  
  public void fwrite(String path, String str) throws FSException {
    if(str == null)
      throw new FSException("Nothing to write ["+ str+ "]");
    this.checkOutput(new File(path));
    try {
      output.write(str.getBytes());
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void fwriteBytes(String path, byte[] bs) throws FSException {
    if(bs == null || bs.length == 0)
      throw new FSException("Nothing to write ["+ bs+ "]");
    this.checkOutput(new File(path));
    try {
      output.write(bs);
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void fwriteImage(String path, Object img) throws FSException {
    if(img == null)
      throw new FSException("Invalid image ["+ img+ "]");
    if(img instanceof FSObject)
      img = ((FSObject)img).getObject();
    if(!(img instanceof BufferedImage))
      throw new FSException("Invalid image ["+ img+ "]");
    
    this.checkOutput(new File(path));
    try {
      ImageIO.write((BufferedImage)img, "jpg", new File(path));
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public byte[] freadBytes(String path, int length) throws FSException {
    if(length < 1) 
      throw new FSException("Invalid length to read ["+ length+ "]");
    this.checkInput(checkFile(path));
    try {
      byte[] bs = new byte[length];
      int read = input.read(bs);
      if(read < 1) return BYTES_EOF;
      if(read < length)
        bs = Arrays.copyOf(bs, read);
      return bs;
    }
    catch(IOException e) {
      throw new FSException(e.toString());
    }
  }
  
  
  public void frclose() throws FSException {
    if(input == null) return;
    try {
      input.close();
      input = null;
    } catch(IOException e) {
      throw new FSException(e.toString());
    }
  }


  public void fwclose() throws FSException {
    if(output == null) return;
    try {
      output.close();
      output = null;
    } catch(IOException e) {
      throw new FSException(e.toString());
    }
  }


  @Override
  public Object getVar(String string) throws FSException {
    switch(string) {
      case KEY_LN:
        return VAL_LN;
      case KEY_CSV:
        return VAL_CSV;
      case KEY_SPACE:
        return VAL_SPACE;
      case EOF:
        return EOF;
      default:
        throw new FSUnsupportedException();
    }
  }


  @Override
  public void setVar(String string, Object o) throws FSException {
    throw new FSException("Read only var ["+ string+ "]");
  }


  @Override
  public Object getVar(String string, Object o) throws FSException {
    return getVar(string);
  }


  @Override
  public void setVar(String string, Object o, Object o1) throws FSException {
    setVar(string, o);
  }
  
  
  
  public static void main(String[] args) throws FSException {
    IOLib io = new IOLib();
    System.out.println(io.syscmd("cmd /c dir d:"));
  }
  
}
