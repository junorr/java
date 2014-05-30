/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca Ã© software livre; vocÃª pode redistribuÃ­-la e/ou modificÃ¡-la sob os
 * termos da LicenÃ§a PÃºblica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versÃ£o 2.1 da LicenÃ§a, ou qualquer
 * versÃ£o posterior.
 * 
 * Esta biblioteca Ã© distribuÃ­da na expectativa de que seja Ãºtil, porÃ©m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implÃ­cita de COMERCIABILIDADE
 * OU ADEQUAÃ‡ÃƒO A UMA FINALIDADE ESPECÃ�FICA. Consulte a LicenÃ§a PÃºblica
 * Geral Menor do GNU para mais detalhes.
 * 
 * VocÃª deve ter recebido uma cÃ³pia da LicenÃ§a PÃºblica Geral Menor do GNU junto
 * com esta biblioteca; se nÃ£o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereÃ§o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/04/2014
 */
public class FileOutputFactory {
  
  private String sfile;
  
  private File file;
  
  private Path pfile;
    
  
  public FileOutputFactory(String sfl) {
    if(sfl == null)
      throw new IllegalArgumentException(
          "Invalid file name: "+ sfl);
    sfile = sfl;
  }
    
  
  public FileOutputFactory(File fl) {
    if(fl == null)
      throw new IllegalArgumentException(
          "Invalid file: "+ fl);
    file = fl;
  }
    
  
  public FileOutputFactory(Path pfl) {
    if(pfl == null)
      throw new IllegalArgumentException(
          "Invalid file path: "+ pfl);
    pfile = pfl;
  }
    
  
  public PrintStream create() {
    if(sfile == null && file == null
        && pfile == null)
      throw new IllegalStateException(
          "Null Arguments in FileStreamFactory");
    
    try {
      if(sfile != null) {
        return new PrintStream(
            Files.newOutputStream(Paths.get(sfile), 
            StandardOpenOption.APPEND, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.SYNC));
      }
      else if(file != null) {
        return new PrintStream(
            Files.newOutputStream(file.toPath(), 
            StandardOpenOption.APPEND, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.SYNC));
      }
      else {
        return new PrintStream(
            Files.newOutputStream(pfile, 
            StandardOpenOption.APPEND, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.SYNC));
      }
    } 
    catch(IOException e) {
      throw new IllegalArgumentException(
          "Error creating FileOutputStream: "+ e.toString());
    }
  }
  
}
