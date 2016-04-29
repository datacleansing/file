package io.datacleansing.fileUploader.rest.representations;

import com.amazonaws.services.s3.model.ObjectMetadata;

public class FileMetadata {
	private String uri;
	private ObjectMetadata metadata;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public ObjectMetadata getMetadata() {
		return metadata;
	}
	public void setMetadata(ObjectMetadata metadata) {
		this.metadata = metadata;
	}
	
	
}
