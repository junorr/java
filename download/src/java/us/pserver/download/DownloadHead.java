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

package us.pserver.download;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import us.pserver.download.util.URIParam;
import us.pserver.download.util.URIPath;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 04/04/2017
 */
public class DownloadHead extends HttpServlet {
  
  static final long DEFAULT_EXPIRE_TIME = 604800000L; // ..ms = 1 week.
  
  protected Path path;
  
  
  protected void printHeaders(HttpServletRequest req) {
    System.out.println("----- Request ------");
    System.out.println(req.getMethod()+ "  "+ req.getRequestURI());
    Enumeration<String> en = req.getHeaderNames();
    while(en.hasMoreElements()) {
      String hd = en.nextElement();
      System.out.println(hd+ ": "+ req.getHeader(hd));
    }
  }
  
  
  protected void printHeaders(HttpServletResponse res) {
    System.out.println("----- Response ------");
    System.out.println(res.getStatus());
    Collection<String> en = res.getHeaderNames();
    for(String hd : en) {
      System.out.println(hd+ ": "+ res.getHeader(hd));
    }
  }
  
  
  protected String getETag() throws IOException {
    return path.getFileName().toString() + "_"
        + Files.size(path) + "_"
        + Files.getLastModifiedTime(path).toMillis();
  }
  
  
  protected boolean headIfNoneMatch(HttpServletRequest req, HttpServletResponse res) throws IOException {
    // If-None-Match header should contain "*" or ETag. If so, then return 304.
    String etag = getETag();
    String ifNoneMatch = req.getHeader("If-None-Match");
    if (ifNoneMatch != null && matches(ifNoneMatch, etag)) {
      res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      res.setHeader("ETag", etag); // Required in 304.
      res.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);
      return true;
    }
    return false;
  }
  
  
  protected boolean headIfModifiedSince(HttpServletRequest req, HttpServletResponse res) throws IOException {
    // If-Modified-Since header should be greater than LastModified. If so, then return 304.
    // This header is ignored if any If-None-Match header is specified.
    long ifModifiedSince = req.getDateHeader("If-Modified-Since");
    if (req.getHeader("If-None-Match") == null 
      && ifModifiedSince != -1 
      && ifModifiedSince + 1000 > Files.getLastModifiedTime(path).toMillis()) {
      res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      res.setHeader("ETag", getETag()); // Required in 304.
      res.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);
      return true;
    }
    return false;
  }
  
  
  protected boolean headIfMatch(HttpServletRequest req, HttpServletResponse res) throws IOException {
    // If-Match header should contain "*" or ETag. If not, then return 412.
    String ifMatch = req.getHeader("If-Match");
    if (ifMatch != null && !matches(ifMatch, getETag())) {
      res.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
      return true;
    }
    return false;
  }

  
  protected boolean headIfUnmodifiedSince(HttpServletRequest req, HttpServletResponse res) throws IOException {
    // If-Unmodified-Since header should be greater than LastModified. If not, then return 412.
    long ifUnmodifiedSince = req.getDateHeader("If-Unmodified-Since");
    if (ifUnmodifiedSince != -1 
        && ifUnmodifiedSince + 1000 
            <= Files.getLastModifiedTime(path).toMillis()) {
      res.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
      return true;
    }
    return false;
  }
  
  
  protected boolean acceptsGzip(HttpServletRequest req) {
    String acceptEncoding = req.getHeader("Accept-Encoding");
    return acceptEncoding != null && accepts(acceptEncoding, "gzip");
  }
  
  
  protected String getContentType(HttpServletRequest req) {
    String contentType = getServletContext()
        .getMimeType(path.getFileName().toString());
    if (contentType == null) {
      contentType = "application/octet-stream";
    }
    if (contentType.startsWith("text")) {
      contentType += ";charset=UTF-8";
    }
    return contentType;
  }
  
  
  protected void headContent(HttpServletRequest req, HttpServletResponse res) throws IOException {
  // Get content type by file name and set default GZIP support and content disposition.
    String contentType = getContentType(req);
    String disposition = "inline";
    // Else, expect for images, determine content disposition. If content type is supported by
    // the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
    if (!contentType.startsWith("image")) {
      String accept = req.getHeader("Accept");
      disposition = accept != null && accepts(accept, contentType) ? "inline" : "attachment";
    }
    // Initialize response.
    res.setHeader("Content-Disposition", disposition 
        + ";filename=\"" + path.getFileName().toString() + "\"");
    res.setHeader("Accept-Ranges", "bytes");
    res.setHeader("ETag", getETag());
    res.setDateHeader("Last-Modified", Files.getLastModifiedTime(path).toMillis());
    res.setDateHeader("Expires", (System.currentTimeMillis() + DEFAULT_EXPIRE_TIME));
  }
  
  
  protected List<Range> getRanges(HttpServletRequest req, HttpServletResponse res) throws IOException {
    List<Range> rgs = new ArrayList<>();
    String range = req.getHeader("Range");
    if (range != null) {
      long length = Files.size(path);
      String[] srg = range.substring(6).split(",");
      for (String part : srg) {
        // Assuming a file with length of 100, the following examples returns bytes at:
        // 50-80 (50 to 80), 40- (40 to length=100), -20 (length-20=80 to length=100).
        long start = sublong(part, 0, part.indexOf("-"));
        long end = sublong(part, part.indexOf("-") + 1, part.length());
        if (start == -1) {
          start = length - end;
          end = length - 1;
        } else if (end == -1 || end > length - 1) {
          end = length - 1;
        }
        rgs.add(new Range(start, end, length));
      }
    }
    return rgs;
  }
  
  
  protected boolean head(HttpServletRequest req, HttpServletResponse res) throws IOException {
    //printHeaders(req);
    path = URIPath.of(
        new URIParam(req.getRequestURI())
    ).getPath().toPath();
    boolean head = true;
    if(path == null) {
      res.sendError(404, "NoFileDef");
    }
    else if(!this.headIfNoneMatch(req, res)
        && !this.headIfModifiedSince(req, res)
        && !this.headIfMatch(req, res)
        && !this.headIfUnmodifiedSince(req, res)) {
      this.headContent(req, res);
      head = false;
    }
    //printHeaders(res);
    return head;
  }

  
  @Override
  protected void doHead(HttpServletRequest req, HttpServletResponse res) throws IOException {
    this.head(req, res);
  }
  

  /**
   * Returns true if the given accept header accepts the given value.
   * @param acceptHeader The accept header.
   * @param toAccept The value to be accepted.
   * @return True if the given accept header accepts the given value.
   */
  protected boolean accepts(String acceptHeader, String toAccept) {
    String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
    String acceptReplaced = toAccept.replaceAll("/.*$", "/*");
    for(int i = 0; i < acceptValues.length; i++) {
      if(acceptValues[i].equals(toAccept)
          || acceptValues[i].equals(acceptReplaced)
          || acceptValues[i].equals("*/*")) {
        return true;
      }
    }
    return false;
  }


  /**
   * Returns true if the given match header matches the given value.
   * @param matchHeader The match header.
   * @param toMatch The value to be matched.
   * @return True if the given match header matches the given value.
   */
  protected boolean matches(String matchHeader, String toMatch) {
    String[] matchValues = matchHeader.split("\\s*,\\s*");
    for(int i = 0; i < matchValues.length; i++) {
      if(matchValues[i].equals(toMatch)
          || matchValues[i].equals("*")) {
        return true;
      }
    }
    return false;
  }
  
  
  /**
   * Returns a substring of the given string value from the given begin index to the given end
   * index as a long. If the substring is empty, then -1 will be returned
   * @param value The string value to return a substring as long for.
   * @param beginIndex The begin index of the substring to be returned as long.
   * @param endIndex The end index of the substring to be returned as long.
   * @return A substring of the given string value as long or -1 if substring is empty.
   */
  protected long sublong(String value, int beginIndex, int endIndex) {
    String substring = value.substring(beginIndex, endIndex);
    return (substring.length() > 0) ? Long.parseLong(substring) : -1;
  }

  
    
  
  
  
  
  
  
  /**
   * This class represents a byte range.
   */
  protected class Range {
    long start;
    long end;
    long length;
    long total;

    /**
     * Construct a byte range.
     * @param start Start of the byte range.
     * @param end End of the byte range.
     * @param total Total length of the byte source.
     */
    public Range(long start, long end, long total) {
        this.start = start;
        this.end = end;
        this.length = end - start + 1;
        this.total = total;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 67 * hash + (int) (this.start ^ (this.start >>> 32));
      hash = 67 * hash + (int) (this.end ^ (this.end >>> 32));
      hash = 67 * hash + (int) (this.length ^ (this.length >>> 32));
      hash = 67 * hash + (int) (this.total ^ (this.total >>> 32));
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Range other = (Range) obj;
      if (this.start != other.start) {
        return false;
      }
      if (this.end != other.end) {
        return false;
      }
      if (this.length != other.length) {
        return false;
      }
      if (this.total != other.total) {
        return false;
      }
      return true;
    }
  }
  
    
}
