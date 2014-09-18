package com.jpower.utils.test;

import com.jpower.utils.Logger;

import java.io.*;

public class TestLogger {

  public static void sleep(int t)
  throws Exception {
    Thread.sleep(t);
  }

  public static void main(String[] args) {
    try {
      File log = new File("logger_test.log");
      Logger logger = new Logger(log);
      logger.setScreenLog(true);

      logger.addComment("Testando o logger");

      //System.out.println(logger.getTime(Logger.DATE));

      logger.add("registro 1");
      logger.add("registro 2");
      logger.add("registro 3");

      sleep(833);

      logger.add("registro 1.1");

      logger.addComment("Comentario 2\nlinha 2");

      logger.add("registro 2.1");
      logger.add("registro 3.1");

      logger.close();

      System.out.println("\n");

      logger.print(System.out);

    } catch(Exception ex) {
      ex.printStackTrace();
    }//try-catch
  }//main

}
