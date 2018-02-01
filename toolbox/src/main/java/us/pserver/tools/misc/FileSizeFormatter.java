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

package us.pserver.tools.misc;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 */
public class FileSizeFormatter {

	private static final double[] sizes = new double[]{1, 1024, 1024*1024, 1024*1024*1024};
	
	private static final String[] units = new String[]{" Bytes", " KB", " MB", " GB"};

	private final DecimalFormat fmt;
	
	
	public FileSizeFormatter() {
		fmt = new DecimalFormat("#,##0.00");
		DecimalFormatSymbols ds = fmt.getDecimalFormatSymbols();
		ds.setDecimalSeparator(',');
		ds.setGroupingSeparator('.');
		fmt.setDecimalFormatSymbols(ds);
	}
	
	
	public String format(long size) {
		for(int i = 0; i < sizes.length -1; i++) {
			if(size <= sizes[i+1]) {
				return fmt.format(size/sizes[i])+ units[i];
			}
		}
		return fmt.format(size/sizes[sizes.length-1])+ units[units.length-1];
	}
  
  
  public static void main(String[] args) {
    long size = 15_000_000_000L;
    System.out.println("* size="+ size);
    System.out.println(new FileSizeFormatter().format(size));
    
    size = 15_000_000L;
    System.out.println("* size="+ size);
    System.out.println(new FileSizeFormatter().format(size));
    
    size = 15_000L;
    System.out.println("* size="+ size);
    System.out.println(new FileSizeFormatter().format(size));
    
    size = 150L;
    System.out.println("* size="+ size);
    System.out.println(new FileSizeFormatter().format(size));
    
    DateFormat df = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss''");
    System.out.println("* df.format(): "+ df.format(new Date()));
  }
  
}
