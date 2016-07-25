/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.pkgbyte.buddy;

import java.util.LinkedList;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.NegatingMatcher;



/**
 *
 * @author juno
 */
public class TestByteBuddy {
	
  public static void main(String[] args) throws Exception {
		Class<Wraping> wcls = new ByteBuddy()
				.subclass(Wraping.class)
				.method(ElementMatchers.isDeclaredBy(Wraping.class).and(new NegatingMatcher(ElementMatchers.isStatic())))
				.intercept(MethodDelegation.to(Wrapper.class))
				.make()
				.load(TestByteBuddy.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();
		Wraping w = wcls.newInstance();
		System.out.println(w.toString());
		System.out.println(w.toInt());
		w.sort(new LinkedList());
  }

}
