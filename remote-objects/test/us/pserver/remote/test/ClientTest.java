/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.remote.test;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import us.pserver.remote.NetConnector;
import us.pserver.remote.NetworkServer;
import us.pserver.remote.RemoteMethod;
import us.pserver.remote.RemoteObject;
import us.pserver.remote.DefaultFactoryProvider;
import us.pserver.remote.MethodInvocationException;


/**
 *
 * @author juno
 */
@BenchmarkMethodChart(filePrefix = "benchmark-rob")
@BenchmarkOptions(callgc = false, warmupRounds = 10, benchmarkRounds = 40, concurrency = 1)
public class ClientTest {
  
  @Rule
  public TestRule benchmark = new BenchmarkRule();
  
  public static NetworkServer server;
  
  RemoteObject rob;
  
  
  public ClientTest() {
  }
  

  @BeforeClass
  public static void setUpClass() {
    System.setProperty("jub.consumers", "H2,CONSOLE");
    System.setProperty("jub.db.file", ".benchmarks");
  }
  

  @AfterClass
  public static void tearDownClass() {
    System.out.println("* Finishing up...");
  }
  

  @Before
  public void setUp() {
    System.out.println("* Setting up...");
    rob = new RemoteObject(
        new NetConnector(), DefaultFactoryProvider
        .getConnectorXmlChannelFactory());
  }
  

  @After
  public void tearDown() {
    System.out.println("* Closing...");
    if(rob.getChannel() != null)
      rob.getChannel().close();
  }

  
  @Test
  public void testXmlNetCommon() throws MethodInvocationException {
    RemoteMethod meth = new RemoteMethod()
        .forObject("UpperEcho")
        .method("reverse")
        .argTypes(String.class)
        .args("a string to be echoed in upper case");
    
    System.out.println("* Invoking remote:");
    System.out.print("  -> "+ meth+ " = ");
    System.out.println(rob.invoke(meth));
  }
  
  
  @Test
  public void testXmlNetOwn() throws MethodInvocationException {
    RemoteObject rem = new RemoteObject(
        new NetConnector(), DefaultFactoryProvider
        .getConnectorXmlChannelFactory());
    
    RemoteMethod meth = new RemoteMethod()
        .forObject("UpperEcho")
        .method("reverse")
        .argTypes(String.class)
        .args("a string to be echoed in upper case");
    
    System.out.println("* Invoking remote:");
    System.out.print("  -> "+ meth+ " = ");
    System.out.println(rem.invoke(meth));
    rem.getChannel().close();
  }
  
  
  @Test
  public void testHttpNetClient() throws MethodInvocationException {
    RemoteObject rem = new RemoteObject(
        new NetConnector("localhost", 10099), 
        DefaultFactoryProvider
        .getHttpRequestChannelFactory());
    
    RemoteMethod meth = new RemoteMethod()
        .forObject("UpperEcho")
        .method("toLowerCase")
        .argTypes(String.class)
        .args("a string to be echoed in upper case");
    
    System.out.println("* Invoking remote:");
    System.out.print("  -> "+ meth+ " = ");
    System.out.println(rem.invoke(meth));
    rem.getChannel().close();
  }
  
  
  @Test
  public void testHttpNetWithProxy() throws MethodInvocationException {
    RemoteObject rem = new RemoteObject(
        new NetConnector("172.24.75.2", 10099)
        .setProxyAddress("172.24.75.19")
        .setProxyPort(6060), 
        DefaultFactoryProvider
        .getHttpRequestChannelFactory());
    
    RemoteMethod meth = new RemoteMethod()
        .forObject("UpperEcho")
        .method("toUpperCase")
        .argTypes(String.class)
        .args("a string to be echoed in upper case");
    
    System.out.println("* Invoking remote:");
    System.out.print("  -> "+ meth+ " = ");
    System.out.println(rem.invoke(meth));
    rem.getChannel().close();
  }
  
  
  public static void main(String[] args) throws MethodInvocationException {
    ClientTest t = new ClientTest();
    t.setUp();
    t.testXmlNetOwn();
  }
  
}
