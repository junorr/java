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
package badraadv.bean;

import com.jpower.date.SimpleDate;
import com.jpower.mongo4j.annotation.Hidden;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Armazena informações de login do tipo usuário e senha,
 * utilizando a classe <code>Password</code> e capaz
 * de autenticar as informações de outro objeto <code>User</code>,
 * através do método <code>authenticate( User ) : boolean</code>;
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 16/12/2011
 * @see adv.roesler.talitah.Password;
 */
@ManagedBean
@SessionScoped
public class User implements Serializable {
  
  private String email;
  
  @Hidden private Password passwd;
  
  private transient String password;
  
  private String name;
  
  private String surname;
  
  private transient String fullName;
  
  private Date birthday;
  
  private String phone;
  
  private String phone2;
  
  private String gender;
  
  private boolean admin;
  
  private boolean adv;
  
  
  /**
   * Construtor sem argumentos, cria um objeto
   * <code>User</code> vazio.
   */
  public User() {
    email = null;
    passwd = null;
    name = null;
    surname = null;
    birthday = null;
    phone = null;
    phone2 = null;
    gender = null;
    admin = false;
    adv = false;
  }
  
  
  /**
   * Construtor padrão que recebe a identificação
   * e senha do usuário.
   * @param email Nome do usuário.
   * @param password Senha do usuário.
   */
  public User(String email, String password) {
    this();
    this.email = email;
    this.passwd = new Password(password);
  }
  
  
  private User copy(User from, User to) {
    if(from == null || !from.validate()
        || to == null)
      return null;
    
    if(from.name != null)
      to.name = from.name;
    if(from.surname != null)
      to.surname = from.surname;
    if(from.email != null)
      to.email = from.email;
    if(from.birthday != null)
      to.birthday = from.birthday;
    if(from.gender != null)
      to.gender = from.gender;
    if(from.passwd != null)
      to.passwd = from.passwd;
    if(from.phone != null)
      to.phone = from.phone;
    if(from.phone2 != null)
      to.phone2 = from.phone2;
    
    to.admin = from.admin;
    
    return to;
  }
  
  
  public User copyFrom(User u) {
    if(u == null || !u.validate())
      return null;
    
    return this.copy(u, this);
  }


  public User copyTo(User u) {
    if(u == null || !this.validate())
      return null;
    
    return this.copy(this, u);
  }


  /**
   * Define o password do usuário, convertendo 
   * a string informada em um objeto <code>Password</code>.
   * @param pass String a ser transformada em um
   * objeto Password.
   * @see adv.roesler.talitah.Password
   */
  public void setPassword(String pass) {
    if(pass == null || pass.trim().isEmpty())
      return;
    passwd = new Password(pass);
  }
  
  
  public String getPassword() {
    return "";
  }
  
  
  public Password getObjectPassword() {
    return passwd;
  }
  
  
  public void setObjectPassword(Password p) {
    passwd = p;
  }


  /**
   * Retorna o nome de usuário.
   * @return Nome de usuário.
   */
  public String getEmail() {
    return email;
  }


  /**
   * Define o nome de usuário.
   * @param email Nome de usuário.
   */
  public void setEmail(String email) {
    this.email = email;
  }


  /**
   * Verifica se os dados do usuário estão
   * estão definidos corretamente.
   * @return <code>true</code> se o nome
   * de usuário e senha estiverem definidos
   * com valore válidos, <code>false</code>
   * caso contrário.
   */
  public boolean validate() {
    boolean valid = name != null;
    valid = valid && surname != null;
    valid = valid && email != null;
    valid = valid && gender != null;
    valid = valid && phone != null;
    valid = valid && passwd != null;
    valid = valid && passwd.getBytes() != null;
    valid = valid && passwd.getBytes().length > 0;
    return valid;
  }
  
  
  /**
   * Autentica o usuário informado, com o nome de
   * usuário e objeto <code>Password</code> da
   * instância atual, retornando o sucesso da
   * autenticação.
   * @param email E-mail
   * @param password Password
   * @return <code>true</code> se o usuário 
   * for autenticado com sucesso, false caso
   * contrário.
   * @see adv.roesler.talitah.Password
   */
  public boolean authenticate(User u) {
    if(u == null || u.getEmail() == null 
        || u.getObjectPassword() == null)
      return false;
    
    if(this.getEmail() == null || this.getObjectPassword() == null)
      return false;
    
    return this.email.equals(u.getEmail()) 
        && passwd.equals(u.getObjectPassword());
  }
  
  
  public Date getBirthday() {
    return birthday;
  }
  
  
  public String formattedBirthday() {
    if(birthday != null)
      return new SimpleDate(birthday).format(SimpleDate.DDMMYYYY_SLASH);
    return null;
  }


  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }


  public String getPhone2() {
    return phone2;
  }


  public void setPhone2(String celphone) {
    this.phone2 = celphone;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
    if(name != null) {
      fullName = name;
      if(surname != null)
        fullName += " " + surname;
    }
  }
  
  
  public String getFullName() {
    if(name == null) return null;
    if(surname == null) return name;
    return name + " " + surname;
  }
  
  
  public void setFullName(String s) {
    if(s == null) return;
    if(!s.contains(" ")) {
      this.name = s;
    } else {
      this.name = s.substring(0, s.indexOf(" "));
      this.surname = s.substring(s.indexOf(" ") +1);
    }
  }
  
  
  public String getPhone() {
    return phone;
  }


  public void setPhone(String phone) {
    this.phone = phone;
  }


  public String getSurname() {
    return surname;
  }


  public void setSurname(String surname) {
    this.surname = surname;
    if(name != null) {
      fullName = name;
      if(surname != null)
        fullName += " " + surname;
    }
  }


  public String getGender() {
    return gender;
  }


  public void setGender(String g) {
    if(g != null && !g.trim().equals(""))
      this.gender = g;
  }


  public boolean isAdmin() {
    return admin;
  }


  public void setAdmin(boolean admin) {
    this.admin = admin;
  }


  public boolean isAdv() {
    return adv;
  }


  public void setAdv(boolean adv) {
    this.adv = adv;
  }
  
  
  @Override
  public boolean equals(Object o) {
    if(o != null && o instanceof User) {
      return this.hashCode()
          == o.hashCode();
    }
    return false;
  }
  
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + (this.email != null ? this.email.hashCode() : 0);
    hash = 97 * hash + (this.passwd != null ? this.passwd.hashCode() : 0);
    hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
    hash = 97 * hash + (this.surname != null ? this.surname.hashCode() : 0);
    hash = 97 * hash + (this.admin ? 1 : 0);
    return hash;
  }
  
  
  @Override
  public String toString() {
    return "User ["
        + "name: " + this.getFullName()
        + ", hash: " + this.hashCode() + "]";
  }
  
}
