/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.pkgbyte.buddy;

import java.lang.reflect.Method;
import java.util.Arrays;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;



/**
 *
 * @author juno
 */
public class Wrapper {
	
	@RuntimeType
	public static void intercept(@Origin Method meth,  @AllArguments Object ... args) {
		System.out.println("* Method invocation: "+ meth+ ": ");
		Arrays.asList(args).forEach(o->System.out.println(" - "+ o.getClass().getName()+ "="+ o));
		/*if(args != null && args.length == 1) {
			return args[0];
		}
		return null;
		*/
	}
	
}
