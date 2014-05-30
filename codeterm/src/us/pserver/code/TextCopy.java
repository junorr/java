/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package us.pserver.code;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;


/**
 *
 * @author Juno
 */
public class TextCopy implements Transferable {

  public static final String MIME_TEXT_PLAIN = "text/plain;representationclass=java.io.InputStream;charset=UTF-8";
  
  public static final DataFlavor FLAVOR = plainTextFlavor();
  
  
  private String text;
  
  
  public TextCopy() {
    text = null;
  }
  
  
  public TextCopy(String text) {
    this.text = text;
  }
  
  
  public String getText() {
    return text;
  }
  
  
  public TextCopy setText(String text) {
    this.text = text;
    return this;
  }
  
  
  public TextCopy append(String text) {
    if(this.text == null)
      this.text = text;
    else
      this.text += text;
    return this;
  }
  
  
  public TextCopy clear() {
    text = null;
    return this;
  }
  
  
  public static DataFlavor plainTextFlavor() {
    try {
      return new DataFlavor(MIME_TEXT_PLAIN);
    } catch(ClassNotFoundException e){
      return null;
    }
  }
  
  
  public TextCopy putInClipboard() {
    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
    clip.setContents(this, null);
    return this;
  }
  
  
  public TextCopy setFromClipboard() {
    TextCopy tc = getFromClipboard();
    if(tc == null) this.text = null;
    else this.text = tc.text;
    return this;
  }
  
  
  public static TextCopy getFromClipboard() {
    try {
      Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
      DataFlavor[] dfs = clip.getAvailableDataFlavors();
      TextCopy text = new TextCopy();
      for(DataFlavor df : dfs) {
        if(text.isDataFlavorSupported(df)) { 
          if(df.isRepresentationClassCharBuffer()) {
            return TextCopy.fromCharBuffer((CharBuffer) clip.getData(df));
          } else if(df.isRepresentationClassInputStream()) {
            return TextCopy.fromInputStream((InputStream) clip.getData(df));
          } else if(df.isRepresentationClassReader()) {
            return TextCopy.fromReader((Reader) clip.getData(df));
          } else if(String.class.isAssignableFrom(df.getRepresentationClass())) {
            return new TextCopy((String)clip.getData(df));
          } else {
            return new TextCopy(new String((char[])clip.getData(df)));
          }
        }
      }
      return null;
    } catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
  @Override
  public String toString() {
    if(text != null) return text;
    return "TextCopy{ null }";
  }
  
  
  public InputStream toInputStream() {
    if(text == null) return null;
    Charset utf8 = Charset.forName("UTF-8");
    return new ByteArrayInputStream(text.getBytes(utf8));
  }
  
  
  public static TextCopy fromInputStream(InputStream input) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      int read = 1;
      while((read = input.read()) != -1)
        bos.write(read);
      Charset utf8 = Charset.forName("UTF-8");
      return new TextCopy(new String(bos.toByteArray(), utf8));
    } catch(IOException e) {
      return null;
    }
  }
  
  
  public static TextCopy fromReader(Reader r) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Writer wr = new BufferedWriter(new OutputStreamWriter(bos));
    try {
      int read = 1;
      while((read = r.read()) != -1)
        wr.write(read);
      wr.flush();
      Charset utf8 = Charset.forName("UTF-8");
      return new TextCopy(new String(bos.toByteArray(), utf8));
    } catch(IOException e) {
      return null;
    }
  }
  
  
  public static TextCopy fromCharBuffer(CharBuffer cb) {
    if(cb == null) return null;
    return new TextCopy(cb.toString());
  }
  
  
  public static TextCopy fromCharArray(char[] bs) {
    if(bs == null || bs.length == 0) return null;
    return new TextCopy(new String(bs));
  }
  
  
  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[] { FLAVOR };
  }


  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    boolean classok = flavor.isRepresentationClassInputStream()
        || flavor.isRepresentationClassReader()
        || flavor.isRepresentationClassCharBuffer()
        || String.class.isAssignableFrom(flavor.getRepresentationClass())
        || char[].class.isAssignableFrom(flavor.getRepresentationClass());
    return classok && flavor.getMimeType().contains("text/plain");
  }


  @Override
  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
    if(flavor == null) throw new UnsupportedFlavorException(flavor);
    if(text == null) return null;
    if(isDataFlavorSupported(flavor))
      return toInputStream();
    return null;
  }
  
  
  public static void main(String[] args) throws Exception {
    TextCopy txt = new TextCopy("Some text");
    txt.putInClipboard();
    System.out.println(TextCopy.getFromClipboard());
    Thread.sleep(10000);
    System.out.println(TextCopy.getFromClipboard());
  }
  
}
