package com.jpower.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author f6036477
 */
public class NetProxy extends Authenticator
{

  private JPanel panel;
  private JLabel userl, passwordl, headerl;
  private JTextField userf;
  private JPasswordField passf;

  private String server;
  private int port;
  private Proxy httpproxy, socksproxy;
  private PasswordAuthentication passauth;


  public NetProxy()
  {
    server = null;
    port = 80;
    httpproxy = null;
    socksproxy = null;
    initGUI();
  }

  private void initGUI()
  {
    //System.out.println( "[NetProxy]: Creating GUI..." );
    panel = new JPanel();
    panel.setLayout(null);
    panel.setSize(150, 80);
    panel.setPreferredSize(new Dimension(150, 80));

    headerl = new JLabel("Proxy Authentication:");
    headerl.setBounds(5, 5, 145, 20);
    headerl.setFont(new Font("SansSerif", Font.BOLD, 12));

    userl = new JLabel("Username: ");
    userl.setBounds(5, 30, 65, 20);

    passwordl = new JLabel("Password: ");
    passwordl.setBounds(5, 55, 65, 20);

    userf = new JTextField();
    userf.setBounds(90, 30, 70, 20);

    passf = new JPasswordField();
    passf.setBounds(90, 55, 70, 20);

    panel.add(headerl);
    panel.add(userl);
    panel.add(passwordl);
    panel.add(userf);
    panel.add(passf);
    //System.out.println( "[NetProxy]: GUI created." );
  }


  public void setHttpProxy(String server, int port)
  {
    SocketAddress address = new InetSocketAddress(server, port);
    System.out.println( "NetProxy: "+ address.toString() );
    httpproxy = new Proxy(Proxy.Type.HTTP, address);
    System.out.println( "NetProxy: "+ httpproxy.toString() );
    Authenticator.setDefault(this);
  }


  public void setSocksProxy(String server, int port)
  {
    SocketAddress address = new InetSocketAddress(server, port);
    httpproxy = new Proxy(Proxy.Type.SOCKS, address);
    Authenticator.setDefault(this);
  }


  public Proxy getHttpProxy()
  {
    return httpproxy;
  }


  public Proxy getSocksProxy()
  {
    return socksproxy;
  }


  public URLConnection openHttpConnection(URL url)
  {
    try {
      return url.openConnection(httpproxy);
    } catch(IOException ioe) {
      System.err.println("[ERROR]: "+
          ioe.getMessage());
      return null;
    }
  }


  public void setPasswordAuthentication(PasswordAuthentication passauth)
  {
    this.passauth = passauth;
  }


  public boolean isPasswordAuthenticationDefined()
  {
    return passauth != null;
  }


  @Override
  public PasswordAuthentication getPasswordAuthentication()
  {
    //System.out.println( "[NetProxy]: Authentication requested." );
    if(passauth == null) {
      int i = JOptionPane.showConfirmDialog(null, panel, "Proxy Authentication", JOptionPane.OK_CANCEL_OPTION);
      if(i != JOptionPane.OK_OPTION) {
        System.err.println("[ERROR]: Invalid Proxy Authentication!");
        System.err.println("[ERROR]: Exiting...");
        System.exit(407);
      }//if

      passauth = new PasswordAuthentication(
          userf.getText(),
          passf.getPassword());
    }//if

    return passauth;
  }

}
