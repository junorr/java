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


package com.jpower.sys.security;

import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import com.jpower.sys.DB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @version 0.0 - 08/02/2013
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class UserDAO {
  
  private ObjectContainer db;
  
  
  public UserDAO() {
    db = DB.openSocketClient();
  }
  
  
  public UserDAO(ObjectContainer db) {
    this.db = db;
  }
  
  
  public User getUserFromStdin() {
    System.out.println("Please insert user required data.");
      User user = new User();
    try (
      BufferedReader reader = new BufferedReader(
          new InputStreamReader(System.in));) {
      
      user.setName(read("* Name\t\t: ", reader));
      user.setEmail(read("* E-mail\t: ", reader));
      user.setPassword(new Password(read("* Password\t: ", reader)));
      user.setAdmin(Boolean.parseBoolean(
          read("* isAdmin\t: ", reader)));
      this.addPermissions(user, reader);
      
    } catch(IOException ex) {
      ex.printStackTrace();
    }
    return user;
  }
  
  
  private String read(String message, BufferedReader reader) throws IOException {
    if(message == null || reader == null)
      return null;
    String str = null;
    do {
      System.out.print(message);
      str = reader.readLine();
      if(str == null || str.trim().isEmpty())
        System.out.println("# Invalid value. Please try again.");
    } while(str == null || str.trim().isEmpty());
    return str;
  }
  
  
  public void addPermissions(User user, BufferedReader reader) throws IOException {
    if(user == null || reader == null) return;
    System.out.println("\nConfigure User Permissions.");
    String menu = createMenu();
    int opt = 0;
    do {
      System.out.println(menu);
      opt = readInt("* Option: ", reader);
      parseOption(opt, user);
      System.out.println();
    } while(opt != 0);
  }
  
  
  private int readInt(String message, BufferedReader reader) throws IOException {
    if(reader == null) return -1;
    String str = null;
    int i = -1;
    do {
      str = read(message, reader);
      try { i = Integer.parseInt(str); }
      catch(NumberFormatException e) {
        System.out.println("# Invalid Option. Please try again.");
      }
    } while(i < 0);
    return i;
  }
  
  
  private String createMenu() {
    return "Choose one option:\n"
        + " 1 . "+ AccessType.ALL.name()+ " (Admin Like)\t"
        + " 2 . "+ AccessType.CPU.name()+ "\n"
        + " 3 . "+ AccessType.CHART_CPU.name()+ "\t\t"
        + " 4 . "+ AccessType.PROCESSES.name()+ "\n"
        + " 5 . "+ AccessType.PROCESS_KILL.name()+ "\t"
        + " 6 . "+ AccessType.DISK.name()+ "\n"
        + " 7 . "+ AccessType.CHART_DISK.name()+ "\t\t"
        + " 8 . "+ AccessType.MEMORY.name()+ "\n"
        + " 9 . "+ AccessType.CHART_MEMORY.name()+ "\t"
        + " 10. "+ AccessType.MEMORY_CLEAR.name()+ "\n"
        + " 11. "+ AccessType.NETWORK.name()+ "\t\t"
        + " 12. "+ AccessType.CHART_NETWORK.name()+ "\n"
        + " 13. "+ AccessType.TRAFFIC.name()+ "\t\t"
        + " 14. "+ AccessType.NOTIFICATION.name()+ "\n"
        + " 15. "+ AccessType.TERMINAL.name()+ "\t\t"
        + " 16. "+ AccessType.USER.name()+ "\n"
        + " 0 . Exit\n";
  }
  
  
  private void parseOption(int option, User user) {
    if(option < 0 || user == null) return;
    
    switch(option) {
      case 1:
        user.getAccess().add(new Access(AccessType.ALL));
        break;
      case 2:
        user.getAccess().add(new Access(AccessType.CPU));
        break;
      case 3:
        user.getAccess().add(new Access(AccessType.CHART_CPU));
        break;
      case 4:
        user.getAccess().add(new Access(AccessType.PROCESSES));
        break;
      case 5:
        user.getAccess().add(new Access(AccessType.PROCESS_KILL));
        break;
      case 6:
        user.getAccess().add(new Access(AccessType.DISK));
        break;
      case 7:
        user.getAccess().add(new Access(AccessType.CHART_DISK));
        break;
      case 8:
        user.getAccess().add(new Access(AccessType.MEMORY));
        break;
      case 9:
        user.getAccess().add(new Access(AccessType.CHART_MEMORY));
        break;
      case 10:
        user.getAccess().add(new Access(AccessType.MEMORY_CLEAR));
        break;
      case 11:
        user.getAccess().add(new Access(AccessType.NETWORK));
        break;
      case 12:
        user.getAccess().add(new Access(AccessType.CHART_NETWORK));
        break;
      case 13:
        user.getAccess().add(new Access(AccessType.TRAFFIC));
        break;
      case 14:
        user.getAccess().add(new Access(AccessType.NOTIFICATION));
        break;
      case 15:
        user.getAccess().add(new Access(AccessType.TERMINAL));
        break;
      case 16:
        user.getAccess().add(new Access(AccessType.USER));
        break;
    }
  }
  
  
  public List<User> listAll() {
    Query q = db.query();
    q.constrain(User.class);
    return DB.query(q, db);
  }
  
  
  public User queryUserByEmail(String email) {
    if(email == null) return null;
    Query q = db.query();
    q.constrain(User.class);
    q.descend("email").constrain(email);
    List<User> list = DB.query(q, db);
    if(list == null || list.isEmpty())
      return null;
    return list.get(0);
  }
  
  
  public List<User> queryUserByName(String name) {
    if(name == null) return null;
    Query q = db.query();
    q.constrain(User.class);
    q.descend("name").constrain(name);
    return DB.query(q, db);
  }
  
  
  public List<User> queryUserByAdmin(boolean admin) {
    Query q = db.query();
    q.constrain(User.class);
    q.descend("admin").constrain(admin);
    return DB.query(q, db);
  }
  
  
  public List<User> queryUserByAccess(AccessType type) {
    Query user = db.query();
    user.constrain(User.class);
    Query access = user.descend("access");
    access.constrain(Access.class);
    access.descend("type").constrain(type);
    return DB.query(user, db);
  }
  
  
  public boolean deleteUserByEmail(String email) {
    User u = this.queryUserByEmail(email);
    if(u == null) return false;
    db.delete(u);
    db.commit();
    return true;
  }
  
  
  public boolean deleteUser(User u) {
    if(u == null) return false;
    db.delete(u);
    db.commit();
    return true;
  }
  
  
  public boolean saveUser(User user) {
    if(user == null) return false;
    User bkp = user.clone();
    this.deleteUserByEmail(user.getEmail());
    db.store(bkp);
    db.commit();
    return true;
  }
  
  
  public boolean testPassword(User u, String password) {
    if(u == null || u.getPassword() == null
        || password == null)
      return false;
    return u.getPassword()
        .validate(new Password(password));
  }
  
  
  public static void main(String[] args) {
    ObjectContainer db = DB.openSocketClient();
    
    /*
    User juno = new User()
        .setName("Juno Roesler")
        .setEmail("juno.rr@gmail.com")
        .setAdmin(true)
        .setPass(new Password("inadonuj"));
    juno.access().add(new Access(AccessType.ALL));
    //db.store(juno);
    //db.commit();
    
    juno = new User()
        .setName("Leitão")
        .setEmail("leitao@pserver.us")
        .setAdmin(false)
        .setPass(new Password("0988"));
    juno.access().add(new Access(AccessType.MEMORY));
    //db.store(juno);
    //db.commit();
    
    System.out.println("* inserted: "+ juno);
    
    Query q = db.query();
    q.constrain(User.class);
    //q.descend("name").constrain("Juno").contains();
    System.out.println("* query...");
    List<User> us = DB.query(q, db);
    for(User u : us) {
      System.out.println(" - found: "+ u);
      for(Access ac : u.access())
        System.out.println("     access: "+ ac.getType());
      System.out.println("   user.contains( MEMORY ): "+ u.contains(AccessType.MEMORY));
      //db.delete(u);
    }
    */
    
    UserDAO dao = new UserDAO();
    List<User> list = dao.queryUserByAccess(AccessType.ALL);
    System.out.println("* Users: "+ list.size());
    for(User u : list)
      System.out.println(" - "+ u.toString());
    
    
    db.commit();
    db.close();
  }
  
}
