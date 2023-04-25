package com.serverless;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("status")
public class Controller {
    @GetMapping
    public ResponseEntity<Response> handleRequest(Map<String, Object> input) {
        Response responseBody = new Response("Go Serverless v1.x! Your function executed successfully!", input);
        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Powered-By", "AWS Lambda & serverless")
                .body(responseBody);
    }
}
