package io.datacleansing.fileUploader.rest;

import io.datacleansing.fileUploader.rest.representations.FileMetadata;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.*;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringInputStream;

@RestController
@RequestMapping(value= "/fileUploader")
public class FileUploaderController {
	private String bucketName;
	private AmazonS3 s3client;
	private String bucketURI;

	@PostConstruct
	private void init(){
		this.bucketName = System.getenv().get("DCH_FILE_UPLOAD_BUCKET");
		s3client = new AmazonS3Client(new EnvironmentVariableCredentialsProvider());
		s3client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
		bucketURI = "https://s3-ap-northeast-1.amazonaws.com/" + this.bucketName + "/";
	}
	
	@RequestMapping( method = RequestMethod.PUT )
	public ResponseEntity<FileMetadata> createModel(
		HttpServletRequest request,
		@RequestBody String data) {
		FileMetadata result = new FileMetadata();
		StringBuffer uri = new StringBuffer(bucketURI);
        try {
            String key = UUID.randomUUID().toString();
            StringInputStream sis = new StringInputStream(data);
            ObjectMetadata om = new ObjectMetadata();
            om.setLastModified(new Date());
            om.setContentType("DataCleansingFile");
            om.setContentLength(data.length());
            PutObjectResult pr = s3client.putObject(bucketName, key, sis, om);
            result.setMetadata(pr.getMetadata());
            uri.append(key);
            result.setUri(uri.toString());
        } catch (AmazonServiceException ase) {
        	ase.printStackTrace();
        } catch (AmazonClientException ace) {
        	ace.printStackTrace();
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
		}
        
		return new ResponseEntity<FileMetadata>(result, HttpStatus.CREATED);
	}
}
