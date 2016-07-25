/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.pserver.maxb.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;



/**
 *
 * @author juno
 */
public class StringFile implements IStringFile {

	private final Path path;
	
	private final String content;
	
	
	public StringFile(Path path, String content) {
		if(path == null) {
			throw new IllegalArgumentException(
					"File path must be not null"
			);
		}
		this.path = path;
		this.content = content;
	}
	
	
	@Override
	public Path getPath() {
		return path;
	}
	
	
	@Override
	public String getContent() {
		return content;
	}
	
	
	public static StringFile of(Path path, String content) {
		return new StringFile(path, content);
	}
	
	
	public static StringFile of(String path, String content) {
		return new StringFile(Paths.get(path), content);
	}


	public static StringFile of(Path path) {
		return new StringFile(path, null);
	}
	
	
	public static StringFile of(String path) {
		return new StringFile(Paths.get(path), null);
	}


	@Override
	public StringFile withPath(Path path) {
		return of(path, content);
	}


	@Override
	public StringFile withContent(String content) {
		return of(path, content);
	}


	@Override
	public IStringFile read() throws IOException {
		SeekableByteChannel ch = Files.newByteChannel(
				path, StandardOpenOption.READ
		);
		ByteBuffer buf = ByteBuffer.allocate(4096);
		StringBuilder sb = new StringBuilder();
		Charset cs = Charset.forName("UTF-8");
		while(ch.read(buf) > 0) {
			sb.append(cs.decode(buf));
		}
		return of(path, sb.toString());
	}


	@Override
	public void write(boolean override) throws IOException {
		if(!override && Files.exists(path)) {
			throw new IOException(
					"File already exists and will not override ("
							+ path.toString()+ ")"
			);
		}
		ByteBuffer buf = ByteBuffer.allocate(content.length());
		buf.put(content.getBytes("UTF-8"));
		buf.flip();
		try (
			SeekableByteChannel ch = Files.newByteChannel(path, 
					StandardOpenOption.CREATE, 
					StandardOpenOption.WRITE
			);
		) {	
			ch.write(buf); 
		}
	}
	
}
