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

import badraadv.User;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/12/2011
 */
public class Processo implements Serializable {
  
  private long numero;
  
  private String formattedNumber;
  
  private String partes;
  
  private String natureza;
  
  private String vara;
  
  private User user;
  
  private User advogado;
  
  private transient List<Andamento> adms;
  
  private Andamento lastAdm;
  
  
  public Processo() {
    this.user = null;
    numero = 0;
    natureza = null;
    partes = null;
    vara = null;
    formattedNumber = null;
    adms = new LinkedList<Andamento>();
    lastAdm = null;
  }
  
  
  public Processo(User u) {
    this();
    this.user = u;
  }
  
  
  public void add(Andamento a) {
    if(a != null) {
      adms.add(a);
      lastAdm = a;
    }
  }
  
  
  public void addAndamento() {
    this.add(new Andamento());
  }


  public Andamento getLastAdm() {
    return lastAdm;
  }


  public long getNumero() {
    return numero;
  }
  
  
  public List<Andamento> getAdms() {
    return adms;
  }
  
  
  public void setAdms(List<Andamento> l) {
    if(l != null) {
      adms = l;
      if(!adms.isEmpty()) {
        Collections.sort(adms, new AndamentoComparator());
        lastAdm = adms.get(adms.size() -1);
      }
    }
  }


  public void setNumero(long numero) {
    this.numero = numero;
    formattedNumber = this.format(numero);
  }
  
  
  private String format(long num) {
    String snum = String.valueOf(num);
    if(snum.length() < 20) {
      int im = 20 - snum.length();
      for(int i = 0; i < im; i++) {
        snum = "0" + snum;
      }
    }
    String f = snum.substring(0, 7) + "-";
    f += snum.substring(7, 9) + ".";
    f += snum.substring(9, 13) + ".";
    f += snum.substring(13, 14) + ".";
    f += snum.substring(14, 16) + ".";
    f += snum.substring(16);
    return f;
  }
  
  
  public String getFormattedNumber() {
    return formattedNumber;
  }
  
  
  public void setFormattedNumber(String s) {
    if(s == null) return;
    s = s.replaceAll("-", "");
    s = s.replaceAll("\\.", "");
    try {
      this.numero = Long.parseLong(s);
      this.hashCode();
    } catch(NumberFormatException ex) {}
  }


  public String getPartes() {
    return partes;
  }


  public void setPartes(String partes) {
    this.partes = partes;
  }


  public String getNatureza() {
    return natureza;
  }


  public void setNatureza(String nat) {
    this.natureza = nat;
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }


  public User getAdvogado() {
    return advogado;
  }


  public void setAdvogado(User advogado) {
    this.advogado = advogado;
  }

  
  public String getVara() {
    return vara;
  }

  
  public void setVara(String vara) {
    this.vara = vara;
  }
  
  
  @Override
  public boolean equals(Object o) {
    if(o != null && o instanceof Processo) {
      return this.hashCode()
          == o.hashCode();
    }
    return false;
  }


  @Override
  public int hashCode() {
    return (int) numero;
  }
  
  
  @Override
  public String toString() {
    return "[Processo : " + 
        this.getFormattedNumber() + " : " +
        (user != null ? user.getName() : "") + " : " +
        this.partes + "]";
  }
  
  
  public static void main(String[] args) {
    Processo p = new Processo(new User());
    long num = 5755464820108217000l;
    System.out.println(num);
    p.setNumero(num);
    System.out.println(p.getFormattedNumber());
    p.setFormattedNumber(p.getFormattedNumber());
  }
  
}
