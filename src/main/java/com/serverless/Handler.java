package com.serverless;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.MultipartStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.serverless.files.UploadFile;
import com.serverless.model.ApiResponse;

public class Handler implements RequestHandler<Map<String, Object>, ApiResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
/*
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		String error = "";
		try {
			UploadFile uploadFile = new UploadFile();
			uploadFile.uploadFiles();	
		} catch (Exception e) {
			error = e.getMessage();
		}
		
		Response responseBody = new Response(error, input);
		
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}*/

	@Override
	public ApiResponse handleRequest(Map<String, Object> input, Context context) {
		JSONObject json = new JSONObject();
		ApiResponse response = null;
		String contentType = "";
		String fileObjKeyName = "";
		Regions clientRegion = Regions.US_EAST_1;
		String bucketName = "fileupload-balam";
		
		try {
			byte[] bI = Base64.getDecoder().decode(input.get("body").toString().getBytes());
			Map<String, String> requestHeader = (Map<String, String>) input.get("headers");
			json.putAll(requestHeader);
			if(requestHeader != null) {
				contentType = requestHeader.get("content-type");
			}
			
			json.put("Contenido", contentType);
			
			
			String[] boundaryArray = contentType.split("=");
			byte[] boundary = boundaryArray[1].getBytes();
			
			ByteArrayInputStream content = new ByteArrayInputStream(bI);
			
			MultipartStream multipartStream = new MultipartStream(content, boundary, bI.length, null);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			boolean nextPart = multipartStream.skipPreamble();
			
			while(nextPart) {
				String header = multipartStream.readHeaders();
				
				fileObjKeyName = getFileName(header, "filename");
				
				multipartStream.readBodyData(out);
				
				nextPart = multipartStream.readBoundary();
			}
			json.put("nombreArchivo", fileObjKeyName);
			//InputStream fileInputStream = new ByteArrayInputStream(out.toByteArray());
			
			UploadFile upload = new UploadFile();			
			upload.uploadFiles(out.toByteArray(), fileObjKeyName);
			
			/*AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
			
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(out.toByteArray().length);
			
			s3Client.putObject(bucketName, fileObjKeyName, fileInputStream, metadata);*/
			
			
			json.put("Status",  "File stored in S3");
			response = new ApiResponse(json.toJSONString(), 200);
		} catch(Exception e) {
			json.put("Message", e.getMessage());
			json.put("Cause", e.getCause());
			response = new ApiResponse(json.toJSONString(), 400);
		}
		
		return response;
	}
	
    private static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        // append = false
        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }

    }
	
	private String getFileName(String str, String field) {
		String result = null;
		
		int index = str.indexOf(field);
		
		if(index >= 0) {
			int first = str.indexOf("\"", index);
			int second = str.indexOf("\"", first + 1);
			result = str.substring(first+1, second);
		}
		
		return result;
	}


}
