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

import com.jpower.date.SimpleDate;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 20/06/2012
 */
public class Post implements Serializable {
  
  private String title;
  
  private String content;
  
  private Date date;
  
  private User user;
  
  
  public Post() {
    date = new SimpleDate().hour(0).minute(0).second(0).millis(0);
    title = "";
    content = "";
  }
  
  
  public Post(String title, String content) {
    this();
    this.title = title;
    this.content = content;
  }
  
  
  public String getContent() {
    return content;
  }


  public void setContent(String content) {
    this.content = content;
  }


  public Date getDate() {
    return date;
  }


  public void setDate(Date date) {
    this.date = date;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }
  
  
  public String getFormattedDate() {
    return new SimpleDate(date).format(SimpleDate.DDMMYYYY_SLASH);
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }

}
