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

package us.pserver.download.util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import us.pserver.download.file.IFPath;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/05/2017
 */
public class URIPath {

  private final URIParam par;
  
  
  public URIPath(URIParam par) {
    if(par == null) {
      throw new IllegalArgumentException("Bad null URIParam");
    }
    this.par = par;
  }
  
  
  public static URIPath of(URIParam par) {
    return new URIPath(par);
  }
  
  
  public IFPath getPath() {
    Path path = null;
    if(par.length() > 1) {
      path = decodePathUTF8(par.getParam(1));
      if(!Files.exists(path)) {
        path = decodePathISO88591(par.getParam(1));
      }
    }
    return IFPath.from(path);
  }
  
  
  protected Path decodePathUTF8(String enc) {
    String spath = new String(
        Base64.getDecoder().decode(enc), 
        StandardCharsets.UTF_8
    );
    return Paths.get(spath);
  }
  

  protected Path decodePathISO88591(String enc) {
    String spath = new String(
        Base64.getDecoder().decode(enc), 
        StandardCharsets.ISO_8859_1
    );
    return Paths.get(spath);
  }
  
}
