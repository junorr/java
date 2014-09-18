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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import murlen.util.fscript.BasicIO;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSFastExtension;
import murlen.util.fscript.FSFunctionExtension;
import murlen.util.fscript.FSUnsupportedException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 25/03/2014
 */
public class ImportScriptLib implements FSFunctionExtension {

  public static final String IMPORT = "import";
  
  
  private BasicIO fs;
  
  
  public ImportScriptLib(BasicIO fs) {
    if(fs == null)
      throw new IllegalArgumentException(
          "Invalid BasicIO script interpreter ["+ fs+ "]");
    this.fs = fs;
  }


  @Override
  public Object callFunction(String string, ArrayList al) throws FSException {
    if(!IMPORT.equals(string))
      throw new FSUnsupportedException(
          "Invalid command ["+ string+ "]");
    
    FUtils.checkLen(al, 1);
    File f = new File(FUtils.str(al, 0));
    if(!f.exists())
      throw new FSUnsupportedException(
          "File to import not exists ["+ f.toString()+ "]");
    
    try(FileReader fr = new FileReader(f);) {
      fs.load(fr);
    } catch(IOException e) {
      throw new FSException(e.getMessage());
    }
    return null;
  }
  
  
  public void addTo(FSFastExtension ext) {
    if(ext == null) return;
    ext.addFunctionExtension(IMPORT, this);
  }
  
}
