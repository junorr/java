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

package com.jpower.inet;

import java.io.File;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/08/2013
 */
public class FileAttachment extends Attachment {

  private File file;
  
  
  public FileAttachment() {
    file = null;
  }
  
  
  public FileAttachment(String path) {
    setPath(path);
  }


  @Override
  public File getFile() {
    return file;
  }


  public FileAttachment setFile(File file) {
    this.file = file;
    if(file != null && file.exists())
      super.setLength(file.length());
    return this;
  }
  

  @Override
  public FileAttachment setPath(String path) {
    super.setPath(path);
    if(path != null && !path.isEmpty()) {
      if(path.contains("/") || path.contains("\\")) {
        int ie = path.lastIndexOf('/');
        if(ie < 0) ie = path.lastIndexOf('\\');
        super.setName(path.substring(ie+1));
      } else {
        super.setPath("./")
            .setName(path);
      }
      this.setFile(new File(path));
    }
    return this;
  }
  
}
