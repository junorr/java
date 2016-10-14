/*
 * Direitos Autorais Reservados (c) 2016 Banco do Brasil S.A.
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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;


/**
 * Configurações relativas ao micro serviço de download de arquivos.
 * Encapsula o caminho do arquivo, contexto e apelido de acesso.
 * Essas informações são armazenadas em banco de dados (103) sob
 * o schema/table disecMicro/download.
 * 
 * @author Juno Roesler - f6036477@bb.com.br
 * @version 20161005
 */
public class FileDownloadConfig {
  
  /**
   * Grupo de queries SQL ("disecMicroServices").
   */
  public static final String SQL_GROUP = "disecMicroServices";
  
  /**
   * SQL para busca dos dados de download ("findDownload").
   */
  public static final String SQL_FIND_DOWNLOAD = "findDownload";
  
  /**
   * SQL para inserção de novo registro de download ("addDownload").
   */
  public static final String SQL_ADD_DOWNLOAD = "addDownload";
  
  
  private final String context;

  private final Path path;
  
  private final String alias;
  
  private final Instant date;


  /**
   * Construtor padrão.
   * @param context Contexto do download.
   * @param path Caminho do arquivo.
   * @param alias Apelido do arquivo (opcional).
   * @param date Data de registro no banco.
   */
  public FileDownloadConfig(String context, Path path, String alias, Instant date) {
    Objects.requireNonNull(context, "Bad Null Context");
    Objects.requireNonNull(path, "Bad Null Path");
    Objects.requireNonNull(date, "Bad Null Date");
    this.context = context;
    this.path = path;
    this.alias = alias;
    this.date = date;
  }


  /**
   * Retorna o contexto sob o qual o arquivo estará disponível.
   * @return String
   */
  public String getContext() {
    return context;
  }


  /**
   * Retorna o caminho do arquivo.
   * @return Path
   */
  public Path getPath() {
    return path;
  }


  /**
   * Retorna o apelido do arquivo (opcional).
   * @return String
   */
  public String getAlias() {
    return alias;
  }


  /**
   * Retorna a data de registro do arquivo no banco.
   * @return Instant
   */
  public Instant getDate() {
    return date;
  }
  
  
  /**
   * Armazena (replace) as informações encapsuladas 
   * no banco de dados.
   * @return Esta instância de FileDownloadConfig.
   * @throws IOException Em caso de erro na 
   * interação com o banco de dados.
   */
  public FileDownloadConfig store() throws IOException {
    SqlQuery query = null;
    try {
      query = new SqlQuery(
          PoolFactory.getDefaultPool().getConnection(), 
          new DefaultFileSqlSource(ResourceLoader.caller())
      );
      query.update(SQL_GROUP, SQL_ADD_DOWNLOAD, 
          this.context, 
          this.path.toAbsolutePath().toString(), 
          this.alias
      );
      query.close();
    }
    catch(SQLException e) {
      throw new IOException(e.toString(), e);
    }
    finally {
      Optional.ofNullable(query)
          .ifPresent(SqlQuery::close);
    }
    return this;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + Objects.hashCode(this.context);
    hash = 67 * hash + Objects.hashCode(this.path);
    return hash;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FileDownloadConfig other = (FileDownloadConfig) obj;
    if (!Objects.equals(this.context, other.context)) {
      return false;
    }
    if (!Objects.equals(this.path, other.path)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "FileDownloadConfig{" + "context=" + context + ", path=" + path + ", alias=" + alias + ", date=" + date + '}';
  }


  /**
   * Método estático para obter uma instância do 
   * objeto construtor de FileDownloadConfig.
   * @return FileDownloadConfig.Builder
   */
  public static Builder builder() {
    return new Builder();
  }



  
  
  
  
  /**
   * Objeto construtor de FileDownloadConfig.
   * Permite construir um objeto de configuração
   * a partir de informações customizadas ou a partir
   * do banco de dados.
   */
  public static class Builder {
    
    private String context;
    
    private Path path;
    
    private String alias;
    
    private Instant date = Instant.now();
    

    /**
     * Retorna o contexto sob o qual o arquivo 
     * estará disponível para download.
     * @return String
     */
    public String getContext() {
      return context;
    }


    /**
     * Define o contexto sob o qual o arquivo 
     * estará disponíovel para download.
     * @param context Contexto.
     * @return Esta instância de Builder.
     */
    public Builder setContext(String context) {
      this.context = context;
      return this;
    }


    /**
     * Retorna o caminho do arquivo.
     * @return Path
     */
    public Path getPath() {
      return path;
    }


    /**
     * Define o caminho do arquivo.
     * @param path Caminho do arquivo (Path).
     * @return Esta instância de Builder.
     */
    public Builder setPath(Path path) {
      this.path = path;
      return this;
    }


    /**
     * Retorna o apelido do arquivo.
     * @return String
     */
    public String getAlias() {
      return alias;
    }


    /**
     * Define o apelido do arquivo.
     * @param alias Apelido do arquivo.
     * @return Esta instância de Builder.
     */
    public Builder setAlias(String alias) {
      this.alias = alias;
      return this;
    }


    /**
     * Retorna a data de registro no banco.
     * @return Instant.
     */
    public Instant getDate() {
      return date;
    }


    /**
     * Define a data de registro do arquivo no banco.
     * @param date Data de registro (Instant).
     * @return Esta instância de Builder.
     */
    public Builder setDate(Instant date) {
      this.date = date;
      return this;
    }


    /**
     * Cria um novo objeto FileDownloadConfig a 
     * partir das informações deste Builder.
     * @return Novo objeto FileDownloadConfig.
     */
    public FileDownloadConfig build() {
      return new FileDownloadConfig(context, path, alias, date);
    }
    
    
    /**
     * Carrega as informações a partir do banco de dados.
     * @param context Contexto ao qual as informações 
     * estão associadas no banco.
     * @return Esta instância de Builder.
     * @throws IOException Em caso de erro na comunicação 
     * com o banco.
     */
    public Builder load(String context) throws IOException {
      if(context == null) {
        throw new IllegalArgumentException("Bad Null Context");
      }
      SqlQuery query = null;
      try {
        query = new SqlQuery(
            PoolFactory.getDefaultPool().getConnection(), 
            new DefaultFileSqlSource(ResourceLoader.caller())
        );
        ResultSet rs = query.execResultSet(SQL_GROUP, SQL_FIND_DOWNLOAD, context);
        rs.next();
        this.context = rs.getString(1);
        this.path = Paths.get(rs.getString(2));
        this.alias = rs.getString(3);
        this.date = Instant.ofEpochMilli(rs.getTimestamp(4).getTime());
        query.close();
      }
      catch(SQLException e) {
        throw new IOException(e.getMessage(), e);
      }
      finally {
        Optional.ofNullable(query)
            .ifPresent(SqlQuery::close);
      }
      return this;
    }
    
  }


  
}
