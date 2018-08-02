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

package us.pserver.orb.xml;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import us.pserver.orb.TypedStrings;
import us.pserver.orb.OrbConfiguration;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/01/2018
 */
public class XmlOrbConfig implements OrbConfiguration {
  
  private final InputStream input;
  
  public XmlOrbConfig(InputStream in) {
    this.input = Objects.requireNonNull(in);
  }
  
  @Override
  public TypedStrings getSupportedTypes() {
    return null;
  }

  @Override
  public Map<String, Object> getValuesMap() {
    try {
      SAXParser ps = SAXParserFactory.newInstance().newSAXParser();
      XmlMapHandler xmh = new XmlMapHandler();
      ps.parse(input, xmh);
      return xmh.getMap();
    }
    catch(Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Override
  public Function<Method, String> getMethodToKeyFunction() {
    return null;
  }
  
  
  
  public static XmlOrbConfig of(String xml) {
    return of(xml, StandardCharsets.UTF_8);
  }
  
  public static XmlOrbConfig of(String xml, Charset cs) {
    return new XmlOrbConfig(new ByteArrayInputStream(xml.getBytes(cs)));
  }
  
  public static XmlOrbConfig of(Path path) throws IOException {
    return new XmlOrbConfig(Files.newInputStream(path, StandardOpenOption.READ));
  }
  
}
