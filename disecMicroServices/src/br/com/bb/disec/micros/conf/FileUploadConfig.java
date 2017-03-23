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
import br.com.bb.disec.micros.util.FileSize;
import br.com.bb.disec.micros.db.SqlObjectType;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Configurações para upload de arquivos.
 * Encapsula informações a respeito do tamanho
 * máximo do arquivo para upload, extensões permitidas
 * e caminho onde o arquivo será salvo.
 * 
 * @author Juno Roesler - f6036477@bb.com.br
 * @version 20160726
 */
public class FileUploadConfig {
  
  /**
   * Grupo de consultas SQL no banco ("disecMicroServices").
   */
  public static final String SQL_GROUP = "disecMicroServices";
  
  /**
   * SQL para buscar informações de upload no banco de dados ("findUpload").
   */
  public static final String SQL_FIND_UPLOAD = "findUpload";
  

  private final FileSize maxSize;
  
  private final Path uploadDir;
  
  private final List<String> allowedExtensions;
  
  
  /**
   * Construtor padrão. 
   * @param maxSize Tamanho máximo do arquivo.
   * @param uploadDir Diretório onde o arquivo será salvo.
   * @param allowedExtensions Lista de extensões permitidas.
   */
  private FileUploadConfig(
      FileSize maxSize, 
      String uploadDir, 
      List<String> allowedExtensions
  ) {
    if(maxSize == null) {
      throw new IllegalArgumentException("Bad Max Size: "+ maxSize);
    }
    if(uploadDir == null || uploadDir.trim().isEmpty()) {
      throw new IllegalArgumentException("Bad Upload Dir Path: "+ uploadDir);
    }
    this.maxSize = maxSize;
    this.uploadDir = Paths.get(uploadDir);
    this.allowedExtensions = (allowedExtensions == null 
        ? Collections.EMPTY_LIST 
        : allowedExtensions
    );
  }


  /**
   * Retorna o tamanho máximo permitido.
   * @return FileSize.
   */
  public FileSize getMaxSize() {
    return maxSize;
  }

  
  /**
   * Retorna o diretório onde será salvo o arquivo.
   * @return Path
   */
  public Path getUploadDir() {
    return uploadDir;
  }


  /**
   * Retorna a lista de extensões permitidas.
   * @return List&lt;String&gt;
   */
  public List<String> getAllowedExtensions() {
    return allowedExtensions;
  }
  
  
  /**
   * Cria e retorna um objeto construtor
   * Builder de FileUploadConfig.
   * @return Builder.
   */
  public static Builder builder() {
    return new Builder();
  }


  @Override
  public String toString() {
    return "FileUploadConfig{\n" 
        + "    maxSize: " + maxSize 
        + "\n    uploadDir: " + uploadDir 
        + "\n    allowedExtensions: " 
        + allowedExtensions + "\n}";
  }

  
  
  
  
  /**
   * Objeto construtor de FileUploadConfig.
   */
  public static class Builder {
    
    private FileSize maxSize;
    
    private String uploadDir;
    
    private List<String> allowedExtensions;
    

    /**
     * Retorna o tamanho máximo para 
     * upload de arquivos.
     * @return FileSize.
     */
    public FileSize getMaxSize() {
      return maxSize;
    }
    
    
    /**
     * Define o tamanho máximo para upload 
     * de arquivos.
     * @param fs Objeto FileSize encapsulando 
     * tamanho de arquivo.
     * @return Esta instância de Builder.
     */
    public Builder setMaxSize(FileSize fs) {
      this.maxSize = fs;
      return this;
    }
    
    
    /**
     * Retorna o diretório de upload de arquivos.
     * @return String.
     */
    public String getUploadDir() {
      return uploadDir;
    }
    
    
    /**
     * Define o diretório de upload de arquivos.
     * @param uploadDir String
     * @return Esta instância de Builder.
     */
    public Builder setUploadDir(String uploadDir) {
      this.uploadDir = uploadDir;
      return this;
    }
    
    
    /**
     * Retorna uma lista contendo as extensões 
     * de arquivos permitidas.
     * @return List&lt;String&gt;
     */
    public List<String> getAllowedExtensions() {
      return allowedExtensions;
    }


    /**
     * Define lista de extensões de arquivos 
     * permitidas.
     * @param allowedExtensions List&lt;String&gt;
     * @return Esta instância de Builder
     */
    public Builder setAllowedExtensions(List<String> allowedExtensions) {
      this.allowedExtensions = allowedExtensions;
      return this;
    }
    
    
    /**
     * Cria e retorna um objeto FileUploadConfig com 
     * as informações deste Builder.
     * @return Nova instância de FileUploadConfig.
     */
    public FileUploadConfig build() {
      return new FileUploadConfig(maxSize, uploadDir, allowedExtensions);
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
        JsonObject json = readJson(query.execResultSet(
            SQL_GROUP, SQL_FIND_UPLOAD, group, name)
        );
        query.close();
        if(json.size() == 0) {
          throw new IOException("Config Not Found For Aplic Name: "+ group);
        }
        if(json.get("allowed_ext") != null) {
          this.allowedExtensions = Arrays.asList(
              json.get("allowed_ext").getAsString().split(",")
          );
        }
        this.maxSize = new FileSize(json.get("max_size").getAsLong());
        this.uploadDir = json.get("path").getAsString();
      }
      catch(SQLException e) {
        throw new IOException(e.getMessage(), e);
      }
      return this;
    }
    
  }


  
}
