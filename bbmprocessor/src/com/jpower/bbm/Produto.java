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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/04/2013
 */
public class Produto {

  private int cdprod;
  
  private int cdmod;
  
  private String prmod;
  
  private String modalidade;
  
  private String linha;
  
  
  public Produto() {
    cdprod = 0;
    cdmod = 0;
    prmod = null;
    modalidade = null;
    linha = null;
  }
  
  
  private void genPrmod() {
    if(cdprod == 0 || cdmod == 0)
      return;
    
    String sprod = String.valueOf(cdprod);
    String smod = String.valueOf(cdmod);
    int lprod = sprod.length();
    int lmod = smod.length();
    
    for(int i = 0; i < (5 - lprod); i++) {
      sprod = "0" + sprod;
    }
    for(int i = 0; i < (5 - lmod); i++) {
      smod = "0" + smod;
    }
    prmod = sprod + "010" + smod;
  }


  public int getCdprod() {
    return cdprod;
  }


  public void setCdprod(int cdprod) {
    this.cdprod = cdprod;
  }


  public int getCdmod() {
    return cdmod;
  }


  public void setCdmod(int cdmod) {
    this.cdmod = cdmod;
  }


  public String getPrmod() {
    if(prmod == null)
      genPrmod();
    return prmod;
  }


  public void setPrmod(String prmod) {
    this.prmod = prmod;
  }


  public String getModalidade() {
    return modalidade;
  }


  public void setModalidade(String modalidade) {
    this.modalidade = modalidade;
  }


  public String getLinha() {
    return linha;
  }


  public void setLinha(String linha) {
    this.linha = linha;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.prmod != null ? this.prmod.hashCode() : 0);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Produto other = (Produto) obj;
    
    String this_prmod = this.getPrmod();
    String other_prmod = other.getPrmod();
    return this_prmod.equals(other_prmod);
  }
  
  
  public boolean parsePrmod(String s) {
    if(s == null || s.trim().isEmpty() 
        || s.length() < 13)
      return false;
    prmod = s;
    
    try {
      
      cdprod = Integer.parseInt(s.substring(0, 4));
      cdmod = Integer.parseInt(s.substring(8));
      return true;
      
    } catch(NumberFormatException e) {
      return false;
    }
  }
  
  
  public boolean fromString(String s) {
    if(s == null || s.isEmpty())
      return false;
    
    String[] vals = s.split(";");
    if(vals.length < 5) return false;
    
    try {
      cdprod = Integer.parseInt(vals[0]);
      cdmod = Integer.parseInt(vals[1]);
      prmod = vals[2];
      modalidade = vals[3];
      linha = vals[4];
      return true;
      
    } catch(NumberFormatException ex) {
      return false;
    }
  }
  
  
  @Override
  public String toString() {
    return String.valueOf(cdprod) + ";"
        + String.valueOf(cdmod) + ";"
        + prmod + ";"
        + modalidade + ";"
        + linha;
  }
  
}
