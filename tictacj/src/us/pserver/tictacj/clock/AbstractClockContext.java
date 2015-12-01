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

package us.pserver.tictacj.clock;

import java.io.PrintStream;
import us.pserver.tictacj.Clock;
import us.pserver.tictacj.ClockContext;
import us.pserver.tictacj.SharedMemory;
import us.pserver.tictacj.util.NotNull;


/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/11/2015
 */
public abstract class AbstractClockContext implements ClockContext {
	
	
	protected Clock clock;
	
	protected PrintStream stdout;
	
	
	protected AbstractClockContext(Clock clk) {
		this(clk, System.out);
	}
	
	
	protected AbstractClockContext(Clock clk, PrintStream out) {
		clock = NotNull.of(clk).getOrFail();
		stdout = NotNull.of(out).getOrFail();
	}


	@Override
	public Clock clock() {
		return clock;
	}


	@Override
	public PrintStream stdout() {
		return stdout;
	}


	@Override
	public SharedMemory sharedMemory() {
		return new StaticSharedMemory();
	}

}
