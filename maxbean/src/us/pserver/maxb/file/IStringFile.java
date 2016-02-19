/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.file;

import java.io.IOException;
import java.nio.file.Path;



/**
 *
 * @author juno
 */
public interface IStringFile {
	
	public String getContent();
	
	public Path getPath();
	
	public IStringFile withPath(Path path);
	
	public IStringFile withContent(String content);
	
	public IStringFile read() throws IOException;
	
	public void write(boolean override) throws IOException;
	
}
