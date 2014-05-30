/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jpower.utils;

import java.io.IOException;

/**
 *
 * @author f6036477
 */
public interface IOutputStream
{

  public void close();

  public void flush();

  public void write(int b) throws IOException;

  public void write(byte[] b) throws IOException;

  public void write(byte[] b, int off, int len) throws IOException;

}
