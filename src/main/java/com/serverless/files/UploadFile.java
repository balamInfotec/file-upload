package com.serverless.files;

import java.io.File;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class UploadFile {
	public void uploadFiles() {
		String bucketName = "fileupload-balam";
		String key = "prueba/prueba.png";
		
		S3Client client = S3Client.builder().build();
		
		PutObjectRequest request = PutObjectRequest.builder()
										.bucket(bucketName)
										.key(key)
										.build();
		File file = new File("C:\\AMBIENTE\\descarga.png");
		
		client.putObject(request, RequestBody.fromFile(file));
		
	}
	
	public void uploadFiles(byte[] bytes, String key) {
		String bucketName = "fileupload-balam";
		
		S3Client client = S3Client.builder().build();
		
		PutObjectRequest request = PutObjectRequest.builder()
										.bucket(bucketName)
										.key(key)
										.build();
		
		client.putObject(request, RequestBody.fromBytes(bytes));
		
	}
}
