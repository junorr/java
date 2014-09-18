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
package com.jpower.simplemail.filter;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import com.jpower.simplemail.Message;


/**
 * <p style="font-size: medium;">
 * Filtro para pesquisa de texto no conteúdo 
 * do email;
 * </p>
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "ContentFilter",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Filtro de conteúdo da mensagem"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class ContentFilter implements Filter<Message> {

  private String content;
  
  
  /**
   * Construtor padrão que recebe o texto 
   * a ser pesquisado no conteúdo da mensagem.
   * @param content Texto a ser pesquisado.
   */
  public ContentFilter(String content) {
    if(content == null || content.trim().equals(""))
      throw new IllegalArgumentException("Invalid content to search");
    this.content = content;
  }
  
  
  /**
   * Define o texto a ser pesquisado no conteúdo
   * da mensagem.
   * @param content Texto a ser pesquisado.
   * @return Esta instância modificada de ContentFilter.
   */
  public ContentFilter setContent(String content) {
    if(content != null && !content.trim().equals(""))
      this.content = content;
    return this;
  }
  
  
  /**
   * Retorna o texto a ser pesquisado no conteúdo
   * da mensagem.
   * @return Texto a ser pesquisado.
   */
  public String getContent() {
    return this.content;
  }
  

  /**
   * Verifica se a mensagem informada possui conteúdo
   * compatível com o texto pesquisado.
   * @param e Mensagem a ser comparada.
   * @return <code>true</code> se o texto comparado
   * é compatível com o conteúdo da mensagem,
   * <code>false</code> caso contrário.
   */
  @Override
  public boolean match(Message e) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}
