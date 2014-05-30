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

package com.jpower.dsync;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/04/2013
 */
public class InfoCopy {

  private AsynchronousFileChannel rchannel;
  
  private AsynchronousFileChannel wchannel;
  
  private ByteBuffer buffer;
  
  private OpType operation;
  
  private long rposition, wposition;
  
  private Path srcpath, dstpath;
  
  private int bufsize;
  
  
  public InfoCopy(OpType op) {
    operation = op;
    bufsize = 0;
    rposition = wposition = 0;
  }
  
  
  public boolean createRChannel(Path p) {
    this.srcPath(p);
    if(srcpath == null) return false;
    try {
      rchannel = AsynchronousFileChannel.open(
          srcpath, StandardOpenOption.READ);
      return true;
    } catch(IOException ex) {
      return false;
    }
  }


  public boolean createWChannel(Path p) {
    this.dstPath(p);
    if(dstpath == null) return false;
    try {
      wchannel = AsynchronousFileChannel.open(
          dstpath, StandardOpenOption.WRITE);
      return true;
    } catch(IOException ex) {
      return false;
    }
  }


  public boolean createRChannel() {
    if(srcpath == null) return false;
    try {
      rchannel = AsynchronousFileChannel.open(
          srcpath, StandardOpenOption.READ);
      return true;
    } catch(IOException ex) {
      return false;
    }
  }


  public boolean createWChannel() {
    if(dstpath == null) return false;
    try {
      wchannel = AsynchronousFileChannel.open(
          dstpath, StandardOpenOption.WRITE);
      return true;
    } catch(IOException ex) {
      return false;
    }
  }


  public AsynchronousFileChannel rchannel() {
    return rchannel;
  }


  public InfoCopy rchannel(AsynchronousFileChannel rchannel) {
    this.rchannel = rchannel;
    return this;
  }


  public AsynchronousFileChannel wchannel() {
    return wchannel;
  }


  public InfoCopy wchannel(AsynchronousFileChannel wchannel) {
    this.wchannel = wchannel;
    return this;
  }


  public long rposition() { return rposition; }

  public InfoCopy rposition(long bytes) {
    rposition += bytes;
    return this;
  }
  
  
  public long wposition() { return wposition; }

  public InfoCopy wposition(long bytes) {
    wposition += bytes;
    return this;
  }
  
  
  public ByteBuffer buffer() { return buffer; }
  
  public InfoCopy buffer(ByteBuffer b) {
    buffer = b;
    return this;
  }
  
  
  public OpType operation() { return operation; }
  
  public InfoCopy operation(OpType op) {
    operation = op;
    return this;
  }
  
  
  public Path srcPath() { return srcpath; }
  
  public InfoCopy srcPath(Path src) {
    if(src != null) srcpath = src;
    return this;
  }
  
  
  public Path dstPath() { return dstpath; }
  
  public InfoCopy dstPath(Path dst) {
    if(dst != null) dstpath = dst;
    return this;
  }
  
  
  public int bufsize() { return bufsize; }
  
  public InfoCopy bufsize(int size) {
    bufsize = size;
    return this;
  }
  
}
