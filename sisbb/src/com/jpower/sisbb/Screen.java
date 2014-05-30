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
package com.jpower.sisbb;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Objects;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/02/2012
 */
public class Screen {
  
  public static Point DEFAULT_ID_POSITION = new Point(1, 3);
  
  
  private LinkedList<Field> args;
  
  private LinkedList<Field> fields;
  
  private String key;
  
  private String id;
  
  private Point idpos;
  
  private Field altWait;
  
  
  public Screen() {
    args = new LinkedList<Field>();
    fields = new LinkedList<Field>();
    altWait = null;
    key = null;
    id = null;
    idpos = DEFAULT_ID_POSITION;
  }
  
  
  public Screen(String id) {
    this();
    this.setId(id);
  }
  
  
  public void addArg(Field arg) {
    if(arg != null) args.add(arg);
  }


  public void addField(Field field) {
    if(field != null) 
      fields.add(field);
  }
  
  
  public Field getArg(String content) {
    if(content == null || content.trim().isEmpty())
      return null;
    for(Field f : fields) {
      if(f.getContent().equals(content))
        return f;
    }
    return null;
  }
  
  
  public Field getArg(Point p) {
    if(p == null)
      return null;
    for(Field f : fields) {
      if(f.getPosition().equals(p))
        return f;
    }
    return null;
  }
  
  
  public Field getField(String name) {
    if(name == null || name.trim().isEmpty())
      return null;
    for(Field f : fields) {
      if(f.getName().equals(name))
        return f;
    }
    return null;
  }


  public Field getAlternativeWait() {
    return altWait;
  }


  public void setAlternativeWait(Field altWait) {
    this.altWait = altWait;
  }
  
  
  public void setKey(String key) {
    if(key != null && !key.trim().equals(""))
      this.key = key;
  }


  public Point getIdPosition() {
    return idpos;
  }


  public void setIdPosition(Point idpos) {
    this.idpos = idpos;
  }


  public LinkedList<Field> args() {
    return args;
  }


  public void setArgs(LinkedList<Field> args) {
    this.args = args;
  }


  public LinkedList<Field> fields() {
    return fields;
  }


  public void setFields(LinkedList<Field> fields) {
    this.fields = fields;
  }


  public String getKey() {
    return key;
  }


  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + Objects.hashCode(this.id);
    return hash;
  }
  
  
  public boolean equals(Object o) {
    return (o != null && o instanceof Screen 
        ? o.hashCode() == this.hashCode() : false);
  }
  
  
  public String toString() {
    return "[Screen: '"+id+"', Key: '"+key+"']";
  }
  
  
  public boolean execArgs(Sisbb s) {
    if(s ==  null || !s.isConnected()) return false;
    
    s.clearError();
    s.delay(Sisbb.DELAY);
    
    if(!args.isEmpty())
      for(Field a : args) {
        s.set(a);
      }
    
    return s.hasError();
  }
  
  
  public boolean execFields(Sisbb s) {
    if(s ==  null || !s.isConnected()) return false;
    
    s.clearError();
    s.delay(Sisbb.DELAY);
    
    if(!fields.isEmpty())
      for(Field f : fields) {
        s.get(f);
      }
    
    return s.hasError();
  }
  
  
  public boolean exec(Sisbb s) {
    if(s ==  null || !s.isConnected()) return false;
    
    if(altWait != null) {
      Field f = s.wait(new Field(this.idpos, this.id), altWait);
      if(f.equals(altWait)) return false;
      
    } else
      s.wait(this);
    
    this.execArgs(s);
    
    s.delay(Sisbb.DELAY);
    if(key != null)
      s.sendKey(key);
    
    this.execFields(s);
    
    return s.hasError();
  }
  
}
