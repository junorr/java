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

package us.pserver.smail;

import us.pserver.smail.internal.AbstractEmbeddedObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * <p style="font-size: medium;">
 * Representa um anexo de email.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class Attachment extends AbstractEmbeddedObject {
  
  private String desc;
  
  private InputStream input;
  
  private File file;
  
  
  /**
   * Construtor padrão sem argumentos.
   */
  public Attachment() {
    desc = null;
    type = "Application/Octet-Stream";
    input = null;
    file = null;
  }
  
  
  /**
   * Construtor que recebe o arquivo a ser anexado.
   * @param f Arquivo a ser anexado.
   */
  public Attachment(File f) {
    this();
    this.setFile(f);
  }
  
  
  /**
   * Retorna o arquivo anexo.
   * @return Arquivo anexo.
   */
  public File getFile() {
    return file;
  }


  /**
   * Define o arquivo anexo.
   * @param file Arquivo anexo. 
   */
  public void setFile(File file) {
    this.file = file;
    if(file != null)
      name = file.getName();
  }


  /**
   * Retorna um InputStream do conteúdo anexo.
   * @return InputStream do conteúdo anexo.
   */
  public InputStream getInput() {
    if(input == null && file != null && file.exists())
      try {
        return new FileInputStream(file);
      } catch(FileNotFoundException ex) {
        return null;
      }
    return input;
  }
  
  
  /**
   * Define um InputStream do conteúdo anexo.
   * @param in InputStream do conteúdo anexo.
   */
  public void setInput(InputStream in) {
    input = in;
  }
  
  
  /**
   * Salva o conteúdo do anexo no arquivo informado.
   * @param f Arquivo onde será salvo o conteúdo do anexo.
   * @throws IOException Caso ocorra erro ao salvar o anexo.
   */
  @Override
  public void saveTo(File f) throws IOException { 
    if(this.file == null && this.input == null)
      throw new IOException("{Attachment.saveTo( File )}: "
          + "Invalid data source (File/InputStream).");
    if(this.file != null && this.file.exists() 
        && !this.file.equals(f))
      this.saveTo(new FileInputStream(this.file), f);
    else
      this.saveTo(input, f);
  }

}
