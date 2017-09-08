/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.nitrite;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;


/**
 *
 * @author juno
 */
public class TestNitrite {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    Nitrite db = Nitrite.builder()
        .filePath("/storage/java/nitrite.db")
        .openOrCreate("hello", "world");
    ObjectRepository<User> repo = db.getRepository(User.class);
    User user = new UserBuilder()
        .withName("juno")
        .withPassword("32132155")
        .newAccess("say")
        .authorize(Role.METHOD)
        .insert()
        .create();
    System.out.println(user);
    System.out.println(repo.insert(user));
    user = repo.find(ObjectFilters.eq("name", "juno")).firstOrDefault();
    System.out.println(user);
    db.commit();
    repo.close();
    db.compact();
    db.close();
  }
  
}
