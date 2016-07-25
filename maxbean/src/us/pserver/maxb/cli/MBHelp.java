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

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 21/02/2016
 */
public class MBHelp {

  private final StringBuilder help;
  
  
  public MBHelp() {
    help = new StringBuilder()
        //       1   5   10   15   20   25   30   35   40   45   50   55   60   65   70   75
        .append("Usage: maxbean <-d|--db-*> <-s|-f> [-c] [-h] [-i] [-I] [-k] [-o] [-t] [-v]\n")
        .append("  -c (--columns)   VAL: List of additional columns than those included with\n")
        .append("                        database table. The accepted format is \n")
        .append("                        'table1.column1=sqlType,tableN.columnN=sqlType...'\n")
        .append("  -d (--db-config) VAL: Database connection configuration (.properties file)\n")
        .append("  --db-driver      VAL: Database Driver configuration \n")
        .append("                        (e.g. 'com.mysql.jdbc.Driver')\n")
        .append("  --db-password    VAL: Database password\n")
        .append("  --db-url         VAL: Database URL connection \n")
        .append("                        (e.g. 'jdbc:mysql://localhost:3306')\n")
        .append("  --db-user        VAL: Database user\n")
        .append("  -f (--file)      VAL: Properties file with schema information to generate source code\n")
        .append("  -h (--help)         : Show this help message\n")
        .append("  -i (--inspect)   VAL: Inspect the database and create a properties file with \n")
        .append("                        schema information\n")
        .append("  -I (--identation)VAL: Set the number of spaces for source code identation\n")
        .append("  -k (--package)   VAL: Java package for the generated source code\n")
        .append("  -o (--output)    VAL: Output directory for MaxBean generated files\n")
        .append("  -s (--schema)    VAL: Database Schema to inspect\n")
        .append("  -t (--tables)    VAL: List of schema tables to inspect \n")
        .append("                        (comma separated: 'arg1,arg2,argN...')\n")
        .append("  -v (--version)      : Show MaxBean version information\n\n")
        .append("Usage Examples:\n")
        .append(" * Generate source code for all tables in a schema database:\n")
        .append("   > maxbean -d db.properties -s myschema -k com.mypack.ex\n")
        .append(" * Inspect the database and generate a relative properties file:\n")
        .append("   > maxbean -d db.properties -s myschema -i\n")
        .append(" * Generate source code from a inspection file:\n")
        .append("   > maxbean -d db.properties -f myschema.properties -k com.mypack.ex\n")
        .append(" * Generate source code from only a couple tables:\n")
        .append("   > maxbean -d db.properties -s myschema -k com.mypack.ex\n")
        .append("   >   -t \"table_one,table_two\"\n")
        .append(" * Generate source code with additional columns not present in tables:\n")
        .append("   > maxbean -d db.properties -s myschema -k com.mypack.ex\n")
        .append("   >   -c \"table_one.col_x=int,table_two.col_y=varchar,table_two.col_z=date\"\n")
        .append(" * Specifing database options manualy:\n")
        .append("   > maxbean -s myschema -k com.mypack.ex\n")
        .append("   >   --db-driver com.mysql.jdbc.Driver --db-url jdbc:mysql://localhost:3306\n")
        .append("   >   --db-user myuser --db-password mypassword\n");
  }
  
  
  @Override
  public String toString() {
    return help.toString();
  }
  
}
