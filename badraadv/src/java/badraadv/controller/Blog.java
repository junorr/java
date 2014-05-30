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
package badraadv.controller;

import badraadv.bean.Post;
import badraadv.bean.User;
import com.jpower.date.SimpleDate;
import com.jpower.mongo4j.QueryBuilder;
import java.util.*;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/06/2012
 */
@ManagedBean
@ViewScoped
public class Blog {
  
  @ManagedProperty(value="#{dao}")
  private DAOProvider dao;
  
  @ManagedProperty(value="#{user}")
  private User user;
  
  private List<Post> posts;
  
  private List<Date> dates;
  
  private Post post;
  
  private Post newpost;
  
  private Date selectedDate;
  
  private boolean edit;
  
  
  public Blog() {
    edit = false;
  }


  public DAOProvider getDao() {
    return dao;
  }


  public void setDao(DAOProvider dao) {
    this.dao = dao;
  }


  public List<Date> getDates() {
    if(dates == null) {
      this.all();
      this.order();
      dates = new LinkedList<>();
      for(int i = 0; i < posts.size(); i++) {
        boolean contains = false;
        for(int j = 0; j < dates.size(); j++) {
          SimpleDate sd = new SimpleDate(dates.get(j));
          SimpleDate sp = new SimpleDate(posts.get(i).getDate());
          if(sd.equalsDate(sp)) {
            contains = true;
            break;
          }
        }
        if(!contains)
          dates.add(posts.get(i).getDate());
      }
    }
    return dates;
  }


  public void setPost(Post p) {
    this.post = p;
  }
  
  
  public Post getPost() {
    return post;
  }
  
  
  public void setNewpost(Post p) {
    this.newpost = p;
  }
  
  
  public Post getNewpost() {
    if(newpost == null)
      newpost = new Post();
    
    newpost.setUser(user);
    return newpost;
  }
  
  
  public void editPost() {
    if(post == null) {
      this.showErrorMessage("Nenhum Post selecionado para edição");
      return;
    }
    edit = true;
    newpost.setTitle(post.getTitle());
    newpost.setContent(post.getContent());
    newpost.setUser(post.getUser());
  }
  
  
  public void deletePost() {
    if(post == null)
      return;
    
    QueryBuilder q = new QueryBuilder()
        .equal("title", post.getTitle())
        .equal("content", post.getContent())
        .equal("date", post.getDate());
        
    dao.get().delete(q, false);
    this.all();
  }
  
  
  public void addPost() {
    if(edit) {
      this.update();
      edit = false;
      return;
    }
    
    if(newpost.getContent() == null
        || newpost.getContent().trim().isEmpty()) {
      this.showErrorMessage("Informe título e conteúdo para a mensagem");
      return;
    }
    
    dao.get().save(newpost);
    newpost = new Post();
    this.all();
  }
  
  
  public void update() {
    if(newpost.getContent() == null
        || newpost.getContent().trim().isEmpty()) {
      this.showErrorMessage("Informe título e conteúdo para a mensagem");
      return;
    }
    
    QueryBuilder q = new QueryBuilder();
    q.byClass(Post.class)
        .equal("title", post.getTitle())
        .equal("content", post.getContent())
        .equal("date", post.getDate());
    
    dao.get().update(q, newpost);
    newpost = new Post();
    this.all();
  }
  
  
  private void showErrorMessage(String summary) {
    if(summary == null || summary.trim().isEmpty())
      return;
    
    FacesContext.getCurrentInstance()
        .addMessage(null, 
        new FacesMessage(
            FacesMessage.SEVERITY_ERROR, 
            summary, null));
  }
  
  
  public String format(Date d) {
    if(d == null) return "";
    return new SimpleDate(d).format(SimpleDate.DDMMYYYY_SLASH);
  }


  public void setDates(List<Date> dates) {
    this.dates = dates;
  }


  public List<Post> getPosts() {
    this.order();
    return posts;
  }


  public void filter() {
    posts = this.getByDate(selectedDate);
  }


  public void all() {
    posts = this.getAllPosts();
  }
  
  
  public void order() {
    if(posts == null)
      this.all();
    
    Comparator<Post> comp = new Comparator<Post>() {
      @Override
      public int compare(Post o1, Post o2) {
        if(o1 == null || o2 == null) return 0;
        if(o1.getDate().after(o2.getDate()))
          return -1;
        else if(o1.getDate().before(o2.getDate()))
          return 1;
        else
          return 0;
      }
    };
    Collections.sort(posts, comp);
  }


  public Date getSelectedDate() {
    return selectedDate;
  }


  public void setSelectedDate(Date selectedDate) {
    this.selectedDate = selectedDate;
  }
  
  
  private List<Post> getAllPosts() {
    QueryBuilder q = new QueryBuilder()
        .byClass(Post.class);
    return dao.get().find(q);
  }
  
  
  private List<Post> getByDate(Date d) {
    if(d == null) 
      return new LinkedList<>();
    
    QueryBuilder q = new QueryBuilder()
        .byClass(Post.class)
        .equal("date", d);
    
    return dao.get().find(q);
  }


  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }
  
}
