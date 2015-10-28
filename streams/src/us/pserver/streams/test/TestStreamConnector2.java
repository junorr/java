/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.streams.test;

import java.io.IOException;
import java.text.DecimalFormat;
import us.pserver.streams.NullOutput;
import us.pserver.streams.SequenceInputStream;
import us.pserver.streams.StreamConnector;
import us.pserver.tools.timer.Timer;


/**
 *
 * @author juno
 */
public class TestStreamConnector2 {
  
  
  public static void main(String[] args) throws IOException {
    for(int i = 0; i < 5; i++) {
    int max = 100_000_000;
    SequenceInputStream sin = new SequenceInputStream(max);
    StreamConnector stream = new StreamConnector(sin, NullOutput.out);
    System.out.print("* stream.connect(): ");
    Timer tm = new Timer.Nanos().start();
    System.out.println(stream.connect());
    System.out.println("* "+ tm.stop());
    sin.restart();
    max -= 100;
    DecimalFormat f = new DecimalFormat("#,##0");
    System.out.print("* stream.connect("+ f.format(max) +"): ");
    tm = new Timer.Nanos().start();
    System.out.println(stream.connect(max));
    System.out.println("* "+ tm.stop());
    stream.close();
    System.out.println("--------------------------");
    }
  }
  
}
