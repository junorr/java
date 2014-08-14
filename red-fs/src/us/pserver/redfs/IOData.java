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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 13/12/2013
 */
public class IOData {

  private RemoteFile rfile;
  
  private Path path;
  
  private long startPosition;
  
  private long length;
  
  private List<ProgressListener> listeners;
  
  
  public IOData() {
    listeners = Collections.synchronizedList(
        new LinkedList<ProgressListener>());
    length = 0;
    startPosition = 0;
    path = null;
    rfile = null;
  }
  
  
  private IOData(List<ProgressListener> list) {
    listeners = list;
    length = 0;
    startPosition = 0;
    path = null;
    rfile = null;
  }
  
  
  public IOData clone() {
    return new IOData(listeners)
        .setLength(length)
        .setPath(path)
        .setRemoteFile(rfile)
        .setStartPos(startPosition);
  }
  
  
  public IOData getWriteVersion() {
    return new IOData()
        .setLength(length)
        .setStartPos(startPosition)
        .setRemoteFile(rfile);
  }


  public RemoteFile getRemoteFile() {
    return rfile;
  }


  public IOData setRemoteFile(RemoteFile rfile) {
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


  public List<ProgressListener> getListeners() {
    return listeners;
  }


  public IOData setListeners(List<ProgressListener> listeners) {
    this.listeners = listeners;
    return this;
  }
  
  
  public IOData addListener(ProgressListener p) {
    if(p != null) listeners.add(p);
    return this;
  }
  
  
  protected synchronized void error(IOException e) {
    if(e != null)
      for(ProgressListener p : listeners)
        p.error(e);
  }
  
  
  protected synchronized void setMax(long max) {
    for(ProgressListener p : listeners)
      p.setMax(max);
  }
  
  
  protected synchronized void update(Path path) {
    if(path != null)
      for(ProgressListener p : listeners)
        p.update(path);
  }
  
  
  protected synchronized void update(long current) {
    for(ProgressListener p : listeners)
      p.update(current);
  }
  
}
