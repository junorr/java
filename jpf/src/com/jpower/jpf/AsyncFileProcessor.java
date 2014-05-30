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

package com.jpower.jpf;

import com.jpower.date.DateDiff;
import com.jpower.date.SimpleDate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 01/11/2012
 */
public class AsyncFileProcessor {

  private String fin;
  
  private String fout;
  
  private PrintStream writer;
  
  private String start, end;
  
  private Lock lock;
  
  private SimpleDate startDate;
  
  private String insert;
  
  private String location;
  
  
  private static class Lock implements Serializable {
    private boolean lock;
    private static Lock instance;
    private static boolean construct = false;
    
    private Lock() throws IllegalAccessException {
      if(!construct)
        throw new IllegalAccessException(
            "Lock should not be instanciated directly");
      lock = false;
    }
    
    public static Lock getInstance() {
      if(instance == null) {
        construct = true;
        try { instance = new Lock(); }
        catch(IllegalAccessException ex) {}
        construct = false;
      }
      return instance;
    }
    
    public synchronized Lock lock() {
      lock = true;
      return this;
    }
    
    public boolean isLocked() {
      return lock;
    }
    
    public Lock unlock() {
      lock = false;
      return this;
    }
  }
  
  
  private class FReader implements Runnable, CompletionHandler<Integer, ByteBuffer> {
    public static final int BUFFER_SIZE = 1000;
    
    private AsynchronousFileChannel channel;
    private Path path;
    private String start, end;
    private long pos;
    private boolean startFound;
    private Thread main;
    private String name;
    private long startpos;
    
