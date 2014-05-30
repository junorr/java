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

package com.jpower.simplemail.internal;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;


/**
 * <p style="font-size: medium;">
 * Define uma fonte de dados (bytes) armazenados
 * em buffer.
 * </p>
 * 
 * @see javax.activation.DataSource
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "BufferedDataSource",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Fonte de dados em buffer"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class BufferedDataSource implements DataSource {

  private InfiniteBuffer buffer;
  
  private String type;
  
  private String name;
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public BufferedDataSource() {
    buffer = new InfiniteBuffer();
    type = null;
    name = null;
  }
  

  /**
   * @see javax.activation.DataSource#getInputStream() 
   */
  @Override
  public InputStream getInputStream() throws IOException {
    return buffer.getInput();
  }


  /**
   * @see javax.activation.DataSource#getOutputStream() 
   */
  @Override
  public OutputStream getOutputStream() throws IOException {
    return buffer.getOutput();
  }


  /**
   * @see javax.activation.DataSource#getContentType() 
   */
  @Override
  public String getContentType() {
    return type;
  }
  
  
  /**
   * Define o tipo (Mime-Type) dos dados.
   * @param s Mime-Type
   */
  public void setContentType(String s) {
    type = s;
  }


  /**
   * @see javax.activation.DataSource#getName() 
   */
  @Override
  public String getName() {
    return name;
  }
  
  
  /**
   * Define o nome da fonte de dados.
   * @param n Nome da fonte de dados.
   */
  public void setName(String n) {
    name = n;
  }
  
}
