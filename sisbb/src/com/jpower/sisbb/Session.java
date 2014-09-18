package com.jpower.sisbb;


import com.ibm.eNetwork.ECL.ECLConstants;
import com.ibm.eNetwork.ECL.ECLErr;
import com.ibm.eNetwork.ECL.ECLPS;
import com.ibm.eNetwork.ECL.ECLSession;
import java.util.Properties;

/**
 *
 * @author f6036477
 */
public class Session
{

  private Properties props;

  private ECLSession session;
  

  public Session(String name) throws ECLErr
  {
    if(name == null || name.equals(""))
      throw new IllegalArgumentException("[ERROR]: " +
          "Invalid Session Name: "+ name);

    //System.loadLibrary("pcseclj");
    
    props = new Properties();
    props.put(
        ECLSession.SESSION_TYPE,
        ECLSession.SESSION_TYPE_3270);
    props.put(
        ECLSession.SESSION_SSL,
        ECLSession.SESSION_OFF);
    props.put(
        ECLSession.SESSION_NAME, name);
    props.put(
        ECLSession.SESSION_PS_SIZE,
        ECLSession.SESSION_PS_24X80_STR);

    session = new ECLSession(props);
  }

  public Properties getProperties()
  {
    return props;
  }

  public void setProperties(Properties p)
  {
    props = p;
  }

  public ECLSession getSession()
  {
    return session;
  }

  public ECLSession createNewSession() throws ECLErr
  {
    session = new ECLSession(props);
    return session;
  }

  public ECLPS getPS()
  {
    if(session == null) return null;
    return session.GetPS();
  }

  @Override
  public String toString()
  {
    return "[ Session " +
        props.getProperty(ECLSession.SESSION_NAME) +
        " ] : " +
        props.getProperty(ECLSession.SESSION_TYPE);
  }


  public static void main(String[] args) throws ECLErr
  {
    System.out.println("java.library.path: ");
    System.out.println(System.getProperty("java.library.path"));
    System.setProperty("java.library.path", "C:\\Windows\\System32");
    System.out.println("java.library.path: ");
    System.out.println(System.getProperty("java.library.path"));
    
    
    System.loadLibrary("pcseclj");
    
    Session s = new Session("A");
    ECLPS ps = s.getPS();
    System.out.println("ps.SendKeys(ECLPS.ENTER_STR)");
    ps.SendKeys(ECLConstants.ENTER_STR);
    System.out.println(ECLPS.ENTER_STR);
    System.out.println("Session size: "+ ps.GetSize());
    char[] buf = new char[ps.GetSize()];
    int r = ps.GetString(buf, buf.length);
    System.out.println("chars readed: "+ r);
    
    int cols = 0;
    for(char c: buf) {
      System.out.print(c);
      cols++;
      if(cols % 80 == 0)
        System.out.println();
    }
  }

}
