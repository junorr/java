package us.pserver.microprofile.hello;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Path("/uri{other:.*}")
public class UriInfoTest {
  
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String getUriInfo(@Context UriInfo info) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("* getAbsolutePath: ").append(info.getAbsolutePath()).append("<br/>");
    sb.append("* getPath: ").append(info.getPath()).append("<br/>");
    sb.append("* getMatchedURIs: ").append(info.getMatchedURIs()).append("<br/>");
    sb.append("* getPathSegments: ").append(info.getPathSegments()).append("<br/>");
    sb.append("* getPathParameters: ").append(info.getPathParameters()).append("<br/>");
    return sb.toString();
  }
  
}
