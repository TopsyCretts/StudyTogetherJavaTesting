package htttpExample.ST;

import org.apache.http.client.HttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HTTPClientHandler {
    private final HttpClient httpClient;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final HTTPDataRequests data;


    public HTTPDataRequests getData() {
        return data;
    }
    public HTTPClientHandler(HttpClient httpClient, HTTPDataRequests data) {
        this.httpClient = httpClient;
        this.data = data;
    }
    public HttpClient getHttpClient() {
        return httpClient;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void getRequest(String url, RequestTypes responseType) {
        executor.submit(new HTTPGetRequestHandler(this, url, responseType));
    }

    public void postRequest(String codeYSTU, String login, String password) {
        executor.submit(new HTTPPostRequestHandler(codeYSTU, login, password, this));
    }

}
