package com.jpower.utils;

/**
 *
 * @author f6036477
 */
public class LinuxPingTool 
    extends AbstractPingTool
{

  private SystemCommand ping;

  private static final String
      CMD_PING = "ping ",
      OPTION_PACKET_SIZE = "-s ",
      OPTION_COUNT = "-c ",
      OPTION_TTL = "-t ",
      OPTION_TIMEOUT = "-W ";

  private boolean successful;


  public LinuxPingTool()
  {
    super();
    successful = false;
    super.setCount(1);
  }

  @Override
  public void setCount(int count)
  {
    if(count <= 0) count = 1;
    super.setCount(count);
  }

  private void mountCommand()
  {
    ping = new SystemCommand();
    ping.setCommand(CMD_PING);
    ping.waitProcessEnd(true);

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

  public void run() {
    this.mountCommand();
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

    int index = this.getOutput().indexOf("data.");
    int index2 = this.getOutput().indexOf("time=", index);
    String line = null;

    if(index < 0) {
      // linha de comando incorreta
      line = this.getOutput();
      successful = false;

    } else if(index2 < 0 && 
        this.getOutput().indexOf("Unreachable", index) > 0) {
      //Destination Unreachable
      index2 = this.getOutput().indexOf("Unreachable", index);
      line = this.getOutput().substring(index +6, index2 +11);
      successful = false;

    } else if(index2 < 0) {
      // Esgotado o tempo limite do pedido
      index = this.getOutput().indexOf("---");
      index = this.getOutput().indexOf("---", index +4);
      index2 = this.getOutput().indexOf("ms", index);
      if(index2 < 0) index2 = this.getOutput().length();
      line = this.getOutput().substring(index +4, index2 +2);
      successful = false;

    } else {
      // Host atingido com sucesso
      index2 = this.getOutput().indexOf("ms", index2);
      line = this.getOutput().substring(index +6, index2 +2);
      successful = true;
    }
    return line;
  }

  public int getHits()
  {
    if(!this.isSuccessful())
      return 0;
    
    int index = this.getOutput().indexOf(",");

    if(index < 0)
      return -1;

    int index2 = this.getOutput().indexOf("rec", index +1);

    String receb = this.getOutput().substring(index +1, index2);
    receb = receb.replaceAll(" ", "");

    try {
      return Integer.parseInt(receb);
    } catch(Exception ex) {
      ex.printStackTrace();
      return -1;
    }
  }

}
