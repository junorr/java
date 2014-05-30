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

package com.jpower.jst;

import java.util.List;

/**
 * <p style="font-size: medium;">
 * Modelo para listas pesquisáveis com SearchEngine.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.11.25
 */
public interface Searchable {
  
  /**
   * Define a fonte de dados onde se dará a pesquisa.
   * @param s Fonte de dados.
   */
  public void setSource(List<String> s);
  
  /**
   * Retorna a fonte de dados onde se dará a pesquisa.
   * @return Fonte de dados.
   */
  public List<String> getSource();
  
  /**
   * Retorna o motor de pesquisa.
   * @return Motor de pesquisa de texto <code>SearchEngine</code>.
   * @see com.power.utils.text.SearchEngine
   */
  public SearchEngine getEngine();
  
  /**
   * Define o motor de pesquisa.
   * @param e Motor de pesquisa de texto <code>SearchEngine</code>.
   * @see com.power.utils.text.SearchEngine
   */
  public void setEngine(SearchEngine e);

  /**
   * Pesquisa a String informada na lista,
   * retornando o primeiro resultado encontrado.
   * @param s Texto a ser pesquisado.
   * @return Primeiro resultado encontrado ,
   * ou <code>null</code>, caso não encontre 
   * um texto compatível.
   */
  public String searchFirst(String s);

  /**
   * Pesquisa a String informada na lista,
   * retornando uma lista com os possíveis resultados.
   * @param s Texto a ser pesquisado.
   * @return Lista com os resultados encontrados.
   */
  public List<String> search(String s);

}
