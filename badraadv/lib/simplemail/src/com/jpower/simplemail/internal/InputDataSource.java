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
 * Implementa uma fonte de dados de um InputStream.
 * </p>
 * 
 * @see java.io.InputStream
 * @see javax.activation.DataSource
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "InputDataSource",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Fonte de dados de um InputStream"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class InputDataSource implements DataSource {

  private InputStream input;
  
  private String contentType;
  
  private String name;
  
  
  /**
   * Construtor padrão que recebe o InputStream
   * de leitura dos dados.
   * @param in InputStream de onde serão lidos os dados.
   */
  public InputDataSource(InputStream in) {
    if(in == null) throw 
        new IllegalArgumentException(
            "{InputDataSource( InputStream )}: "
            + "Null InputStream");
    
    input = in;
  }
  

  /**
   * @see javax.activation.DataSource#getInputStream() 
   */
  @Override
  public InputStream getInputStream() throws IOException {
    return input;
  }


  /**
   * @see javax.activation.DataSource#getOutputStream() 
   */
  @Override
  public OutputStream getOutputStream() throws IOException {
    return null;
  }


  /**
   * @see javax.activation.DataSource#getContentType() 
   */
  @Override
  public String getContentType() {
    return contentType;
  }


  /**
   * @see javax.activation.DataSource#getName() 
   */
  @Override
  public String getName() {
    return name;
  }


  /**
   * Define o tipo (Mime-Type) dos dados.
   * @param contentType Mime-Type
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }


  /**
   * Define o nome da fonte de dados.
   * @param name Nome da fonte de dados.
   */
  public void setName(String name) {
    this.name = name;
  }
  
}
