/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package us.pserver.smail.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * <p style="font-size: medium;">
 * Classe abstrata que implementa funcionalidades 
 * comuns de objetos embarcados no email.
 * </p>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public abstract class AbstractEmbeddedObject implements EmbeddedObject {

  protected String name;
  
  protected String type;
  
  protected String description;
  

  @Override
  public String getName() {
    return name;
  }


  @Override
  public void setName(String name) {
    this.name = name;
  }


  @Override
  public String getType() {
    return type;
  }


  @Override
  public void setType(String type) {
    this.type = type;
  }
  

  @Override
  public String getDescription() {
    return description;
  }


  @Override
  public void setDescription(String desc) {
    this.description = desc;
  }
  

  @Override
  public void saveTo(InputStream input, File f) throws IOException {
    if(input == null) throw new IOException(
        "{EmbeddedObject.saveTo( InputStream, File )}: Invalid InputStream.");
    
    FileOutputStream out = new FileOutputStream(f);
    byte[] buf = new byte[1024];
    int read = 0;
    while((read = input.read(buf)) > 0) {
      out.write(buf, 0, read);
    }
    out.flush();
    out.close();
    input.close();
  }
  
}
