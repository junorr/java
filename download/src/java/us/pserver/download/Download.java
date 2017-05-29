/*
 * net/balusc/webapp/FileServlet.java
 *
 * Copyright (C) 2009 BalusC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package us.pserver.download;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * A file servlet supporting resume of downloads and client-side caching and GZIP of text content.
 * This servlet can also be used for images, client-side caching would become more efficient.
 * This servlet can also be used for text files, GZIP would decrease network bandwidth.
 *
 * @author BalusC
 * @link http://balusc.blogspot.com/2009/02/fileservlet-supporting-resume-and.html
 */
@WebServlet("/get/*")
public class Download extends DownloadHead {

  // Constants ----------------------------------------------------------------------------------
  protected static final int DEFAULT_BUFFER_SIZE = 4096; // ..bytes = 10KB.
  protected static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

  
  /**
   * Process GET request.
   * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse).
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    if(head(req, res)) return;
    printHeaders(req);
    //System.out.println("* get: "+ path.toString());
    //System.out.println("* get: "+ path.toString());
    //System.out.println("* get: "+ path.toString());
    long length = Files.size(path);
    List<Range> ranges = getRanges(req, res);
    Range full = new Range(0, length - 1, length);
    if(ranges.isEmpty()) {
      this.copyFull(req, res, full);
    } 
    else {
      this.copyMultipart(req, res, ranges);
    }
  }
  
  
  private void copyFull(HttpServletRequest req, HttpServletResponse res, Range full) throws ServletException, IOException {
    res.setContentLengthLong(full.length);
    res.setHeader("Content-Range", "bytes " 
        + full.start + "-" 
        + full.end + "/" 
        + full.total
    );
    res.setContentType(this.getContentType(req));
    res.flushBuffer();
    printHeaders(res);
    try (
          RandomAccessFile input = new RandomAccessFile(path.toFile(), "r");
          OutputStream output = res.getOutputStream();
        ) {
      copy(input, output, full.start, full.length);
    }
  }

    
  private void copyMultipart(HttpServletRequest req, HttpServletResponse res, List<Range> ranges) throws ServletException, IOException {
    res.setStatus(206); // partial content
    res.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
    res.flushBuffer();
    printHeaders(res);
    String contype = this.getContentType(req);
    try (
          RandomAccessFile input = new RandomAccessFile(path.toFile(), "r");
          OutputStream output = res.getOutputStream();
        ) {
      ServletOutputStream sos = (ServletOutputStream) output;
      for (Range r : ranges) {
        sos.println();
        sos.println("--" + MULTIPART_BOUNDARY);
        sos.println("Content-Type: " + contype);
        sos.println("Content-Range: bytes " 
            + r.start + "-" 
            + r.end + "/" 
            + r.total
        );
        copy(input, output, r.start, r.length);
      }
      sos.println();
      sos.println("--" + MULTIPART_BOUNDARY + "--");
    }
  }

    
  /**
   * Copy the given byte range of the given input to the given output.
   * @param input The input to copy the given range to the given output for.
   * @param output The output to copy the given range from the given input for.
   * @param start Start of the byte range.
   * @param length Length of the byte range.
   * @throws IOException If something fails at I/O level.
   */
  private static void copy(RandomAccessFile input, OutputStream output, long start, long length)
      throws IOException
  {
      byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
      int read;

      if (input.length() == length) {
          // Write full range.
          while ((read = input.read(buffer)) > 0) {
              output.write(buffer, 0, read);
          }
      } else {
          // Write partial range.
          input.seek(start);
          long toRead = length;

          while ((read = input.read(buffer)) > 0) {
              if ((toRead -= read) > 0) {
                  output.write(buffer, 0, read);
              } else {
                  output.write(buffer, 0, (int) toRead + read);
                  break;
              }
          }
      }
  }

}