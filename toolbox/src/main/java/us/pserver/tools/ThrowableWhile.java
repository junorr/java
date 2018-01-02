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

import us.pserver.tools.function.ThrowableConsumer;
import us.pserver.tools.function.ThrowableFunction;
import us.pserver.tools.function.ThrowablePredicate;
import us.pserver.tools.function.ThrowableTask;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 02/12/2017
 */
public class ThrowableWhile<A> {

  private A attachment;
  
  private final ThrowablePredicate<A> predicate;
  
  
  public ThrowableWhile(A attachment, ThrowablePredicate<A> predicate) {
    this.attachment = attachment;
    this.predicate = NotMatch.notNull(predicate).getOrFail("Bad null Predicate");
  }
  
  
  public void go(ThrowableFunction<A,A> exec) throws Exception {
    while(predicate.test(attachment)) {
      attachment = exec.apply(attachment);
    }
  }
  
  
  public void go(ThrowableConsumer<A> exec) throws Exception {
    while(predicate.test(attachment)) {
      exec.accept(attachment);
    }
  }
  
  
  public void go(ThrowableTask task) throws Exception {
    while(predicate.test(attachment)) {
      task.run();
    }
  }
  
}
