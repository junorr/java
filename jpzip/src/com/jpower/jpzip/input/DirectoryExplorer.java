package com.jpower.jpzip.input;

import com.jpower.jpzip.Path;
import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;


/**
 *
 * @author f6036477
 */
public class DirectoryExplorer {
  
  private InputContainer container;
  
  private File dir;
  
  private FileFilter filter;
  
  
  private class AllFilesFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
      return true;
    }
  }
  
  
  public DirectoryExplorer() {
    container = new InputContainer();
    dir = null;
    filter = new AllFilesFilter();
  }
  
  
  public void setDirectory(File d) {
    if(d == null || !d.exists() || !d.isDirectory())
      throw new IllegalArgumentException(
          "Invalid directory: "+d);
    dir = d;
  }


  public FileFilter getFilter() {
    return filter;
  }


  public void setFilter(FileFilter filter) {
    if(filter == null) filter = new AllFilesFilter();
    this.filter = filter;
  }
  
  
  public InputContainer getInputContainer() {
    return container;
  }
  
  
  public InputContainer explore() {
    if(dir == null) return null;
    
    Path path = new Path().addPath(dir.getName());
    File[] content = dir.listFiles(filter);
    if(content == null || content.length == 0)
      return null;
    
    this.exploreDir(content, path);
    return container;
  }
  
  
  private void exploreDir(File[] content, Path path) {
    for(File f : content) {
      if(f.isFile())
        container.add(new FileInput(f, path.resolve()));
      else
        exploreDir(f.listFiles(filter), 
            path.clone().addPath(f.getName()));
    }
  }
  
  
  public static void main(String[] args) {
    DirectoryExplorer exp = new DirectoryExplorer();
    File dir = new File("e:/files/7zip");
    exp.setDirectory(dir);
    InputContainer cont = exp.explore();
    Iterator it = cont.iterator();
    while(it.hasNext())
      System.out.println(it.next());
  }
  
}
