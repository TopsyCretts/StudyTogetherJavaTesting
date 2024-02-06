import okhttp3.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HTTPClientHandler {
    private final OkHttpClient httpClient;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final HTTPDataRequests data;


    public HTTPDataRequests getData() {
        return data;
    }

    public HTTPClientHandler(OkHttpClient httpClient, HTTPDataRequests data) {
        this.httpClient = httpClient;
        this.data = data;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void getRequest(String url, RequestTypes responseType) {
        executor.submit(new HTTPGetRequestHandler(this,url, responseType));
    }

    public void postRequest(String codeYSTU, String login, String password) {
        executor.submit(new HTTPPostRequestHandler(codeYSTU, login, password, this));
    }
}