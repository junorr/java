/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca e um software livre; voce pode redistribui-la e/ou modifica-la sob os
 * termos da Licenca Publica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versao 2.1 da Licenca, ou qualquer
 * versao posterior.
 * 
 * Esta biblioteca eh distribuida na expectativa de que seja util, porem, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implicita de COMERCIABILIDADE
 * OU ADEQUACAO A UMA FINALIDADE ESPECIFICA. Consulte a Licença Publica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voce deve ter recebido uma copia da Licença Publica Geral Menor do GNU junto
 * com esta biblioteca; se nao, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereco 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.tools;

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
