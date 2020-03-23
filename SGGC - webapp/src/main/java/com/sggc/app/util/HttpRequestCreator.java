package com.sggc.app.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HttpRequestCreator {
    private HttpClient client;
    private Header contentTypeHeader;
    private String URI;


    public HttpRequestCreator(String URI) {
        contentTypeHeader = new BasicHeader("Content-Type", "application/json; charset=UTF-8");
        client = HttpClientBuilder.create().build();
        this.URI = URI;
    }

    public String getAll() throws IOException {
        try {
            client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(URI);
            get.addHeader(contentTypeHeader);

            HttpResponse response = client.execute(get);

            return inputStreamToString(response.getEntity().getContent());

        } catch (IOException e) {
           throw e;
        }
    }

    private String inputStreamToString(InputStream is) throws IOException {
        String line = "";
        StringBuilder total = new StringBuilder();

        try(BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return total.toString();
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }
}
