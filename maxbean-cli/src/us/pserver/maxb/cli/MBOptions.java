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

package us.pserver.maxb.cli;

import java.io.File;
import java.util.List;
import uk.co.flamingpenguin.jewel.cli.Option;
import uk.co.flamingpenguin.jewel.cli.Unparsed;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 18/02/2016
 */
public interface MBOptions {

  @Option(
      shortName = {"c"}, 
      longName = {"dbConfig"}, 
      description = "Database configuration properties file"
  )
  public File getDBConfig();
  public boolean isDBConfig();
  
  @Option(
      longName = {"dbDriver"}, 
      description = "Set the database driver connetcion"
  )
  public String getDBDriver();
  public boolean isDBDriver();
  
  @Option(
      longName = {"dbUrl"}, 
      description = "Set the database url connetcion"
  )
  public String getDBUrl();
  public boolean isDBUrl();
  
  @Option(
      longName = {"dbUser"}, 
      description = "Set the database user"
  )
  public String getDBUser();
  public boolean isDBUser();
  
  @Option(
      longName = {"dbPassword"}, 
      description = "Set the database password"
  )
  public String getDBPassword();
  public boolean isDBPassword();
  
	/*
  @Unparsed(
      name = "Name of Schema to inspect"
  )
	*/
  @Option(
      shortName = {"s"},
      longName = {"schema"}, 
      description = "The Schema name to inspect"
  )
  public String getSchema();
	public boolean isSchema();
  
  @Option(
      shortName = {"t"},
      longName = {"tables"}, 
      description = "A list of table names to inspect"
  )
  public List<String> getTables();
  public boolean isTables();
  
  @Option(
      shortName = {"i"},
      longName = {"inspect"}, 
      description = "Inspect the database and create the properties file"
  )
  public boolean getInspect();
  public boolean isInspect();
  
  @Option(
      shortName = {"p"},
      longName = {"properties"}, 
      description = "Inspection properties file for source code generation"
  )
  public File getProperties();
  public boolean isProperties();
  
  @Option(
      shortName = {"o"},
      longName = {"output"}, 
      description = "Output directory for the generated files"
  )
  public File getOutputDir();
  public boolean isOutputDir();
  
  @Option(
      shortName = {"I"},
      longName = {"identation"}, 
      description = "Identation code level"
  )
  public int getIdentation();
  public boolean isIdentation();
  
  @Option(
      shortName = {"k"},
      longName = {"package"}, 
      description = "The package name for the source code"
  )
  public String getPackage();
  public boolean isPackage();
  
  @Option(
      shortName = {"h"},
			helpRequest = true,
			description = "Show this Help message"
	)
  public boolean getHelp();
  
  @Option(
      shortName = {"v"},
      description = "MaxBean version info"
  )
  public boolean getVersion();
  public boolean isVersion();
  
}
