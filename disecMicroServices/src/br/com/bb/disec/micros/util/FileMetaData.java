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

package br.com.bb.disec.micros.util;

import java.nio.file.Path;
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/09/2016
 */
public class FileMetaData {

  private final String filename;
  
  private final String token;
  
  private final Path path;
  
  private final long size;
  
  
  public FileMetaData(String filename, String token, Path path, long size) {
    if(filename == null) {
      throw new IllegalArgumentException("Bad Null filename");
    }
    if(token == null) {
      throw new IllegalArgumentException("Bad Null token");
    }
    if(path == null) {
      throw new IllegalArgumentException("Bad Null path");
    }
    this.filename = filename;
    this.token = token;
    this.path = path;
    this.size = size;
  }


  public String getFilename() {
    return filename;
  }


  public String getToken() {
    return token;
  }


  public Path getPath() {
    return path;
  }


  public long getSize() {
    return size;
  }
  
  
  public FileMetaData with(String filename) {
    return new FileMetaData(filename, token, path, size);
  }
  
  
  public FileMetaData withToken(String token) {
    return new FileMetaData(filename, token, path, size);
  }
  
  
  public FileMetaData with(Path path) {
    return new FileMetaData(filename, token, path, size);
  }
  
  
  public FileMetaData with(long size) {
    return new FileMetaData(filename, token, path, size);
  }


  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.token);
    hash = 37 * hash + Objects.hashCode(this.path);
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
    final FileMetaData other = (FileMetaData) obj;
    if (!Objects.equals(this.token, other.token)) {
      return false;
    }
    if (!Objects.equals(this.path, other.path)) {
      return false;
    }
    return true;
  }


  @Override
  public String toString() {
    return "FileMetaData{\n" 
        + "  - filename: " + filename + ",\n"
        + "  - token...: " + token + ",\n"
        + "  - path....: " + path + ",\n"
        + "  - size....: " + new FileSize(size).toString() 
        + "\n}";
  }
  
}
