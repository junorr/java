package br.com.bb.disec.aplic.upload;

public class NameHandler implements UploadHandler {

	private final String path;
	
	private final String basename;
	
	
	public NameHandler(String path, String basename) {
		if(path == null || path.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid file path: "+ path);
		}
		if(basename == null || basename.trim().isEmpty()) {
			throw new IllegalArgumentException("Invalid file base name: "+ basename);
		}
		this.path = path;
		this.basename = basename;
	}
	
	
	public String getPath() {
		return path;
	}
	
	
	public String getBaseName() {
		return basename;
	}
	
	
	@Override
	public String handle() {
		StringBuilder sb = new StringBuilder()
			.append(path);
		if(!path.endsWith("/")
				&& !path.endsWith("\\")) {
			sb.append("/");
		}
		sb.append(String.valueOf(System.currentTimeMillis()))
			.append("_").append(basename);
		return sb.toString();
	}

}
