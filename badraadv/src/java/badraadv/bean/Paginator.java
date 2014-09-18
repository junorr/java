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
package badraadv.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/06/2012
 */
public class Paginator<T> {
  
  private List<T> list;
  
  private List<ArrayList<T>> pages;
  
  private int page;
  
  private int rows;
  
  
  public Paginator(int rows, List<T> list) {
    this.rows = rows;
    pages = new LinkedList<>();
    pages.add(new ArrayList<T>(1));
    this.list = list;
    page = 0;
    this.paginate();
  }
  
  
  private void paginate() {
    if(list == null) return;
    Iterator<T> it = list.iterator();
    ArrayList<T> ts = new ArrayList<>(rows);
    while(it.hasNext()) {
      T t = it.next();
      if(ts.size() == rows) {
        pages.add(ts);
        ts = new ArrayList<>(rows);
      }
      ts.add(t);
    }
  }
  
  
  public List<T> currentPage() {
    if(page >= pages.size())
      page = 0;
    return pages.get(page);
  }
  
  
  public int getPages() {
    return pages.size();
  }
  
  
  public List<T> nextPage() {
    if(page >= pages.size())
      page = 0;
    return pages.get(page++);
  }
  
  
  public List<T> prevPage() {
    if((page-1) < 0)
      page = pages.size();
    return pages.get(--page);
  }
  
}
