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

package com.jpower.bbm;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 15/04/2013
 */
public class ProdutoCount {

  private Produto p;
  
  private int count;
  
  private double tvalue;
  
  private Instancia inst;
  
  
  public ProdutoCount() {
    p = null;
    tvalue = 0;
    inst = null;
  }
  
  
  public ProdutoCount(Analise a) {
    this();
    if(a == null || a.getProduto() == null
        || a.getProduto().getPrmod() == null
        || a.getInstancia() == null)
      throw new IllegalArgumentException(
          "Invalid Analise object: "+ a);
    
    this.p = a.getProduto();
    this.count = 1;
    this.tvalue = a.getValor();
    inst = a.getInstancia();
  }
  
  
  public ProdutoCount inc(double value) {
    if(value > 0) {
      count++;
      tvalue += value;
    }
    return this;
  }


  public Produto getProduto() {
    return p;
  }


  public ProdutoCount setProduto(Produto p) {
    this.p = p;
    return this;
  }


  public double getTotalValue() {
    return tvalue;
  }


  public Instancia getInstancia() {
    return inst;
  }


  public ProdutoCount setInstancia(Instancia inst) {
    this.inst = inst;
    return this;
  }


  public ProdutoCount setTotalValue(double tvalue) {
    this.tvalue = tvalue;
    return this;
  }
  
  
  public int getCount() {
    return count;
  }


  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + Objects.hashCode(this.p);
    hash = 9 * hash + Objects.hashCode(this.inst);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if(obj == null) return false;
    if(obj instanceof Analise)
      return equals((Analise) obj);
    if(getClass() != obj.getClass())
      return false;
    
    final ProdutoCount other = (ProdutoCount) obj;
    
    if(other.getProduto() == null && this.p == null)
      return true;
    else if(other.getProduto() == null || this.p == null)
      return false;
    
    if(other.getInstancia() == null && this.inst == null)
      return true;
    else if(other.getInstancia() == null || this.inst == null)
      return false;
    
    return other.getProduto().equals(this.p)
        && other.getInstancia().equals(this.inst);
  }
  
  
  public boolean equals(Analise obj) {
    if(obj == null) return false;
    return obj.getProduto().equals(this.p)
        && obj.getInstancia().equals(this.inst);
  }
  
  
  public String getStringValue() {
    DecimalFormat df = new DecimalFormat("#.##");
    return df.format(tvalue);
  }
  
  
  @Override
  public String toString() {
    if(p == null) return null;
    return (p.getPrmod() == null ? "null" : p.getPrmod()) + ";"
        + (p.getLinha() == null ? "null" : p.getLinha()) + ";"
        + String.valueOf(count) + ";"
        + getStringValue() + ";"
        + (inst == null ? "null" : inst.name());
  }
  
  
  public double parseValue(String s) {
    if(s == null || s.trim().isEmpty())
      return -1;
    try {
      if(s.lastIndexOf(",") > s.lastIndexOf(".")) {
        s = s.replace(".", "");
        s = s.replace(",", ".");
      }
      else s = s.replace(",", "");
      return Double.parseDouble(s);
      
    } catch(NumberFormatException e) {
      return -1;
    }
  }
  
  
  public boolean fromString(String s) {
    if(s == null || s.trim().isEmpty())
      return false;
    
    String[] vals = s.split(";");
    if(vals == null || vals.length < 5)
      return false;
    
    if(p == null) p = new Produto();
    p.setPrmod(vals[0]);
    p.setLinha(vals[1]);
    
    try {
      count = Integer.parseInt(vals[2]);
      tvalue = this.parseValue(vals[3]);
      if(vals[4].equals(Instancia.AGENCIA.name()))
        inst = Instancia.AGENCIA;
      else inst = Instancia.CSO;
      
      return true;
      
    } catch(NumberFormatException e) {
      return false;
    }
  }
  
}
