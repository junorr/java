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

package us.pserver.tools;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public class While<A> {

  private A attachment;
  
  private final Predicate<A> predicate;
  
  
  public While(A attachment, Predicate<A> predicate) {
    this.attachment = attachment;
    this.predicate = NotNull.of(predicate).getOrFail("Bad null Predicate");
  }
  
  
  public void go(Function<A,A> exec) {
    while(predicate.test(attachment)) {
      attachment = exec.apply(attachment);
    }
  }
  
  
  public void go(Consumer<A> exec) {
    while(predicate.test(attachment)) {
      exec.accept(attachment);
    }
  }
  
  
  public void go(Runnable task) {
    while(predicate.test(attachment)) {
      task.run();
    }
  }
  
}
