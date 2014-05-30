/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.utils;

/**
 * Classe utilitária para executar
 * o comando ping no sistema
 * operacional Windows, utilizando
 * o comando nativo do sistema.
 * @author Juno Roesler
 */
public class WindowsPingTool 
    extends AbstractPingTool
{

  private SystemCommand ping;

  private static final String
      CMD_PING = "cmd /c ping ",
      OPTION_PACKET_SIZE = "-l ",
      OPTION_COUNT = "-n ",
      OPTION_TTL = "-i ",
      OPTION_TIMEOUT = "-w ";

  private boolean successful;


  public WindowsPingTool()
  {
    super();
    successful = false;
  }

  private void mountCommand()
  {
    ping = new SystemCommand();
    ping.setCommand(CMD_PING);

    String options = null;
    if(this.getCount() > 0)
      options = OPTION_COUNT+
          String.valueOf(this.getCount())+ " ";
    if(this.getTTL() > 0)
      options += OPTION_TTL+
          String.valueOf(this.getTTL())+ " ";
    if(this.getTimeout() > 0)
      options += OPTION_TIMEOUT+
          String.valueOf(this.getTimeout())+ " ";
    if(this.getPacketSize() > 0)
      options += OPTION_PACKET_SIZE+
          String.valueOf(this.getPacketSize())+ " ";

    options += this.getHost();
    ping.setArgs(options);
  }

  public void run()
  {
    this.mountCommand();
    //System.out.println(ping.toString());
    ping.run();
  }

  public String getOutput()
  {
    return ( (ping.getOutput() != null &&
        !ping.getOutput().equals("")) ?
          ping.getOutput() :
          ping.getErrorOutput());
  }

  public boolean isSuccessful()
  {
    this.getReturnLine();
    return successful;
  }

  public String getReturnLine()
  {
    if(ping == null || this.getOutput() == null)
      return null;

    int index = this.getOutput().indexOf(":");
    int index2 = this.getOutput().indexOf("TTL=", index);
    String line = null;

    if(index < 0) {
      // Endereço incorreto
      line = this.getOutput();
      successful = false;

    } else if(index2 < 0) {
      // Esgotado o tempo limite do pedido
      index2 = this.getOutput().indexOf(".", index) + 1;
      line = this.getOutput().substring(index +7, index2);
      successful = false;

    } else {
      // Host atingido com sucesso
      line = this.getOutput().substring(index +7, index2 +7);
      successful = true;
    }
    return line;
  }

  public int getHits()
  {
    int index = this.getOutput().indexOf(",");
    if(index < 0)
      return -1;

    int index2 = this.getOutput().indexOf(",", index +2);
    String receb = this.getOutput().substring(index +2, index2);
    receb = receb.replaceAll(" ", "");
    
    try {
      return Integer.parseInt(
          receb.substring(
          receb.indexOf("=") +2));
    } catch(Exception ex) {
      ex.printStackTrace();
      return -1;
    }
    
  }

}
