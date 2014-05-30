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

package com.jpower.db4o;

import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 27/12/2012
 */
public class Endereco {

  private String rua;
  
  private int numero;
  
  private Cidade cidade;
  
  
  public Endereco() {
    rua = null;
    numero = 0;
    cidade = null;
  }


  public String getRua() {
    return rua;
  }


  public void setRua(String rua) {
    this.rua = rua;
  }


  public int getNumero() {
    return numero;
  }


  public void setNumero(int numero) {
    this.numero = numero;
  }


  public Cidade getCidade() {
    return cidade;
  }


  public void setCidade(Cidade cidade) {
    this.cidade = cidade;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 19 * hash + Objects.hashCode(this.rua);
    hash = 19 * hash + this.numero;
    hash = 19 * hash + Objects.hashCode(this.cidade);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Endereco other = (Endereco) obj;
    if (!Objects.equals(this.rua, other.rua))
      return false;
    if (this.numero != other.numero)
      return false;
    if (!Objects.equals(this.cidade, other.cidade))
      return false;
    return true;
  }


  @Override
  public String toString() {
    return rua + ", numero=" + numero + ", cidade=" + cidade;
  }
  
}
