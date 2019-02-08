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

package us.pserver.orb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/02/2019
 */
@FunctionalInterface
public interface DataSource<T> {

  public T getSource() throws Exception;
  
  
  
  public static DataSource<Reader> ofInputStream(InputStream input) {
    return () -> new BufferedReader(new InputStreamReader(input));
  }
  
  public static DataSource<Reader> ofFile(Path path) {
    return () -> Files.newBufferedReader(path, StandardCharsets.UTF_8);
  }
  
  public static DataSource<Reader> ofClassPath(String resource) {
    return ofClassPath(resource, Orb.class.getClassLoader());
  }
  
  public static DataSource<Reader> ofClassPath(String resource, ClassLoader loader) {
    return ofInputStream(loader.getResourceAsStream(resource));
  }
  
  public static DataSource<Map<String,String>> ofEnv() {
    return () -> System.getenv();
  }
  
  public static DataSource<Properties> ofSystemProps() {
    return () -> System.getProperties();
  }
  
}
