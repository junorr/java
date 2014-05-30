package com.jpower.jpzip;


/**
 *
 * @author juno
 */
public class Path {
  
  private StringBuilder relative;
  
  private static final String slash = "/";
  
  private String name;
  
  
  public Path() {
    relative = new StringBuilder();
    name = null;
  }
  
  
  public Path(String name) {
    this();
    this.setName(name);
  }
  
  
  public Path(String name, String path) {
    this(name);
    this.addPath(path);
  }
  
  
  public Path(String name, Path path) {
    this(name);
    this.addPath(path);
  }
  
  
  public void setName(String name) {
    if(name != null && !name.equals(""))
      this.name = name;
  }
  
  
  public String getName() {
    return name;
  }
  
  
  public Path addPath(String path) {
    if(path == null || path.equals("")) return this;
    if(relative.length() > 0) relative.append(slash);
    relative.append(path);
    return this;
  }
  
  
  public Path addPath(Path path) {
    if(path == null || path.isEmpty()) return this;
    return this.addPath(path.resolve());
  }
  
  
  public Path clearPath() {
    relative = new StringBuilder();
    return this;
  }
  
  
  public String removeLast() {
    if(relative.length() <= 0) return null;
    int idx = relative.lastIndexOf(slash);
    String last = relative.substring(idx);
    relative.delete(idx, relative.length());
    return last;
  }
  
  
  public boolean isEmpty() {
    return this.length() > 0;
  }
  
  
  public int length() {
    int plus = (relative.length() > 0 ? 1 : 0);
    return relative.length() + name.length() + plus;
  }
  
  
  public String getRawPath() {
    return relative.toString();
  }
  
  
  public String resolve() {
    if(relative.length() > 0) {
      String ret = null;
      if(name != null)
        ret = relative.toString().concat(slash).concat(name);
      else
        ret = relative.toString();
      return ret;
    }
    return name;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 41 * hash + (this.relative != null ? this.relative.hashCode() : 0);
    hash = 41 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }
  
  
  @Override
  public boolean equals(Object o) {
    if(o == null || !(o instanceof Path))
      return false;
    return this.hashCode() == o.hashCode();
  }
  
  
  @Override
  public Path clone() {
    Path p = new Path();
    p.name = name;
    p.relative.append(relative.toString());
    return p;
  }
  
  
  public String toString() {
    return this.resolve();
  }
  
}
