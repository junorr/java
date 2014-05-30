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
package adv.roesler.talitah.bean;

import com.jpower.date.SimpleDate;
import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 05/01/2012
 */
public class Andamento implements Serializable {
  
  private Date data;
  
  private String descricao;
  
  
  public Andamento() {
    data = new Date();
    descricao = null;
  }
  
  
  public Andamento(Date data, String descricao) {
    this.data = data;
    this.descricao = descricao;
  }
  
  
  public Date getData() {
    return data;
  }


  public void setData(Date data) {
    this.data = data;
  }


  public String getDescricao() {
    return descricao;
  }


  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }


  public int hashCode() {
    int hash = 37;
    hash = 43 * hash + (this.data != null ? this.data.hashCode() : 0);
    hash = 43 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
    return hash;
  }


  @Override
  public boolean equals(Object o) {
    if(o != null && o instanceof Andamento) {
      return this.hashCode()
          == o.hashCode();
    }
    return false;
  }


  @Override
  public String toString() {
    return "[Andamento: " + 
        (data != null ? new SimpleDate(data)
        .format(SimpleDate.DDMMYYYY_SLASH) : "") +
        " : " + descricao + "]";
  }
  
}
