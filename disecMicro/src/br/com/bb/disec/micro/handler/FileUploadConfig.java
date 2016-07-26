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

package br.com.bb.disec.micro.handler;

import br.com.bb.disec.micro.json.JsonDouble;
import br.com.bb.disec.micro.util.FileSize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 26/07/2016
 */
public class FileUploadConfig {

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
    
    
    public Builder load(URL url) throws IOException {
      if(url == null) {
        throw new IllegalArgumentException("Bad URL: "+ url);
      }
      try {
        return load(Paths.get(url.toURI()));
      } catch(URISyntaxException e) {
        throw new IOException(e.getMessage(), e);
      }
    }
    
    
    public Builder load(Path path) throws IOException {
      if(path == null) {
        throw new IllegalArgumentException("Bad Path: "+ path);
      }
      Gson gson = new GsonBuilder()
          .registerTypeAdapter(FileSize.class, FileSize.converter())
          .registerTypeAdapter(Double.class, new JsonDouble())
          .create();
      try (BufferedReader br = Files.newBufferedReader(
          path, Charset.forName("UTF-8"))
          ) {
        Builder b = gson.fromJson(br, Builder.class);
        this.setMaxSize(b.getMaxSize())
            .setUploadDir(b.getUploadDir())
            .setAllowedExtensions(b.getAllowedExtensions());
      }
      return this;
    }
    
    
    public Builder save(URL url) throws IOException {
      if(url == null) {
        throw new IllegalArgumentException("Bad URL: "+ url);
      }
      try {
        return save(Paths.get(url.toURI()));
      } catch(URISyntaxException e) {
        throw new IOException(e.getMessage(), e);
      }
    }
    
    
    public Builder save(Path path) throws IOException {
      if(path == null) {
        throw new IllegalArgumentException("Bad Path: "+ path);
      }
      Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .registerTypeAdapter(FileSize.class, FileSize.converter())
          .registerTypeAdapter(Double.class, new JsonDouble())
          .create();
      try (BufferedWriter fw = Files.newBufferedWriter(
          path, Charset.forName("UTF-8"), 
          StandardOpenOption.WRITE, 
          StandardOpenOption.TRUNCATE_EXISTING, 
          StandardOpenOption.CREATE)
          ) {
        fw.write(gson.toJson(this));
        fw.write('\n');
        fw.flush();
      }
      return this;
    }
    
  }


  
}
