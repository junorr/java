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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.pstat;

import java.io.Serializable;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/02/2013
 */
public class ImageInfo implements Serializable {

  private String path;
  
  private String name;
  
  private String title;
  
  private String comment;
  
  private String link;
  
  
  public ImageInfo() {
    path = null;
    name = null;
    title = null;
    comment = null;
    link = null;
  }


  public String getPath() {
    return path;
  }


  public ImageInfo setPath(String path) {
    this.path = path;
    return this;
  }


  public String getName() {
    return name;
  }


  public ImageInfo setName(String name) {
    this.name = name;
    return this;
  }


  public String getTitle() {
    return title;
  }


  public ImageInfo setTitle(String title) {
    this.title = title;
    return this;
  }


  public String getComment() {
    return comment;
  }


  public ImageInfo setComment(String comment) {
    this.comment = comment;
    return this;
  }


  public String getLink() {
    return link;
  }


  public ImageInfo setLink(String link) {
    this.link = link;
    return this;
  }
  
}
