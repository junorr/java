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

package us.pserver.redfs;

import java.nio.file.Path;
import us.pserver.listener.ProgressContainer;
import us.pserver.listener.ProgressListener;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/12/2013
 */
public class IOData extends ProgressContainer {

  private RFile rfile;
  
  private Path path;
  
  private long startPosition;
  
  private long length;
  
  
  public IOData() {
    length = 0;
    startPosition = 0;
    path = null;
    rfile = null;
  }
  
  
  @Override
  public IOData addListener(ProgressListener pl) {
    super.add(pl);
    return this;
  }
  
  
  @Override
  public IOData clone() {
    return new IOData()
        .setLength(length)
        .setPath(path)
        .setRFile(rfile)
        .setStartPos(startPosition);
  }
  
  
  public IOData getWriteVersion() {
    return new IOData()
        .setLength(length)
        .setStartPos(startPosition)
        .setRFile(rfile);
  }


  public RFile getRFile() {
    return rfile;
  }


  public IOData setRFile(RFile rfile) {
    this.rfile = rfile;
    return this;
  }


  public Path getPath() {
    return path;
  }


  public IOData setPath(Path path) {
    this.path = path;
    return this;
  }


  protected long getStartPos() {
    return startPosition;
  }


  protected IOData setStartPos(long startPosition) {
    this.startPosition = startPosition;
    return this;
  }


  protected long getLength() {
    return length;
  }


  protected IOData setLength(long length) {
    this.length = length;
    return this;
  }

}
