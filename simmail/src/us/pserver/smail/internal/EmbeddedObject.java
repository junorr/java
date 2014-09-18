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
import java.io.IOException;
import java.io.InputStream;


/**
 * <p style="font-size: medium;">
 * Define o comportamento de um objeto embarcado
 * de email.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public interface EmbeddedObject {
  
  /**
   * Retorna o nome do objeto.
   * @return Nome do objeto.
   */
  public String getName();
  
  /**
   * Define o nome do objeto.
   * @param name Nome do objeto.
   */
  public void setName(String name);
  
  /**
   * Retorna o tipo mime (Mime-Type) do objeto.
   * @return Tipo mime do objeto
   */
  public String getType();
  
  /**
   * Define o tipo mime (Mime-Type) do objeto.
   * @param type Tipo mime do objeto
   */
  public void setType(String type);
  
  /**
   * Retorna a descrição do objeto.
   * @return Descrição do objeto.
   */
  public String getDescription();
  
  /**
   * Define a descrição do objeto.
   * @param desc Descrição do objeto.
   */
  public void setDescription(String desc);
  
  /**
   * Salva o conteúdo do <code>InputStream</code>
   * no arquivo especificado.
   * @param input Fonte de dados.
   * @param f Arquivo de destino.
   * @throws IOException No caso de erro na
   * transferência dos dados.
   */
  public void saveTo(InputStream input, File f) throws IOException;
  
  /**
   * Salva o conteúdo do objeto no arquivo especificado.
   * @param f Arquivo de destino.
   * @throws IOException No caso de erro ao salvar.
   */
  public void saveTo(File f) throws IOException;
  
}
