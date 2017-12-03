/*
 * Direitos Autorais Reservados (c) 2017 Juno Roesler
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

package us.pserver.kumuluzee.hello2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * File MIME type mapping and detecting (by file extension) based on
 * Mozilla incomplete list of MIME types 
 * (https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types).
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 01/12/2017
 */
public class FileMimeType {
  
  private final Map<String,MimeType> extensionMap = new TreeMap<>();
  
  /**
   * Default constructor, map file extensions to MIME types.
   */
  public FileMimeType() {
    extensionMap.put(EXT_3GP, MimeType.VIDEO_3GPP);
    extensionMap.put(EXT_3G2, MimeType.VIDEO_3GPP2);
    extensionMap.put(EXT_7Z, MimeType.APPLICATION_X_7Z_COMPRESSED);
    extensionMap.put(EXT_AAC, MimeType.AUDIO_AAC);
    extensionMap.put(EXT_ABW, MimeType.APPLICATION_X_ABIWORD);
    extensionMap.put(EXT_ARC, MimeType.APPLICATION_OCTET_STREAM);
    extensionMap.put(EXT_AVI, MimeType.VIDEO_X_MSVIDEO);
    extensionMap.put(EXT_AZW, MimeType.APPLICATION_VND_AMAZON_EBOOK);
    extensionMap.put(EXT_BIN, MimeType.APPLICATION_OCTET_STREAM);
    extensionMap.put(EXT_BZ, MimeType.APPLICATION_X_BZIP);
    extensionMap.put(EXT_BZ2, MimeType.APPLICATION_X_BZIP2);
    extensionMap.put(EXT_CSH, MimeType.APPLICATION_X_CSH);
    extensionMap.put(EXT_CSS, MimeType.TEXT_CSS);
    extensionMap.put(EXT_DOC, MimeType.APPLICATION_MSWORD);
    extensionMap.put(EXT_EOT, MimeType.APPLICATION_VND_MS_FONTOBJECT);
    extensionMap.put(EXT_EPUB, MimeType.APPLICATION_EPUB_ZIP);
    extensionMap.put(EXT_GIF, MimeType.IMAGE_GIF);
    extensionMap.put(EXT_HTM, MimeType.TEXT_HTM);
    extensionMap.put(EXT_HTML, MimeType.TEXT_HTML);
    extensionMap.put(EXT_ICO, MimeType.IMAGE_X_ICON);
    extensionMap.put(EXT_ICS, MimeType.TEXT_CALENDAR);
    extensionMap.put(EXT_JAR, MimeType.APPLICATION_JAVA_ARCHIVE);
    extensionMap.put(EXT_JPEG, MimeType.IMAGE_JPEG);
    extensionMap.put(EXT_JPG, MimeType.IMAGE_JPEG);
    extensionMap.put(EXT_JS, MimeType.APPLICATION_JAVASCRIPT);
    extensionMap.put(EXT_JSON, MimeType.APPLICATION_JSON);
    extensionMap.put(EXT_MID, MimeType.AUDIO_MIDI);
    extensionMap.put(EXT_MIDI, MimeType.AUDIO_MIDI);
    extensionMap.put(EXT_MPKG, MimeType.APPLICATION_VND_APPLE_INSTALLER_XML);
    extensionMap.put(EXT_ODP, MimeType.APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION);
    extensionMap.put(EXT_ODS, MimeType.APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET);
    extensionMap.put(EXT_OGV, MimeType.VIDEO_OGG);
    extensionMap.put(EXT_OGX, MimeType.APPLICATION_OGG);
    extensionMap.put(EXT_PNG, MimeType.IMAGE_PNG);
    extensionMap.put(EXT_PDF, MimeType.APPLICATION_PDF);
    extensionMap.put(EXT_PPT, MimeType.APPLICATION_VND_MS_POWERPOINT);
    extensionMap.put(EXT_RAR, MimeType.APPLICATION_X_RAR_COMPRESSED);
    extensionMap.put(EXT_RTF, MimeType.APPLICATION_RTF);
    extensionMap.put(EXT_SH, MimeType.APPLICATION_X_SH);
    extensionMap.put(EXT_SVG, MimeType.IMAGE_SVG_XML);
    extensionMap.put(EXT_SWF, MimeType.APPLICATION_X_SHOCKWAVE_FLASH);
    extensionMap.put(EXT_TAR, MimeType.APPLICATION_X_TAR);
    extensionMap.put(EXT_TIF, MimeType.IMAGE_TIFF);
    extensionMap.put(EXT_TIFF, MimeType.IMAGE_TIFF);
    extensionMap.put(EXT_TS, MimeType.APPLICATION_TYPESCRIPT);
    extensionMap.put(EXT_TTF, MimeType.FONT_TTF);
    extensionMap.put(EXT_VSD, MimeType.APPLICATION_VND_VISIO);
    extensionMap.put(EXT_WEBA, MimeType.AUDIO_WEBM);
    extensionMap.put(EXT_WEBP, MimeType.IMAGE_WEBP);
    extensionMap.put(EXT_WOFF, MimeType.FONT_WOFF);
    extensionMap.put(EXT_XHTML, MimeType.APPLICATION_XHTML_XML);
    extensionMap.put(EXT_XLS, MimeType.APPLICATION_VND_MS_EXCEL);
    extensionMap.put(EXT_XLSX, MimeType.APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET);
    extensionMap.put(EXT_XML, MimeType.APPLICATION_XML);
    extensionMap.put(EXT_XUL, MimeType.APPLICATION_VND_MOZILLA_XUL_XML);
    extensionMap.put(EXT_ZIP, MimeType.APPLICATION_ZIP);
  }
  
