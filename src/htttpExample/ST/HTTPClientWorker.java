package htttpExample.ST;

import org.apache.http.client.HttpClient;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class uses for working with HTTP requests and responses with client instance.
 */
public class HTTPClientWorker {
    private final HttpClient httpClient;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String codeYSTU;
    private String groupScheduleUrl;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public HTTPClientWorker(HttpClient httpClient, Data data) {
        this.httpClient = httpClient;
        this.data = data;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public ExecutorService getExecutor() {
        return executor;
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

    public void getRequest(String url, RequestTypes responseType) {
        executor.submit(new HTTPGetReqWorker(this, url, responseType));
    }

    public void postRequest(String codeYSTU, String login, String password) {
        executor.submit(new HTTPPostReqWorker(codeYSTU, login, password, this));
    }

}
