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

package us.pserver.zerojs.reader;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import us.pserver.zerojs.exception.JsonReadException;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/04/2016
 */
public class FileInput extends ChannelInput {
  
  
  public FileInput(String file) throws JsonReadException {
    super(init(file));
  }
  
  
  public FileInput(String file, Charset cs) throws JsonReadException {
    super(init(file), cs);
  }
  
  
  public FileInput(Path file) throws JsonReadException {
    super(init(file));
  }
  
  
  public FileInput(Path file, Charset cs) throws JsonReadException {
    super(init(file), cs);
  }
  
  
  public FileInput(File file) throws JsonReadException {
    super(init(file));
  }
  
  
  public FileInput(File file, Charset cs) throws JsonReadException {
    super(init(file), cs);
  }
  
  
  private static ReadableByteChannel init(String file) throws JsonReadException {
    if(file == null || file.isEmpty()) {
      throw new IllegalArgumentException(
          "File name must be not null"
      );
    }
    return init(Paths.get(file));
  }
  

  private static ReadableByteChannel init(Path file) throws JsonReadException {
    if(file == null || !Files.exists(file)) {
      throw new IllegalArgumentException(
          "Invalid File Path: "+ file
      );
    }
    try {
      return Files.newByteChannel(file, StandardOpenOption.READ);
    } catch(IOException e) {
      throw new JsonReadException(e.getMessage(), e);
    }
  }
  

  private static ReadableByteChannel init(File file) throws JsonReadException {
    if(file == null || !file.exists()) {
      throw new IllegalArgumentException(
          "Invalid File: "+ file
      );
    }
    return init(file.toPath());
  }

}
