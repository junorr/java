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

package us.pserver.tictacj.log4j;

import java.io.PrintStream;
import org.slf4j.LoggerFactory;
import us.pserver.log.Logging;
import us.pserver.tictacj.Clock;
import us.pserver.tictacj.clock.AbstractClockContext;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/11/2015
 */
public class Log4jClockContext extends AbstractClockContext {
	
	
	public Log4jClockContext(Clock clk) {
		super(clk);
		Logging.configureLogger();
	}
	
	
	public Log4jClockContext(Clock clk, PrintStream out) {
		super(clk, out);
		Logging.configureLogger();
	}

	
	@Override
	public org.slf4j.Logger logger(Class cls) {
		return LoggerFactory.getLogger(cls);
	}
	
	
	public static void main(String[] args) {
		Log4jClockContext ctx = new Log4jClockContext(null);
		ctx.logger(ctx.getClass()).info("Testing Log4j with Slf4j: {}", "Hello");
	}
	
}
