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


/**
 * <p style="font-size: medium;">
 * Utilitário para pesquisa rápida.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.11.25
 */
public class Search {
  
  
  /**
   * Pesquisa a primeira string na segunda,
   * retornando <code>true</code> se compatível.
   * @see com.jpower.jst.SearchEngine
   * @param text Texto procurado.
   * @param source Base onde será feira a pesquisa.
   * @return <code>true</code> se a segunda String contém
   * texto compatível com a primeira, <code>false</code>
   * caso contrário.
   */
  public static boolean search(String text, String source) {
    return search(text, source, new SearchEngine());
  }
  
  
  /**
   * Pesquisa a primeira string na segunda,
   * retornando <code>true</code> se compatível.
   * @see com.jpower.jst.SearchEngine
   * @param text Texto procurado.
   * @param source Base onde será feira a pesquisa.
   * @param accuracy nível de precisão utilizada por SearchEngine.
   * @return <code>true</code> se a segunda String contém
   * texto compatível com a primeira, <code>false</code>
   * caso contrário.
   */
  public static boolean search(String text, String source, double accuracy) {
    SearchEngine e = new SearchEngine();
    e.setAccuracy(accuracy);
    return search(text, source, e);
  }
  
  
  private static boolean search(String text, String source, SearchEngine eng) {
    if(text == null || source == null || eng == null)
      return false;
    eng.setSource(SearchEngine.toSearch(source));
    eng.setText(SearchEngine.toSearch(text));
    return eng.match();
  }
  
}