  /**
   * Get file extension.
   * @param p Path to file.
   * @return String file extension (with dot included).
   * @throws IllegalArgumentException if file extension could not be detected.
   */
  public String getExtension(Path p) {
    int dot = Objects.requireNonNull(p).toString().lastIndexOf(".");
    if(dot < 0) {
      throw new IllegalArgumentException("File extension not found for file: "+ p);
    }
    return p.toString().substring(dot);
  }
  
  /**
   * Get the MIME type of the given Path.
   * @param p Path to file.
   * @return The file MIME type.
   * @throws IllegalArgumentException if file MIME type could not be detected.
   */
  public MimeType getMimeType(Path p) {
    String ext = this.getExtension(p);
    if(!extensionMap.containsKey(ext)) {
      throw new java.lang.IllegalArgumentException("MimeType not found for file: "+ p);
    }
    return extensionMap.get(getExtension(p));
  }
  
  /**
   * Get the String content type of the given Path.
   * @param p Path to file.
   * @return The file content type.
   */
  public String getContentType(Path p) {
    String ext = this.getExtension(p);
    return extensionMap.containsKey(ext) 
        ? extensionMap.get(ext).toString() 
        : probeSystemContentType(p);
  }
  
  /**
   * Probe system content type for the given Path.
   * @param p Path to file.
   * @return File content type.
   * @throws IllegalStateException rethrowing IOException.
   */
  public String probeSystemContentType(Path p) {
    try {
      return Files.probeContentType(p);
    }
    catch(IOException e) {
      throw new IllegalStateException(e.toString(), e);
    }
  }
  
  
  public static final String EXT_3GP = ".3gp";
  public static final String EXT_3G2 = ".3g2";
  public static final String EXT_7Z = ".7Z";
  public static final String EXT_AAC = ".aac";
  public static final String EXT_ABW = ".abw";
  public static final String EXT_ARC = ".arc";
  public static final String EXT_AVI = ".avi";
  public static final String EXT_AZW = ".azw";
  public static final String EXT_BIN = ".bin";
  public static final String EXT_BZ = ".bz";
  public static final String EXT_BZ2 = ".bz2";
  public static final String EXT_CSH = ".csh";
  public static final String EXT_CSS = ".css";
  public static final String EXT_CSV = ".csv";
  public static final String EXT_DOC = ".doc";
  public static final String EXT_EOT = ".eot";
  public static final String EXT_EPUB = ".epub";
  public static final String EXT_GIF = ".gif";
  public static final String EXT_HTM = ".htm";
  public static final String EXT_HTML = ".html";
  public static final String EXT_ICO = ".ico";
  public static final String EXT_ICS = ".ics";
  public static final String EXT_JAR = ".jar";
  public static final String EXT_JPEG = ".jpeg";
  public static final String EXT_JPG = ".jpg";
  public static final String EXT_JS = ".js";
  public static final String EXT_JSON = ".json";
  public static final String EXT_MID = ".mid";
  public static final String EXT_MIDI = ".midi";
  public static final String EXT_MPEG = ".mpeg";
  public static final String EXT_MPG = ".mpg";
  public static final String EXT_MPKG = ".mpkg";
  public static final String EXT_ODP = ".odp";
  public static final String EXT_ODS = ".ods";
  public static final String EXT_ODT = ".odt";
  public static final String EXT_OGA = ".oga";
  public static final String EXT_OGV = ".ogv";
  public static final String EXT_OGX = ".ogx";
  public static final String EXT_OTF = ".otf";
  public static final String EXT_PNG = ".png";
  public static final String EXT_PDF = ".pdf";
  public static final String EXT_PPT = ".ppt";
  public static final String EXT_RAR = ".rar";
  public static final String EXT_RTF = ".rtf";
  public static final String EXT_SH = ".sh";
  public static final String EXT_SVG = ".svg";
  public static final String EXT_SWF = ".swf";
  public static final String EXT_TAR = ".tar";
  public static final String EXT_TIF = ".tif";
  public static final String EXT_TIFF = ".tiff";
  public static final String EXT_TS = ".ts";
  public static final String EXT_TTF = ".ttf";
  public static final String EXT_VSD = ".vsd";
  public static final String EXT_WAV = ".wav";
  public static final String EXT_WEBA = ".weba";
  public static final String EXT_WEBM = ".webm";
  public static final String EXT_WEBP = ".webp";
  public static final String EXT_WOFF = ".woff";
  public static final String EXT_WOFF2 = ".woff2";
  public static final String EXT_XHTML = ".xhtml";
  public static final String EXT_XLS = ".xls";
  public static final String EXT_XLSX = ".xlsx";
  public static final String EXT_XML = ".xml";
  public static final String EXT_XUL = ".xul";
  public static final String EXT_ZIP = ".zip";
  
  
  /**
   * File MIME type.
   */
  public static enum MimeType {
    APPLICATION_EPUB_ZIP("application/epub+zip"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_JAVA_ARCHIVE("application/java-archive"),
    APPLICATION_JSON("application/json"),
    APPLICATION_MSWORD("application/msword"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_OGG("application/ogg"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_RTF("application/rtf"),
    APPLICATION_TYPESCRIPT("application/typescript"),
    APPLICATION_VND_AMAZON_EBOOK("application/vnd.amazon.ebook"),
    APPLICATION_VND_APPLE_INSTALLER_XML("application/vnd.apple.installer+xml"),
    APPLICATION_VND_MOZILLA_XUL_XML("application/vnd.mozilla.xul+xml"),
    APPLICATION_VND_MS_EXCEL("application/vnd.ms-excel"),
    APPLICATION_VND_MS_FONTOBJECT("application/vnd.ms-fontobject"),
    APPLICATION_VND_MS_POWERPOINT("application/vnd.ms-powerpoint"),
    APPLICATION_VND_OASIS_OPENDOCUMENT_PRESENTATION("application/vnd.oasis.opendocument.presentation"),
    APPLICATION_VND_OASIS_OPENDOCUMENT_SPREADSHEET("application/vnd.oasis.opendocument.spreadsheet"),
    APPLICATION_VND_OASIS_OPENDOCUMENT_TEXT("application/vnd.oasis.opendocument.text"),
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    APPLICATION_VND_VISIO("application/vnd.visio"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_XML("application/xml"),
    APPLICATION_X_7Z_COMPRESSED("application/x-7z-compressed"),
    APPLICATION_X_ABIWORD("application/x-abiword"),
    APPLICATION_X_BZIP("application/x-bzip"),
    APPLICATION_X_BZIP2("application/x-bzip2"),
    APPLICATION_X_CSH("application/x-csh"),
    APPLICATION_X_RAR_COMPRESSED("application/x-rar-compressed"),
    APPLICATION_X_SH("application/x-sh"),
    APPLICATION_X_SHOCKWAVE_FLASH("application/x-shockwave-flash"),
    APPLICATION_X_TAR("application/x-tar"),
    APPLICATION_ZIP("application/zip"),
    AUDIO_3GPP("audio/3gpp"),
    AUDIO_3GPP2("audio/3gpp2"),
    AUDIO_AAC("audio/aac"),
    AUDIO_MIDI("audio/midi"),
    AUDIO_OGG("audio/ogg"),
    AUDIO_WEBM("audio/webm"),
    AUDIO_X_WAV("audio/x-wav"),
    FONT_OTF("font/otf"),
    FONT_TTF("font/ttf"),
    FONT_WOFF("font/woff"),
    FONT_WOFF2("font/woff2"),
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_TIFF("image/tiff"),
    IMAGE_WEBP("image/webp"),
    IMAGE_X_ICON("image/x-icon"),
    IMAGE_SVG_XML("image/svg+xml"),
    TEXT_CALENDAR("text/calendar"),
    TEXT_CSS("text/css"),
    TEXT_CSV("text/csv"),
    TEXT_HTM("text/htm"),
    TEXT_HTML("text/html"),
    VIDEO_3GPP("video/3gpp"),
    VIDEO_3GPP2("video/3gpp2"),
    VIDEO_MPEG("video/mpeg"),
    VIDEO_OGG("video/ogg"),
    VIDEO_WEBM("video/webm"),
    VIDEO_X_MSVIDEO("video/x-msvideo");
    
    private MimeType(String type) {
      this.mime = type;
    }
    
    private final String mime;
    
    @Override
    public String toString() {
      return this.mime;
    }
    
  }
  
}
