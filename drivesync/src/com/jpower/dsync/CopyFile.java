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
import java.nio.channels.CompletionHandler;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 23/04/2013
 */
public class CopyFile implements FileVisitor<Path>, 
    CompletionHandler<Integer, InfoCopy> {
  
  public static final int BLOCK_SIZE = 4096;

  public static final int MAX_BUFFER_SIZE = 20 * 4096;
  
  public static final int DEFAULT_BUFFER_SIZE = MAX_BUFFER_SIZE;
  
  private Path srcPath, dstPath;
  
  private List<ProgressListener> listeners;
  
  private int bufferSize;
  
  private long total;
  
  private long progress;
  
  
  public CopyFile(String srcDir, String dstDir) {
    if(srcDir == null || srcDir.trim().isEmpty())
      throw new IllegalArgumentException(
          "Invalid srcDir: "+ srcDir);
    
    if(dstDir == null || dstDir.trim().isEmpty())
      throw new IllegalArgumentException(
          "Invalid dstDir: "+ dstDir);
    
    listeners = Collections.synchronizedList(
        new LinkedList<ProgressListener>());
    bufferSize = DEFAULT_BUFFER_SIZE;
    srcPath = Paths.get(srcDir);
    dstPath = Paths.get(dstDir);
    total = progress = 0;
    
    if(!Files.exists(srcPath))
      throw new IllegalArgumentException(
          "srcDir does not exists: "+ srcDir);
    
    if(!Files.exists(dstPath))
      try {
        Files.createDirectories(dstPath);
      } catch(IOException e) {
        throw new IllegalArgumentException(
            "Cant create dstDir: "+ dstDir, e);
      }
  }


  public int getBufferSize() {
    return bufferSize;
  }


  public CopyFile setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
    return this;
  }
  
  
  public CopyFile addListener(ProgressListener p) {
    if(p != null) {
      listeners.add(p);
      p.setTotal(total);
    }
    return this;
  }
  
  
  public List<ProgressListener> getListeners() {
    return listeners;
  }
  
  
  public long getTotal() {
    return total;
  }
  
  
  public CopyFile setTotal(long t) {
    total = t;
    return this;
  }
  
  
  public long getProgress() {
    return progress;
  }
  
  
  private Path createDerivatedPath(Path src, Path dst) {
    if(src == null || dst == null)
      return null;
    Path r = srcPath.relativize(src);
    return dst.resolve(r);
  }


  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    Path dst = this.createDerivatedPath(dir, dstPath);
    if(dst != null) Files.createDirectories(dst);
    return FileVisitResult.CONTINUE;
  }
  
  
  private void initCopy(Path src, Path dst) throws IOException {
    if(src == null || dst == null) return;
    if(!Files.exists(dst)) Files.createFile(dst);
    
    InfoCopy ci = new InfoCopy(OpType.OP_READ);
    ci.bufsize(getBestBufferSize(src));
    ci.createRChannel(src);
    ci.createWChannel(dst);
    ci.buffer(ByteBuffer.allocateDirect(ci.bufsize()));
    ci.rchannel().read(ci.buffer(), ci.rposition(), ci, this);
  }
  
  
  public static int getBestBufferSize(Path file) {
    long size = file.toFile().length();
    if(size <= BLOCK_SIZE) return BLOCK_SIZE;
    else if(size >= MAX_BUFFER_SIZE) return MAX_BUFFER_SIZE;
    else {
      int mult = (int) Math.ceil(((double)size / BLOCK_SIZE));
      return mult * BLOCK_SIZE;
    }
  }


  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    Path dst = this.createDerivatedPath(file, dstPath);
    if(dst != null) {
      this.initCopy(file, dst);
    }
    return FileVisitResult.CONTINUE;
  }


  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    if(!listeners.isEmpty()) {
      for(ProgressListener p : listeners)
        p.onError(exc);
    }
    return FileVisitResult.TERMINATE;
  }


  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }
  
  
  public synchronized void notifyProgress() {
    boolean completed = progress >= total;
    for(ProgressListener p : listeners) {
      p.setProgress(progress);
      if(completed) p.onComplete();
    }
  }
  
  
  public synchronized boolean completed(int inc) {
    progress += inc;
    return progress >= total;
  }
  

  @Override
  public void completed(Integer result, InfoCopy at) {
    if(result < 0) {
      this.terminate(at);
      return;
    }
    
    if(at.operation() == OpType.OP_READ) {
      at.rposition(result);
      at.operation(OpType.OP_WRITE);
      at.buffer().flip();
      at.wchannel().write(at.buffer(), at.wposition(), at, this);
    }
    
    else {//OpType.OP_WRITE
      if(!this.completed(result)) {
        at.wposition(result);
        at.operation(OpType.OP_READ);
        at.buffer().clear();
        at.rchannel().read(at.buffer(), at.rposition(), at, this);
      }
      this.notifyProgress();
    }
  }
  
  
  public void terminate(InfoCopy c) {
    if(c == null || c.rchannel() == null 
        || c.wchannel() == null) 
      return;
    
    try {
      c.rchannel().close();
      c.wchannel().close();
      FileAttributes.copyAll(c.srcPath(), c.dstPath());
      
    } catch(IOException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public void failed(Throwable exc, InfoCopy attachment) {
    for(ProgressListener p : listeners) {
      p.onError((Exception) exc);
    }
  }
  
  
  public CopyFile calculateTotalSize() {
    SizeFileVisitor sf = new SizeFileVisitor(srcPath);
    this.setTotal(sf.calculateSize());
    return this;
  }
  
  
  public void startBlocking() throws IOException {
    final Object block = new Object();
    
    this.addListener(new ProgressListenerImpl() {
      @Override public void onComplete() {
        synchronized(block) { block.notifyAll(); }
      }
    });
    
    if(total <= 0) 
      this.calculateTotalSize();
    
    Files.walkFileTree(srcPath, this);
    
    try {
      synchronized(block) { block.wait(); }
      
    } catch(InterruptedException ex) {
      throw new IOException(ex);
    }
  }
  
  
  public CopyFile start() throws IOException {
    if(total <= 0) 
      this.calculateTotalSize();
    
    Files.walkFileTree(srcPath, this);
    return this;
  }

  
  public static void main(String[] args) throws IOException {
    //String src = "D:/zzz/src";
    //String dst = "D:/zzz/dst";
    String src = "/home/juno/zzz/src";
    String dst = "/home/juno/zzz/dst";
    CopyFile cf = new CopyFile(src, dst);
    
    ProgressBar bar = new ProgressBar();
    //cf.addListener(bar);
    
    System.out.println("* start!");
    long start = System.currentTimeMillis();
    
    cf.startBlocking();
    
    start = System.currentTimeMillis() - start;
    System.out.println("* decurred time: "+ start+ " millis");
    System.out.println("* Exiting...");
    
    //* OS ext4 copy time (1,1 GB): 37s
    //* CP ext4 copy time (1,1 GB): 37s
    
    //* OS ntfs copy time (1,1 GB): 40s
    //* CP ntfs copy time (1,1 GB): 46s
    
    //Rodando com o processador ocupado
    //* CP ntfs copy time (1,1 GB): 48s
    
    //Versão com buffer modificado
    //* CP ntfs copy time (1,1 GB): 36s
  }

}
