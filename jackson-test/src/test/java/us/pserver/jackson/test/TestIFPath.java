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

package us.pserver.jackson.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.jackson.test.bean.IFPath;
import us.pserver.jackson.test.bean.IFSize;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 23/04/2018
 */
public class TestIFPath {

  private static final Path file = Paths.get("D:/Euro_Truck_Simulator_2_LinuxMint.7z");
  
  private static final IFPath path = IFPath.from(file);
  
  private static final ObjectMapper mpr = createOM();
  
  private static final String json = "{\"path\":\"D:\\\\Euro_Truck_Simulator_2_LinuxMint.7z\",\"owner\":\"juno\",\"group\":\"-\",\"time\":{\"creationTime\":\"2017-12-07T22:33:03.131903Z\",\"lastModifiedTime\":\"2017-12-07T22:33:03.609279Z\",\"lastAccessTime\":\"2017-12-07T22:33:03.131903Z\"},\"permissions\":\"644\",\"size\":65514643,\"directory\":false}";
  
  
  private static ObjectMapper createOM() {
    ObjectMapper mapper = new ObjectMapper()
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    return mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }
  
  
  @Test
  public void testFPathToJson() throws JsonProcessingException, IOException {
    String jfs = mpr.writeValueAsString(path);
    System.out.printf("* IFSize.from( %s ): [%s].toJson: %s%n", file, path, jfs);
    Assertions.assertEquals(json, jfs);
  }
  
  @Test
  public void testFPathFromJson() throws JsonProcessingException, IOException {
    IFPath fpt = mpr.readValue(json, IFPath.class);
    System.out.printf("* IFSize.from( %s ): [%s].fromJson: %s%n", file, path, fpt);
    Assertions.assertEquals(path, fpt);
  }
  
}
