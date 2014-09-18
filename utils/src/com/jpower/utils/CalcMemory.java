package com.jpower.utils;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author juno
 */
public class CalcMemory
{

  private static boolean lock = true;

  private static CalcMemory cmem;

  public List<Expression> expressions;

  public List<Expression> memory;

  public List<VarExpression> vars;


  private CalcMemory()
  {
    if(lock)
      throw new UnsupportedOperationException(
          "Class not instaciable: CalcMemory");
  }
  
  protected void init() throws IllegalAccessException
  {
    if(lock)
      throw new IllegalAccessException(
          "Method access not allowed!");
    expressions = new LinkedList<Expression>();
    vars = new LinkedList<VarExpression>();
    memory = new LinkedList<Expression>();
  }

  public static CalcMemory getInstance()
  {
    if(cmem == null) {
      lock = false;
      cmem = new CalcMemory() { };
      try {
        cmem.init();
      } catch (IllegalAccessException ex) {}
    }
    lock = true;
    return cmem;
  }

  public boolean contains(String var)
  {
    for(VarExpression v : vars)
      if(v.getVarName().equals(var))
        return true;
    return false;
  }

  public VarExpression get(String var)
  {
    for(VarExpression v : vars)
      if(v.getVarName().equals(var))
        return v;
    return null;
  }

  public double getValue(String var)
  {
    VarExpression v = this.get(var);
    if(v != null) return v.getValue();
    else return -1;
  }
  
  public void put(VarExpression var)
  {
    if(var == null) return;
    VarExpression v = null;
    int index = -1;
    for(int i = 0; i < vars.size(); i++) {
      v = vars.get(i);
      if(v.equals(var))
        index = i;
    }
    if(index == -1) vars.add(var);
    else vars.set(index, var);
  }

  public void putVar(String var, double value)
  {
    if(var == null || var.equals(""))
      return;

    if(this.contains(var) && this.getValue(var) == value)
      return;

    VarExpression v = this.get(var);
    if(v == null) v = new VarExpression(var);
    this.put(v);
    v.setValue(value);
  }
  
  
  public Expression findExpression(String name) {
    if(name == null || name.isEmpty() || memory.isEmpty())
      return null;
    for(Expression e : memory) {
      if(e.getName() != null
          && e.getName().equals(name))
        return e;
    }
    return null;
  }
  

  public void save(File f)
      throws IOException
  {
    if(f == null)
      throw new IllegalArgumentException(
          "\n    File must be NOT NULL!\n" +
          "    #CalcMemory.save(File): line 112#");

    if(!f.exists())
      f.createNewFile();

    FileOutputStream fos = new FileOutputStream(f);

    XStream x = new XStream();
    x.omitField(CalcMemory.class, "expressions");
    x.omitField(CalcMemory.class, "cmem");
    x.omitField(Expression.class, "objs");

    x.toXML(this, fos);

    fos.close();
  }

  public static void load(File f)
      throws IOException
  {
    if(f == null || !f.exists())
      throw new IllegalArgumentException(
          "\n    File must be NOT NULL!\n" +
          "    #CalcMemory.load(File): line 128#");

    FileInputStream fis = new FileInputStream(f);
    XStream x = new XStream();

    CalcMemory.lock = false;
    
    Object o = x.fromXML(fis);
    fis.close();

    if(!(o instanceof CalcMemory))
      throw new IllegalStateException("\n" +
          "    Cannot read \"CalcMemory\" instance form: "+f+"\n" +
          "    "+ f + " contains " + o.getClass().getSimpleName() + " instance.\n" +
          "    #CalcMemory.load(File): line 139");

    if(cmem != null) {
      cmem.vars = ((CalcMemory) o).vars;
      cmem.memory = ((CalcMemory) o).memory;
    } else {
      CalcMemory.cmem = (CalcMemory) o;
    }//else
    if(cmem.expressions == null) {
      cmem.expressions = new LinkedList<Expression>();
    }
    CalcMemory.lock = true;
  }

}
