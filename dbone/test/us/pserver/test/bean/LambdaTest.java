/************************************************************************
 * Copyright (C) 2017 Juno Roelser - juno.rr@gmail.com                  *                            
 * This program is free software: you can redistribute it and/or modify *
 * it under the terms of the GNU General Public License as published by *
 * the Free Software Foundation, either version 3 of the License, or    *
 * (at your option) any later version.                                  *
 * This program is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of       *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the        *
 * GNU General Public License for more details.                         *
 * You should have received a copy of the GNU General Public License    *
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.*
 ************************************************************************/

package us.pserver.test.bean;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.Test;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/12/2017
 */
public class LambdaTest {

  public static class One {
    private final int num;
    public One(int i) {
      num = i;
    }
    public String toString() {
      return String.format("One{%d}", num);
    }
    public static One of(int i) {
      return new One(i);
    }
  }
  
  @Test
  public void applicableLambda() {
    Arrays.asList(One.of(1), One.of(2), One.of(3)).stream().map(One::toString);
    One one = One.of(1);
    Function<One,String> fun = One::toString;
    Supplier<String> sup = one::toString;
    
  }
  
}
