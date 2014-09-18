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
public abstract class Pessoa {
  
  private String nome;
  
  private Endereco endereco;

  
  public Pessoa() {
    nome = null;
    endereco = null;
  }
  
  
  public Pessoa(String nome) {
    this.nome = nome;
    endereco = null;
  }


  public String getNome() {
    return nome;
  }


  public void setNome(String nome) {
    this.nome = nome;
  }


  public Endereco getEndereco() {
    return endereco;
  }


  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.nome);
    hash = 97 * hash + Objects.hashCode(this.endereco);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Pessoa other = (Pessoa) obj;
    if (!Objects.equals(this.nome, other.nome))
      return false;
    if (!Objects.equals(this.endereco, other.endereco))
      return false;
    return true;
  }


  @Override
  public String toString() {
    return "Pessoa{" + "nome=" + nome + ", endereco=" + endereco + '}';
  }
  
}
