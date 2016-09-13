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

package br.com.bb.disec.micro.conf;

import br.com.bb.disec.micro.db.DefaultDBSqlSource;
import br.com.bb.disec.micro.db.PoolFactory;
import br.com.bb.disec.micro.db.SqlQuery;
import br.com.bb.disec.micro.util.FileSize;
import br.com.bb.disec.micro.db.SqlObjectType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/07/2016
 */
public class FileUploadConfig {
  
  public static final String SQL_GROUP = "disecMicro";
  
  public static final String SQL_FIND_UPLOAD = "findUpload";
  

  private final FileSize maxSize;
  
  private final Path uploadDir;
  
  private final List<String> allowedExtensions;
  
  
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


  public FileSize getMaxSize() {
    return maxSize;
  }


  public Path getUploadDir() {
    return uploadDir;
  }


  public List<String> getAllowedExtensions() {
    return allowedExtensions;
  }
  

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

  
  
  
  
  public static class Builder {
    
    private FileSize maxSize;
    
    private String uploadDir;
    
    private List<String> allowedExtensions;
    

    public FileSize getMaxSize() {
      return maxSize;
    }


    public Builder setMaxSize(FileSize fs) {
      this.maxSize = fs;
      return this;
    }


    public String getUploadDir() {
      return uploadDir;
    }


    public Builder setUploadDir(String uploadDir) {
      this.uploadDir = uploadDir;
      return this;
    }


    public List<String> getAllowedExtensions() {
      return allowedExtensions;
    }


    public Builder setAllowedExtensions(List<String> allowedExtensions) {
      this.allowedExtensions = allowedExtensions;
      return this;
    }


    public FileUploadConfig build() {
      return new FileUploadConfig(maxSize, uploadDir, allowedExtensions);
    }
    
    
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
    
    
    public Builder load(String aplicName) throws IOException {
      if(aplicName == null) {
        throw new IllegalArgumentException("Bad Aplic Name: "+ aplicName);
      }
      try {
        SqlQuery query = new SqlQuery(
            PoolFactory.getDefaultPool().getConnection(), 
            new DefaultDBSqlSource()
        );
        JsonObject json = readJson(query.execResultSet(
            SQL_GROUP, SQL_FIND_UPLOAD, aplicName)
        );
        query.close();
        if(json.size() == 0) {
          throw new IOException("Config Not Found For Aplic Name: "+ aplicName);
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
