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

package us.pserver.neo4j.test;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import us.pserver.dyna.ResourceLoader;
import us.pserver.neo4j.test.bean.Role;
import us.pserver.neo4j.test.bean.User;
import us.pserver.neo4j.test.bean.UserBuilder;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 16/08/2017
 */
public class Main {
  
  private static final String[] PACKAGES = {
    "us.pserver.neo4j.test.bean"
  };
  
  private final SessionFactory factory;
  
  
  public Main() throws IOException {
    Configuration conf = new Configuration();
    Properties props = new Properties();
    props.load(ResourceLoader.caller().loadStream("/resources/ogm.properties"));
    conf.driverConfiguration()
        .setDriverClassName(props.getProperty("driver"))
        .setURI(props.getProperty("uri"))
        .setCredentials(
            props.getProperty("username"), 
            props.getProperty("password")
        );
    factory = new SessionFactory(conf, PACKAGES);
  }
  
  
  public Session getSession() {
    return factory.openSession(); 
  }
  
  
  public User createUser() {
    return new UserBuilder()
        .withName("Juno")
        .withPassword("32132155")
        .newAccess("testObjectBox.jar")
        .authorize(Role.JAR)
        .insert()
        .newAccess("testobjectbox.Message")
        .authorize(Role.CLASS)
        .insert()
        .newAccess("msg")
        .deny(Role.SET_FIELD)
        .insert()
        .create();
  }

  
  public User updateUser() {
    return new UserBuilder()
        .withName("Juno")
        .withPassword("32132155")
        .newAccess("testObjectBox.jar")
        .authorize(Role.JAR)
        .insert()
        .newAccess("testobjectbox.Message")
        .authorize(Role.CLASS)
        .insert()
        .newAccess("msg")
        .deny(Role.SET_FIELD)
        .insert()
        .newAccess("setMessage")
        .deny(Role.METHOD)
        .insert()
        .create();
  }


  public static void main(String[] args) throws IOException {
    Main main = new Main();
    User usr = main.createUser();
    System.out.println(usr);
    System.out.println("* Inserting user...");
    Session sess = main.getSession();
    usr.accesses().forEach(sess::save);
    sess.save(usr);
    System.out.println("* Quering by name...");
    String query = "match (u:User) where u.name = 'Juno' return u";
    System.out.println("    "+ query);
    Iterable<User> us = sess.query(User.class, query, Collections.EMPTY_MAP);
    us.forEach(System.out::println);
    usr = us.iterator().next();
    User upd = main.updateUser().clone(usr.getId());
    System.out.println("* Updated User: "+ upd);
    System.out.println("* updating...");
    sess.save(upd);
    System.out.println("* Quering by name...");
    us = sess.query(User.class, query, Collections.EMPTY_MAP);
    us.forEach(System.out::println);
  }
  
}
