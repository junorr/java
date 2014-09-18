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

package com.jpower.tn3270.script;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/08/2013
 */
public class Var {
  
  public static final String STR_TEMP = "TEMP";
  
  public static final String STR_NL = "NL";
  
  public static final String STR_CSV = "CSV";
  
  
  public static final Var 
      
      NL = new Var(STR_NL, "\n"),
      
      CSV = new Var(STR_CSV, ";"),
      
      TEMP = new Var(STR_TEMP);
  
  
  public static final int INVALID_NUMBER = Integer.MIN_VALUE;
  

  private String name;
  
  private Object value;
  
  private NumberFormat format;
  
  
  public Var() {
    name = null;
    value = null;
    format = DecimalFormat.getInstance();
  }
  
  
  public Var(String name) {
    this.name = name;
    value = null;
    format = DecimalFormat.getInstance();
  }
  
  
  public Var(String name, Object value) {
    this.name = name;
    format = DecimalFormat.getInstance();
    this.setValue(value);
  }
  
  
  public String name() {
    return name;
  }
  
  
  public String value() {
    return (value != null 
        ? value.toString() : null);
  }
  
  
  public Object getObject() {
    return value;
  }
  
  
  public Var setName(String str) {
    name = str;
    return this;
  }
  
  
  public Var setValue(Object obj) {
    value = obj;
    return this;
  }
  
  
  public double getDouble() {
    double db = INVALID_NUMBER;
    if(value == null) return db;
    if(Number.class.isAssignableFrom(value.getClass()))
      return ((Number) value).doubleValue();

    try {
      db = Double.parseDouble(value.toString());
      if(String.valueOf(db).equals(value.toString()))
        return db;
    } catch(NumberFormatException ne) {}
    try { 
      db = format.parse(
        value.toString()).doubleValue(); 
    } catch(ParseException e) {}
    return db;
  }
  
  
  public int getInt() {
    return (int) this.getDouble();
  }
  
  
  public double getDouble(NumberFormat fmt) {
    if(fmt == null) return this.getDouble();
    double db = INVALID_NUMBER;
    if(value == null) return db;
    if(Number.class.isAssignableFrom(value.getClass()))
      return ((Number) value).doubleValue();

    try {
      db = Double.parseDouble(value.toString());
      if(String.valueOf(db).equals(value.toString()))
        return db;
    } catch(NumberFormatException ne) {}
    try { 
      db = fmt.parse(
        value.toString()).doubleValue(); 
    } catch(ParseException e) {}
    return db;
  }
  
  
  public int getInt(NumberFormat fmt) {
    return (int) this.getDouble(fmt);
  }
  
  
  public boolean getBoolean() {
    if(value == null) return false;
    return Boolean.parseBoolean(
        value.toString());
  }
  
  
  public String toString() {
    return (name == null || value == null 
        ? "false" : name+"="+value);
  }
  
  
  public static void main(String[] args) {
    Var v = new Var("myvar", 500);
    System.out.println("* "+v);
    v.setValue(5000.44);
    System.out.println("* "+v.getDouble());
    v.setValue("4,000.55");
    System.out.println("* "+v.getDouble());
  }
  
}
