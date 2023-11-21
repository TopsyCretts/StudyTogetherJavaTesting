package htttpExample.ST;

import org.apache.http.client.HttpClient;
import org.jsoup.nodes.Document;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class uses for working with HTTP requests and responses with client instance.
 */
public class HTTPClientWorker {
    private final HttpClient httpClient;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String codeYSTU;
    private String groupScheduleUrl;
    private Document responseDoc;

    public HTTPClientWorker(HttpClient client) {
        this.httpClient = client;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
    public void setResponseDoc(Document responseDoc) {
        this.responseDoc = responseDoc;
    }

    public String getCodeYSTU() {
        return codeYSTU;
    }

    public void setCodeYSTU(String codeYSTU) {
        this.codeYSTU = codeYSTU;
    }

    public String getGroupScheduleUrl() {
        return groupScheduleUrl;
    }

    public void setGroupScheduleUrl(String groupScheduleUrl) {
        this.groupScheduleUrl = groupScheduleUrl;
    }

    public void getRequest(String url, ResponseTypes responseType) {
        executor.submit(new HTTPGetReqWorker(this, url, responseType));
    }

    public void postRequest(String codeYSTU, String login, String password) {
        executor.submit(new HTTPPostReqWorker(codeYSTU, login, password, this));
    }

}
