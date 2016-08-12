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

package br.com.bb.micro.test;

import br.com.bb.disec.micro.json.JsonDouble;
import br.com.bb.disec.micro.util.FileSize;
import br.com.bb.disec.micro.util.FileSize.Unit;
import br.com.bb.disec.micro.util.RDouble;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/07/2016
 */
public class TestFileSize {

  
  public static void main(String[] args) {
    FileSize size = new FileSize(35, Unit.GB);
    DecimalFormat df = new DecimalFormat("#,##0.0#####", new DecimalFormatSymbols(Locale.US));
    System.out.println("* size (B) : "+ df.format(size.getSize())+ " bytes");
    System.out.println("* size (UN): "+ size.toString());
    System.out.println("* size (KB): "+ df.format(RDouble.of(size.getAs(Unit.KB)).round(2)));
    System.out.println("* size (MB): "+ df.format(RDouble.of(size.getAs(Unit.MB)).round(2)));
    System.out.println("* size (GB): "+ df.format(RDouble.of(size.getAs(Unit.GB)).round(2)));
    System.out.println("* size (TB): "+ df.format(RDouble.of(size.getAs(Unit.TB)).round(4)));
    
    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(Double.class, new JsonDouble())
        .registerTypeAdapter(FileSize.class, FileSize.converter())
        .create();
    System.out.println("* json: "+ gson.toJson(size));
  }
  
}
