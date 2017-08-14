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

package us.pserver.morphia.test;

import java.util.List;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import us.pserver.morphia.test.bean.DefUser;
import us.pserver.morphia.test.bean.Transaction;
import us.pserver.morphia.test.bean.User;
import us.pserver.morphia.test.bean.UserBuilder;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/08/2017
 */
public class Main {

  private final Morphia morphia;
  
  private final Datastore db;
  
  
  public Main() {
    morphia = new Morphia();
    morphia.mapPackage("us.pserver.morphia.test.bean");
    db = morphia.createDatastore(MongoConnectionPool.mongoClient(), "morphia-test-bean");
    db.ensureIndexes();
  }
  
  
  public User createUser() {
    return new UserBuilder()
        .withName("juno")
        .withPassword("inadonuj")
        .newAccess("testObjectBox.jar")
        .authorize(Transaction.JAR).insert()
        .newAccess("testobjectbox.Message")
        .authorize(Transaction.CLASS).insert()
        .newAccess().authorize(Transaction.SET_FIELD).insert()
        .create();
  }
  
  
  public User updateUser() {
    return new UserBuilder()
        .withName("juno")
        .withPassword("inadonuj")
        .newAccess("testObjectBox.jar")
        .authorize(Transaction.JAR).insert()
        .newAccess("testobjectbox.Message")
        .authorize(Transaction.CLASS).insert()
        .newAccess().authorize(Transaction.METHOD).insert()
        .create();
  }
  
  
  public List<DefUser> queryUserName() {
    Query<DefUser> query = db.createQuery(DefUser.class);
    query.field("name").startsWith("j").and(query.criteria("name").endsWith("no"));
    return query.asList();
  }
  
  
  public void insert(User user) {
    db.save(user);
  }
  
  
  public void updateAccess(User user) {
    Query<DefUser> query = db.createQuery(DefUser.class);
    query.field("name").startsWith("j").and(query.criteria("name").endsWith("no"));
    UpdateOperations<DefUser> update = db.createUpdateOperations(DefUser.class).set("access", user.access());
    db.update(query, update);
  }
  
  
  public static void main(String[] args) {
    Main main = new Main();
    User usr = main.createUser();
    System.out.println(usr);
    System.out.println("* Inserting user...");
    //main.insert(usr);
    System.out.println("* Quering by name...");
    List<DefUser> us = main.queryUserName();
    us.forEach(System.out::println);
    usr = main.updateUser();
    System.out.println("* Updated User: "+ usr);
    System.out.println("* update access...");
    main.updateAccess(usr);
    System.out.println("* Quering by name...");
    us = main.queryUserName();
    us.forEach(System.out::println);
  }
  
}
