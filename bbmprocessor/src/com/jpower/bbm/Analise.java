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

import com.jpower.date.SimpleDate;
import java.text.DecimalFormat;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 11/04/2013
 */
public class Analise {
  
  private int prefixo;
  
  private int operacao;
  
  private String situacao;
  
  private SimpleDate data;
  
  private Produto produto;
  
  private double valor;
  
  private Instancia instancia;
  
  
  public Analise() {
    prefixo = 0;
    operacao = 0;
    situacao = null;
    data = null;
    produto = null;
    valor = 0;
    instancia = null;
  }


  public int getPrefixo() {
    return prefixo;
  }


  public void setPrefixo(int prefixo) {
    this.prefixo = prefixo;
  }


  public int getOperacao() {
    return operacao;
  }


  public void setOperacao(int operacao) {
    this.operacao = operacao;
  }


  public String getSituacao() {
    return situacao;
  }


  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }


  public SimpleDate getData() {
    return data;
  }


  public void setData(SimpleDate data) {
    this.data = data;
  }


  public Produto getProduto() {
    return produto;
  }


  public void setProduto(Produto produto) {
    this.produto = produto;
  }


  public double getValor() {
    return valor;
  }


  public void setValor(double valor) {
    this.valor = valor;
  }


  public Instancia getInstancia() {
    return instancia;
  }


  public void setInstancia(Instancia instancia) {
    this.instancia = instancia;
  }
  
  
  public boolean fromString(String s) {
    if(s == null || s.trim().isEmpty())
      return false;
    
    String[] vals = s.split(";");
    if(vals == null || vals.length < 9)
      return false;
    
    try {
      
      prefixo = Integer.parseInt(vals[0]);
      operacao = Integer.parseInt(vals[1]);
      situacao = vals[2].trim();
      data = SimpleDate.parseDate(vals[3]);
      produto = new Produto();
      produto.parsePrmod(vals[5]);
      if(!vals[6].trim().equals("null"))
        produto.setLinha(vals[6]);
      valor = Double.parseDouble(vals[7]);
      if(vals[8].equals(Instancia.AGENCIA.name()))
        instancia = Instancia.AGENCIA;
      else
        instancia = Instancia.CSO;
      
      return true;
      
    } catch(NumberFormatException e) {
      return false;
    }
  }
  
  
  public String getStringValue() {
    DecimalFormat df = new DecimalFormat("#.##");
    return df.format(valor);
  }
  
  
  public String toString() {
    return String.valueOf(prefixo) + ";"
        + String.valueOf(operacao) + ";"
        + situacao + ";"
        + (data != null 
          ? data.format(SimpleDate.DDMMYYYY_SLASH) 
          : "null") + ";"
        + (data != null 
          ? String.valueOf(data.month())
          : "null") + ";"
        + (produto != null
          ? produto.getPrmod()
          : "null") + ";"
        + (produto != null
          ? produto.getLinha()
          : "null") + ";"
        + getStringValue() + ";"
        + (instancia != null
          ? instancia.name() : "null");
  }
  
}
