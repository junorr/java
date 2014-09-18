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

import com.jpower.log.SimpleLog;
import com.jpower.log.SimpleLogFactory;
import java.awt.Point;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 01/02/2012
 */
public class Field {
  
  private String name;
  
  private Point pos;
  
  private int length;
  
  private String content;
  
  private SimpleLog slog;
  
  
  public Field() {
    slog = SimpleLogFactory.getInstance();
    pos = null;
    length = 0;
    content = null;
    name = null;
  }
  
  
  public Field(String name, Point pos, int length) {
    this();
    this.pos = pos;
    this.length = length;
    this.name = name;
  }
  
  
  public Field(Point pos, String content) {
    this();
    this.setPosition(pos);
    this.setContent(content);
  }
  
  
  public boolean validate() {
    return pos != null && length > 0;
  }
  
  
  public boolean fillContent(Sisbb sisbb) {
    if(!validate() || sisbb == null) return false;
    sisbb.clearError();
    sisbb.setCursor(pos);
    content = sisbb.getString(length);
    sisbb.prevCursor();
    return sisbb.hasError();
  }
  
  
  public Date getAsDate() {
    if(content == null) return null;
    content = content.trim();
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    try {
      return df.parse(content);
    } catch (ParseException ex) {
      slog.logWarning(ex.getMessage());
      return null;
    }
  }
  
  
  public double getAsDouble() {
    if(content == null) return -1;
    content = content.trim();
    try {
      return Double.parseDouble(content);
    } catch(NumberFormatException ex) {
      slog.logWarning(ex.getMessage());
      return -1;
    }
  }

  
  public long getAsLong() {
    if(content == null) return -1;
    content = content.trim();
    try {
      return Long.parseLong(content);
    } catch(NumberFormatException ex) {
      slog.logWarning(ex.getMessage());
      return -1;
    }
  }

  
  public int getAsInt() {
    if(content == null) return -1;
    content = content.trim();
    try {
      return Integer.parseInt(content);
    } catch(NumberFormatException ex) {
      slog.logWarning(ex.getMessage());
      return -1;
    }
  }


  public String getContent() {
    return content;
  }


  public void setContent(String c) {
    if(c != null && !c.trim().equals(""))
      this.content = c;
    if(content != null) length = content.length();
  }


  public int getLength() {
    return length;
  }


  public void setLength(int length) {
    this.length = length;
  }


  public Point getPosition() {
    return pos;
  }


  public void setPosition(Point pos) {
    this.pos = pos;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public int hashCode() {
    int hash = 5;
    hash = 23 * hash + Objects.hashCode(this.pos);
    hash = 23 * hash + this.length;
    hash = 23 * hash + Objects.hashCode(this.content);
    return hash;
  }
  
  
  public boolean equals(Object o) {
    return (o != null && o instanceof Field
        ? o.hashCode() == this.hashCode() : false);
  }
  
  
  public String toString() {
    return "[Field: "+ pos+ ", "+ content+ ", "+ length+ "]";
  }
  
}