    public FReader(String name, String in, long pos, String start, String end, Thread main) {
      path = Paths.get(in);
      this.start = start;
      this.end = end;
      this.pos = pos;
      this.main = main;
      startFound = false;
      startpos = 0;
      this.name = name;
      try {
        channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
      } catch(IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    
    public void close() {
      try { channel.close(); main.interrupt(); } 
      catch (IOException e) { this.failed(e, null); }
        
      if(startFound) {
        if(insert != null && location.equals("end")) {
          writer.println(insert);
          insert = null;
        }
        writer.flush();
        writer.close();
      }
    }
    
    private int reverseIndexOf(String orig, String search, int offset) {
      if(orig == null || search == null || offset < 1) return -1;
      for(int i = (offset - search.length() +1); i >= 0; i--) {
        String s = orig.substring(i, i + search.length());
        if(s.equals(search))
          return i;
      }
      return -1;
    }
    
    @Override
    public void completed(Integer i, ByteBuffer buf) {
      if(i > 0) {
        boolean keepgoing = true;
        String line = new String(buf.array());
        pos += i;
        
        if(line.contains(start) && !lock.isLocked()) {
          int idx = reverseIndexOf(line, "\n", line.indexOf(start));
          if(idx >= 0) line = line.substring(idx);
          
          startFound = true;
          startpos = pos;
          lock.lock();
          System.out.println("* "+ name+ " start found '"
              + start+ "' in "+ pos+ " bytes!");
        }
        
        if(startFound && line.contains(end)) {
          keepgoing = false;
          
          line = line.substring(0, line.indexOf("\n", line.indexOf(end)));
          
          System.out.println("* "+ name+ " end found '"
              + end+ "' in "+ pos+ " bytes!");
          
          System.out.println("* "+ name+ ": "
              + (double)((pos - startpos) / 1024.0)
              + " KB processed!");
          
          SimpleDate endDate = new SimpleDate();
          System.out.println("* "+ name+ " stopped in "
              + endDate.format(SimpleDate.DDMMYYYY_HHMMSS_SLASH));
          
          DateDiff diff = new DateDiff(startDate, endDate);
          System.out.println("* Time Elapsed: "
              + diff.calculate().toString());
          
        } 
        
        if(startFound) {
          if(insert != null && location.equals("start")) {
            writer.println(insert);
            insert = null;
          }
          writer.print(line);
        }
        
        if(keepgoing) {
          ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
          channel.read(buffer, pos, buffer, this);
          
        } else {
          close();
        }
        
      } else { 
        System.out.println("* No more data: "+ i);
        close();
      }
    }
    
    @Override
    public void failed(Throwable th, ByteBuffer buf) {
      th.printStackTrace();
      throw new RuntimeException(th);
    }
    
    @Override
    public void run() {
      if(start == null || start.trim().isEmpty())
        throw new RuntimeException("Invalid start string: "+ start);
      if(end == null || end.trim().isEmpty())
        throw new RuntimeException("Invalid end string: "+ end);
      if(channel == null)
        throw new RuntimeException("Invalid channel: "+ channel);
      if(pos < 0) 
        throw new RuntimeException("Invalid position: "+ pos);
      
      ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
      System.out.println("* "+ name+ " started in "+ pos+ " bytes!");
      channel.read(buffer, pos, buffer, this);
    }
  }
  
  
  public AsyncFileProcessor() {
    fin = null;
    fout = null;
    writer = null;
    start = null;
    end = null;
    startDate = new SimpleDate();
    lock = Lock.getInstance();
    System.out.println("* FileProcessor started in "
        + startDate.format(SimpleDate.DDMMYYYY_HHMMSS_SLASH));
  }
  
  
  public AsyncFileProcessor setInputFile(String input) {
    fin = input;
    return this;
  }
  
  
  public AsyncFileProcessor setOutput(PrintStream output) {
    writer = output;
    return this;
  }
  
  
  public String getInputFile() {
    return fin;
  }
  
  
  public String getOutputFile() {
    return fout;
  }


  public String getStartString() {
    return start;
  }


  public String getTextInsert() {
    return insert;
  }


  public AsyncFileProcessor setTextInsert(String insert) {
    this.insert = insert;
    return this;
  }


  public String getTextLocation() {
    return location;
  }


  public AsyncFileProcessor setTextLocation(String location) {
    this.location = location;
    return this;
  }
  
  
  public AsyncFileProcessor setStartString(String start) {
    this.start = start;
    System.out.println("* start string: '"+ start+ "'");
    return this;
  }


  public String getEndString() {
    return end;
  }


  public AsyncFileProcessor setEndString(String end) {
    this.end = end;
    System.out.println("* end string: '"+ end+ "'");
    return this;
  }
  
  
  public void run() {
    if(start == null || start.trim().isEmpty())
      throw new RuntimeException("Invalid start string: "+ start);
    if(end == null || end.trim().isEmpty())
      throw new RuntimeException("Invalid end string: "+ end);
    if(fin == null || fin.isEmpty())
      throw new RuntimeException("Invalid input file: "+ fin);
    if(writer == null)
      throw new RuntimeException("Invalid output file: "+ fout);
    
    long size = 0;
    try { size = Files.size(Paths.get(fin)); } 
    catch (IOException ex) {}
    
    System.out.println("* File size: "+ size+ " bytes");
    
    long part = size / 3 - 1;
    long init = 0;
    
    Thread current = Thread.currentThread();
    
    Thread t1 = new Thread(new FReader("fr1", fin, init, start, end, current));
    t1.setPriority(Thread.MAX_PRIORITY);
    t1.setDaemon(false);
    init += part;
    Thread t2 = new Thread(new FReader("fr2", fin, init, start, end, current));
    t2.setPriority(Thread.MAX_PRIORITY);
    t2.setDaemon(false);
    init += part;
    Thread t3 = new Thread(new FReader("fr3", fin, init, start, end, current));
    t3.setPriority(Thread.MAX_PRIORITY);
    t3.setDaemon(false);
    
    t1.start();
    t2.start();
    t3.start();
    
    try { current.join(); } 
    catch (InterruptedException ex) {}
  }
  
  
  public static void main(String[] args) throws FileNotFoundException {
    AsyncFileProcessor fp = new AsyncFileProcessor();
    fp.setInputFile("C:\\Users\\juno\\mysql\\mysql-uso_dados")
        .setOutput(new PrintStream("C:\\Users\\juno\\mysql\\tb_cli.sql"))
        .setStartString("TB_PRC")
        .setEndString(");");
    fp.run();
  }
  
}
