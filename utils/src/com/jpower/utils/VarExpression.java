package com.jpower.utils;

/**
 *
 * @author juno
 */
public class VarExpression
    extends Expression
{
  
  private String var;
  
  private double value;
  
  private boolean valdef;

  
  public VarExpression()
  {
    var = null;
    value = 0;
    valdef = false;
  }
  
  public VarExpression(String varname)
  {
    this();
    var = varname;
  }
  
  public VarExpression(String varname, double value)
  {
    this.var = varname;
    this.setVariable(var, value);
  }

  private CalcMemory calcMemory()
  {
    return CalcMemory.getInstance();
  }

  public double getValue()
  {
    return value;
  }

  public void setValue(double value)
  {
    this.value = value;
    this.valdef = true;

    //calcMemory().putVar(var, value);
  }

  public boolean setVariable(String var, double value)
  {
    if(var == null || var.equals(""))
      return false;

    this.var = var;
    this.value = value;
    this.valdef = true;
    calcMemory().putVar(var, value);

    return true;
  }

  public String getVarName()
  {
    return var;
  }

  @Override
  public VarExpression getVar()
  {
    return this;
  }

  public void setVarName(String varname)
  {
    if(varname == null || varname.equals(""))
      throw new IllegalArgumentException(
          "Var name must be NOT NULL!\n" +
          "#VarExpression.setVarName( String )#");
    this.var = varname;
  }

  @Override
  public double resolve()
  {
    if(calcMemory().contains(var)
        && calcMemory().getValue(var) != value)
      this.setValue(calcMemory().getValue(var));

    if(!valdef)
      throw new IllegalStateException(
          "Variable value is not defined: " + var + "=<null>\n" +
          "#VarExpression.resolve()#");

    return value;
  }

  @Override
  public boolean equals(Object o)
  {
    if(!(o instanceof VarExpression))
      return false;
    VarExpression v = (VarExpression) o;
    if(v.getVarName().equals(this.var))
      return true;
    return false;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 59 * hash + (this.var != null ? this.var.hashCode() : 0);
    return hash;
  }

  @Override
  public VarExpression clone()
  {
    VarExpression clone = new VarExpression();
    if(var != null)
      clone.setVarName(var);
    if(valdef)
      clone.setValue(value);
    return clone;
  }

  @Override
  public String toString()
  {
    return var + (this.valdef ? "=" + value : "");
  }

}
