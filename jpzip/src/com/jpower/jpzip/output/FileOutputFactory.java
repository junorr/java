package com.jpower.jpzip.output;

import com.jpower.jpzip.Path;
import java.io.File;
import java.util.zip.ZipEntry;


/**
 *
 * @author juno
 */
public class FileOutputFactory {

  
  public static FileOutput create(ZipEntry entry) {
    return create(entry, null);
  }
  
  
  public static FileOutput create(ZipEntry entry, String path) {
    if(entry == null) return null;
    Path p = new Path();
    p.addPath(path);
    p.setName(entry.toString());
    
    File f = new File(p.resolve());
    if(f.getParentFile() != null && !f.getParentFile().exists())
      f.getParentFile().mkdirs();
    
    return new FileOutput(f, true);
  }
  
}
