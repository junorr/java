package com.power.visual;

/**
 *
 * @author f6036477
 */
public class CodeBuilder
{

  private StringBuffer buffer, declarations, methods, imports, classbuf;

  public static final int PUBLIC = 1, PRIVATE = 0, PROTECTED = 2;

  private static String IDENT = "  ";

  private static String NL = "\n";


  public CodeBuilder()
  {
    buffer = new StringBuffer();
    declarations = new StringBuffer();
    methods = new StringBuffer();
    imports = new StringBuffer();
    classbuf = new StringBuffer();
  }

  public void declareVar(int access, Class type, String name)
  {
    String classpack, classname;
    classpack = type.getName();

    if(classpack.indexOf(".") >= 0) {
      classname = classpack.substring(
          classpack.lastIndexOf(".") +1);

      if(imports.indexOf(classpack) < 0 &&
          classpack.indexOf("lang") < 0) {
        imports.append("import ");
        imports.append(classpack);
        imports.append(";");
        imports.append(NL);
      }
    } else
      classname = classpack;

    declarations.append(IDENT);
    if(access == PUBLIC)
      declarations.append("public ");
    else if(access == PRIVATE)
      declarations.append("private ");
    else if(access == PROTECTED)
      declarations.append("protected ");
    else
      return;

    declarations.append(classname);
    declarations.append(" ");
    declarations.append(name);
    declarations.append(";");
    declarations.append(NL);
    declarations.append(NL);
  }



}
