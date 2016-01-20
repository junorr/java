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

package us.pserver.streams.test;

import java.io.IOException;
import java.util.function.Consumer;
import us.pserver.streams.SearchableInputStream;
import us.pserver.streams.SequenceInputStream;
import us.pserver.streams.StreamUtils;
import us.pserver.tools.Bean;
import us.pserver.tools.RoundDouble;
import us.pserver.tools.io.NullOutput;
import us.pserver.tools.timer.Timer;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 19/06/2015
 */
public class TestBenchLimitedBufferVSSearchableIS {
	
	
	public static void searchableIS(final Bean<Double> time) {
    byte[] stop = {-12, -11, -10};
		final Timer tm = new Timer.Nanos();
    SearchableInputStream sin = new SearchableInputStream(
        new SequenceInputStream(255), stop,
				stream -> {
					tm.lapAndStop();
					//System.out.println(tm);
					time.plus(tm.elapsedMillis());
				}
    );
    int read = 0;
		try {
			tm.start();
			while(true) {
				read = sin.read();
				if(read == -1) break;
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

  
	public static void limitedBuffer(final Bean<Double> time) {
    byte[] stop = {-12, -11, -10};
		final Timer tm = new Timer.Nanos();
    SequenceInputStream sis = new SequenceInputStream(255);
		try {
			tm.start();
			StreamUtils.transferUntil(sis, NullOutput.out, stop);
			tm.lapAndStop();
			time.plus(tm.elapsedMillis());
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void bench(Consumer<Bean<Double>> cs) {
    System.out.println("* Test bench for 255 bytes:");
		System.out.println("  Warming 10x...");
		Bean<Double> time = new Bean(0.0);
		for(int i = 0; i < 10; i++) {
			cs.accept(time);
		}
		System.out.println("  Running 5x...");
		time = new Bean(0.0);
		for(int i = 0; i < 5; i++) {
			cs.accept(time);
		}
		System.out.println("* Average: "+ new RoundDouble((time.get() / 15.0), 4)+ "ms");
	}

  
  public static void main(String[] args) throws IOException {
    System.out.println("* SearchableInputStream:");
		bench(TestBenchLimitedBufferVSSearchableIS::searchableIS);
		System.out.println("------------------------------------------");
		
    System.out.println("* LimitedBuffer:");
		bench(TestBenchLimitedBufferVSSearchableIS::limitedBuffer);
  }
  
}
