package br.com.bb.disec.aplic.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.fileupload.FileItem;

public class FormFieldHandler implements UploadHandler {

	private final TreeMap<String,String> map;
	
	private final List<FileItem> items;
	
	private final List<FileItem> fileUpload;
	
	
	public FormFieldHandler(List<FileItem> items) {
		if(items == null) {
			throw new IllegalArgumentException("Invalid FileItem List: "+ items);
		}
		this.items = items;
		map = new TreeMap<String,String>();
		fileUpload = new ArrayList<FileItem>();
	}
	
	
	public Map<String,String> getFormFields() {
		return map;
	}
	
	
	public List<FileItem> getFileUpload() {
		return fileUpload;
	}
	
	
	public FormFieldHandler parse() {
		map.clear();
 		for(FileItem fi : items) {
 			if(fi.isFormField()) {
 				map.put(fi.getFieldName(), fi.getString());
 			} else {
 				fileUpload.add(fi);
 			}
 		}
 		return this;
	}
	
	
	@Override
	public Map<String,String> handle() {
		if(!map.isEmpty()) {
			return map;
		}
		return parse().getFormFields();
	}

}
