/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;


/**
 *
 * @author juno
 */
public class TestByteBuddy {
  
  public static class MyString {
    public String toString() {
      return "Hello ByteBuddy from Runtime Created Unknown Class!!!";
    }
  }

  public static void main(String[] args) throws Exception {
    ByteBuddyAgent.installOnOpenJDK();
    ByteBuddy buddy = new ByteBuddy();
    Class<?> myruntype = buddy.redefine(MyString.class)
        .name(String.class.getName())
        //.method(ElementMatchers.named("toString"))
        //.intercept(FixedValue.value("Hello ByteBuddy from Runtime Created Unknown Class!!!"))
        .make()
        //.load(TestByteBuddy.class.getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST_PERSISTENT)
        .load(String.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
        .getLoaded();
    System.out.println(myruntype.getName());
    System.out.println(myruntype.newInstance());
  }
  
}
