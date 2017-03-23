/*
 * Direitos Autorais Reservados (c) 2011 Banco do Brasil S.A.
 * Contato: f6036477@bb.com.br
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

package br.com.bb.disec.micros.conf;

import br.com.bb.disec.micro.ResourceLoader;
import br.com.bb.disec.micro.db.DefaultFileSqlSource;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micros.db.SqlObjectType;
import br.com.bb.disec.micros.util.BashProcExecutor;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


/**
 * Configurações para upload de arquivos.
 * Encapsula informações a respeito do tamanho
 * máximo do arquivo para upload, extensões permitidas
 * e caminho onde o arquivo será salvo.
 * 
 * @author Juno Roesler - f6036477@bb.com.br
 * @version 20160726
 */
public class ProcExecutorConfig {
  
  /**
   * Grupo de consultas SQL no banco ("disecMicroServices").
   */
  public static final String SQL_GROUP = "disecMicroServices";
  
  /**
   * SQL para buscar informações de execução de processos no banco de dados ("findProcExec").
   */
  public static final String SQL_FIND_PROCESS = "findProcExec";
  

  private final BashProcExecutor proc;
  
  private final String command;
  
  private final boolean waitOutput;
  
  
  /**
   * Construtor padrão. 
   * @param pex Executor de processos no bash.
   */
  private ProcExecutorConfig(BashProcExecutor pex, String cmd, boolean waitOutput) {
    if(pex == null) {
      throw new IllegalArgumentException("Bad null BashProcExecutor");
    }
    this.proc = pex;
    this.command = cmd;
    this.waitOutput = waitOutput;
  }


  /**
   * Retorna o tamanho máximo permitido.
   * @return FileSize.
   */
  public BashProcExecutor getProcExecutor() {
    return proc;
  }
  
  
  public boolean isWaitOutput() {
    return waitOutput;
  }
  
  
  public String getCommand() {
    return command;
  }

  
  public static Builder builder() {
    return new Builder();
  }


  
  
  
  
  /**
   * Objeto construtor de FileUploadConfig.
   */
  public static class Builder {
    
    private String command;
    
    private boolean waitOutput;
    

    /**
     * Retorna o comando a ser executado.
     * @return comando a ser executado.
     */
    public String getCommand() {
      return command;
    }
    
    
    public boolean isWaitOutput() {
      return waitOutput;
    }
    
    
    /**
     * Cria e retorna um objeto FileUploadConfig com 
     * as informações deste Builder.
     * @return Nova instância de FileUploadConfig.
     */
    public ProcExecutorConfig build() {
      return new ProcExecutorConfig(new BashProcExecutor(this.command), this.command, this.waitOutput);
    }
    
    
    /**
     * Retorna um objeto json com as 
     * informações do ResultSet informado.
     * @param rst ResultSet.
     * @return Novo objeto json com as 
     * informaçoes do ResultSet.
     * @throws SQLException Em caso de erro 
     * na consulta ao banco de dados.
     */
    private JsonObject readJson(ResultSet rst) throws SQLException {
      JsonObject json = new JsonObject();
      SqlObjectType sot = new SqlObjectType();
      ResultSetMetaData meta = rst.getMetaData();
      int cols = meta.getColumnCount();
      if(rst.next()) {
        for(int i = 1; i <= cols; i++) {
          json.add(meta.getColumnLabel(i), sot.getElement(rst, i));
        }
      }
      return json;
    }
    
    
    /**
     * Carrega informações do banco de dados 
     * no objeto Builder.
     * @param group Nome do grupo de upload
     * @param name Nome do upload
     * @return
     * @throws IOException 
     */
    public Builder load(String group, String name) throws IOException {
      if(group == null) {
        throw new IllegalArgumentException("Bad upload group: "+ group);
      }
      if(name == null) {
        throw new IllegalArgumentException("Bad upload name: "+ name);
      }
      try {
        SqlQuery query = new SqlQuery(
            PoolFactory.getDefaultPool().getConnection(), 
            new DefaultFileSqlSource(ResourceLoader.caller())
        );
        JsonObject json = readJson(query.execResultSet(SQL_GROUP, SQL_FIND_PROCESS, group, name)
        );
        query.close();
        if(json.size() == 0) {
          throw new IOException("Config Not Found For: "+ group+ "."+ name);
        }
        this.command = json.get("command").getAsString();
        this.waitOutput = json.get("wait_output").getAsInt() == 1;
      }
      catch(SQLException e) {
        throw new IOException(e.getMessage(), e);
      }
      return this;
    }
    
  }


  
}
