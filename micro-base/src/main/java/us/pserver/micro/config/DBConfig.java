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

package us.pserver.micro.config;

import java.nio.file.Path;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/04/2018
 */
public interface DBConfig {

  public static enum DBType {
    FILE, MEMORY, MAPPED_FILE
  }

  public int getMicrodbConcurrency();
  
  public Path getMicrodbFile();
  
  public DBType getMicrodbType();
  
  public String getMicrodbUser();
  
  public String getMicrodbPassword();
  
  
  public default DB createDB() {
    switch(getMicrodbType()) {
      case FILE:
        return DBMaker.fileDB(getMicrodbFile().toFile())
            .concurrencyScale(getMicrodbConcurrency())
            .make();
      case MAPPED_FILE:
        return DBMaker.fileDB(getMicrodbFile().toFile())
            .concurrencyScale(getMicrodbConcurrency())
            .fileMmapEnable()
            .make();
      default:
        return DBMaker.memoryDirectDB()
            .concurrencyScale(getMicrodbConcurrency())
            .make();
    }
  }
  
}
