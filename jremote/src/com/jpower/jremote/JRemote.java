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

package com.jpower.jremote;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 21/09/2012
 */
public class JRemote {
  
  private Config conf;
  
  
  public JRemote() {
    conf = new Config("./jremote.conf");
    conf.setComment("JRemote 0.9-2012922");
    if(conf.isEmpty())
      this.initDefaults();
  }
  
  
  private void initDefaults() {
    conf.put(ConfigKeys.ADDRESS.name(), "127.0.0.1");
    conf.put(ConfigKeys.PORT.name(), 20443);
    conf.put(ConfigKeys.PROXY_ADDRESS.name(), "");
    conf.put(ConfigKeys.PROXY_PORT.name(), 0);
    conf.put(ConfigKeys.PROXY_AUTH.name(), "");
    conf.put(ConfigKeys.EXEC_TYPE.name(), ConfigKeys.CONTROLLED.name());
    conf.save();
  }
  
  
  public void init() {
    String type = conf.get(ConfigKeys.EXEC_TYPE.name());
    System.out.println("\n* Starting JRemote ("+ type+ " mode)...\n");
    
    if(type.equalsIgnoreCase(
        ConfigKeys.CLIENT.name())) {
      
      FrontEnd fe = new FrontEnd();
      ClientController cc = fe.getController();
      if(conf.contains(ConfigKeys.PORT.name()))
        cc.setPort(conf.getInt(ConfigKeys.PORT.name()));
      if(conf.contains(ConfigKeys.ADDRESS.name()))
        cc.setAddress(conf.get(ConfigKeys.ADDRESS.name()));
      if(conf.contains(ConfigKeys.PROXY_ADDRESS.name())
          && conf.contains(ConfigKeys.PROXY_PORT.name()))
        cc.setProxy(conf.get(ConfigKeys.PROXY_ADDRESS.name()),
            conf.getInt(ConfigKeys.PROXY_PORT.name()));
      if(conf.contains(ConfigKeys.PROXY_AUTH.name()))
        cc.setProxyAuth(conf.get(ConfigKeys.PROXY_AUTH.name()));
      
      cc.start();
      
    } else if(type.equalsIgnoreCase(
        ConfigKeys.CONTROLLED.name())) {
      
      ScreenController sc = new ScreenController();
      if(conf.contains(ConfigKeys.PORT.name()))
        sc.setPort(conf.getInt(ConfigKeys.PORT.name()));
      if(conf.contains(ConfigKeys.ADDRESS.name()))
        sc.setAddress(conf.get(ConfigKeys.ADDRESS.name()));
      if(conf.contains(ConfigKeys.PROXY_ADDRESS.name())
          && conf.contains(ConfigKeys.PROXY_PORT.name()))
        sc.setProxy(conf.get(ConfigKeys.PROXY_ADDRESS.name()),
            conf.getInt(ConfigKeys.PROXY_PORT.name()));
      if(conf.contains(ConfigKeys.PROXY_AUTH.name()))
        sc.setProxyAuth(conf.get(ConfigKeys.PROXY_AUTH.name()));
      
      sc.start();
      
    } else {
      Server s = new Server();
      if(conf.contains(ConfigKeys.PORT.name()))
        s.setPort(conf.getInt(ConfigKeys.PORT.name()));
      s.start();
    }
  }
  
  
  private int toInt(String s) {
    try {
      return Integer.parseInt(s);
    } catch(Exception ex) {
      return -1;
    }
  }
  
  
  public void parseArgs(String[] args) {
    if((args == null || args.length == 0) 
        && conf.getFile().exists()) {
      conf.load();
      return;
    }
    
    if(args == null || args.length == 0) {
      this.showHelp();
      System.exit(1);
    }
    if(!"-h".equals(args[0]) && args.length < 2) {
      this.showHelp();
      System.exit(1);
    } 
    for(int i = 0; i < args.length; i++) {
      if("-h".equals(args[i])) {
        this.showHelp();
        System.exit(0);
      }
      else if("-t".equals(args[i])) {
        if(!args[i+1].equalsIgnoreCase(ConfigKeys.CLIENT.name())
            && !args[i+1].equalsIgnoreCase(ConfigKeys.SERVER.name())
            && !args[i+1].equalsIgnoreCase(ConfigKeys.CONTROLLED.name())) {
          this.showHelp();
          System.exit(1);
        }
        conf.put(ConfigKeys.EXEC_TYPE.name(), args[i+1]);
      }
      else if("-p".equals(args[i])) {
        if(toInt(args[i+1]) < 0) {
          this.showHelp();
          System.exit(1);
        }
        conf.put(ConfigKeys.PORT.name(), toInt(args[i+1]));
      }
      else if("-a".equals(args[i])) {
        conf.put(ConfigKeys.ADDRESS.name(), args[i+1]);
      }
      else if("-y".equals(args[i])) {
        this.parseProxy(args[i+1]);
      }
      else if("-c".equals(args[i])) {
        this.parseProxyAuth(args[i+1]);
      }
    }
    conf.save();
  }
  
  
  private void parseProxy(String s) {
    if(s == null || s.isEmpty() || !s.contains(":")) {
      this.showHelp();
      System.exit(1);
    }
    String[] ss = s.split(":");
    if(ss.length != 2 || toInt(ss[1]) < 0) {
      this.showHelp();
      System.exit(1);
    }
    conf.put(ConfigKeys.PROXY_ADDRESS.name(), ss[0]);
    conf.put(ConfigKeys.PROXY_PORT.name(), ss[1]);
  }
  
  
  private void parseProxyAuth(String s) {
    if(s == null || s.isEmpty()) {
      this.showHelp();
      System.exit(1);
    }
    conf.put(ConfigKeys.PROXY_AUTH.name(), s);
  }
  
  
  public void showHelp() {
    StringBuilder sb = new StringBuilder();
    sb.append("\n");
    sb.append("      JRemote 0.9-2012922 \n");
    sb.append("---------------------------------- \n");
    sb.append("       Copyright (C) 2012 \n");
    sb.append(" Juno Roesler - juno.rr@gmail.com \n");
    sb.append("---------------------------------- \n");
    sb.append("OS Remote Control - Server Based \n");
    sb.append("Usage: \n");
    sb.append("  jremote [-t] [-p] [-a] [-y] [-c] [-h] \n");
    sb.append("  Options description: \n");
    sb.append("  -t: Execution Type \n");
    sb.append("      {SERVER | CLIENT | CONTROLLED (DEFAULT) } \n");
    sb.append("  -p: Port (Default: 20443) \n");
    sb.append("  -a: Host Address \n");
    sb.append("  -y: Proxy {<host>:<port>} \n");
    sb.append("  -c: Proxy Credentials {<user>:<passwd>} \n");
    sb.append("  -h: Show this Help \n");
    System.err.println(sb.toString());
  }
  
  
  public static void main(String[] args) {
    JRemote jr = new JRemote();
    jr.parseArgs(args);
    jr.init();
  }
  
}
