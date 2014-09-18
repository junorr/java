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

package com.jpower.tn3270.script.parser;

import com.jpower.tn3270.script.CommandType;
import com.jpower.tn3270.script.Command;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 03/08/2013
 */
public abstract class CommandParser {
  
  public static final String 
      PLUS = "+",
      MINUS = "-",
      MULTIPLY = "*",
      DIVIDE = "/",
      EQUALS = "==",
      NOT_EQUALS = "!=",
      GREATER = ">",
      LESSER = "<";


  public abstract boolean canParse(String str);

  public abstract Command parse(String str);
  
  
  protected boolean canParseBasic(String str, String strCmd) {
    return str != null 
        && str.contains(" ")
        && str.trim().startsWith(strCmd);
  }
  
  
  public String removeInitSpaces(String str) {
    if(str == null || str.isEmpty())
      return str;
    String nstr = "";
    boolean letter = false;
    for(int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if(!letter) letter = c != ' ';
      if(letter) nstr += c;
    }
    return nstr;
  }
  
  
  protected Command parseBasic(String str, CommandType cmd) {
    if(!this.canParse(str)) return null;
    str = this.removeInitSpaces(str);
    
    String sr[] = str.split(" ");
    if(sr == null || sr.length 
        < cmd.getArgsSize() + 1)
      return null;
    
    Command sc = new Command(cmd);
    int i = 0;
    int isr = 1;
    while(i < cmd.getArgsSize()) {
      if(sr[isr].isEmpty()) {
        isr++;
        continue;
      }
      sc.setArg(sr[isr], i);
      i++; isr++;
    }
    return sc;
  }
  
  
  public static boolean isExpression(String str) {
    if(startsWithNumber(str)) return true;
    return str.contains(PLUS)
        || str.contains(MINUS)
        || str.contains(MULTIPLY)
        || str.contains(DIVIDE)
        || str.contains(EQUALS)
        || str.contains(NOT_EQUALS)
        || str.contains(GREATER)
        || str.contains(LESSER);
  }
  
  
  public static boolean startsWithNumber(String s) {
    if(s == null || s.trim().isEmpty()) return false;
    try {
      Integer.parseInt(s.trim().substring(0, 1));
      return true;
    } catch(NumberFormatException e) {
      return false;
    }
  }


  public static CommandParser getInstanceFor(String str) {
    if(str == null 
        || str.trim().isEmpty())
      return null;
    
    switch(str) {
      case Command.STR_END:
        return new EndParser();
      case Command.STR_ENDWHILE:
        return new EndWhileParser();
      case Command.STR_ELSE:
        return new ElseParser();
    }
    
    String command = str.substring(
        0, str.indexOf(" ")).trim();
    
    switch(command) {
      case Command.STR_APPEND:
        return new AppendParser();
      case Command.STR_CONNECT_3270:
        return new Connect3270Parser();
      case Command.STR_CONTAINS:
        return new ContainsParser();
      case Command.STR_CURSOR:
        return new CursorParser();
      case Command.STR_DELAY:
        return new DelayParser();
      case Command.STR_EQUALS:
        return new EqualsParser();
      case Command.STR_FILE_GET:
        return new FileGetParser();
      case Command.STR_FILE_PUT:
        return new FilePutParser();
      case Command.STR_GET_FIELD:
        return new GetFieldParser();
      case Command.STR_GET_SCREEN:
        return new GetScreenParser();
      case Command.STR_IF:
        return new IfParser();
      case Command.STR_KEY:
        return new KeyCommandParser();
      case Command.STR_NOT:
        return new NotParser();
      case Command.STR_PASS:
        return new PassParser();
      case Command.STR_PRINT:
        return new PrintParser();
      case Command.STR_SET_FIELD:
        return new SetFieldParser();
      case Command.STR_VAR:
        return new VarParser();
      case Command.STR_WAIT:
        return new WaitParser();
      case Command.STR_WHILE:
        return new WhileParser();
      default:
        return null;
    }
  }
  
  
  public static void main(String[] args) {
    String str = "   s t r";
    //System.out.println(CommandParser.removeInitSpaces(str));
    /*
    String cmd = "endwhile";
    System.out.println("* cmd    = "+ cmd);
    System.out.println("* parser = "+ CommandParser.getInstanceFor(cmd));
    */
  }
  
}
