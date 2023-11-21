package htttpExample.ST;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTTPPostReqWorker implements Runnable {
    private final HTTPClientWorker clientWorker;
    private final String codeYSTU, login, password;

    public HTTPPostReqWorker(String codeYSTU, String login, String password, HTTPClientWorker client) {
        this.clientWorker = client;
        this.codeYSTU = codeYSTU;
        this.login = login;
        this.password = password;
    }

    @Override
    public void run() {
        creatingRequest();
    }

    private void creatingRequest() {
        HttpPost postRequest = new HttpPost("https://www.ystu.ru/WPROG/auth1.php");
        postRequest.addHeader(HttpHeaders.CONNECTION, "keep-alive");
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("codeYSTU", codeYSTU));
        urlParameters.add(new BasicNameValuePair("login", login));
        urlParameters.add(new BasicNameValuePair("password", password));
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            convertingResponseToDocument(postRequest, ResponseTypes.POST_AUTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void convertingResponseToDocument(HttpPost postRequest, ResponseTypes responseType) throws IOException {
        try (CloseableHttpResponse response = (CloseableHttpResponse)
                clientWorker.getHttpClient().execute(postRequest)) {
            Document responseDoc = parsingResponse(response);
            clientWorker.getExecutor().submit(new ResponseWorker(clientWorker,responseDoc,responseType));
        }
    }

    private Document parsingResponse(CloseableHttpResponse response) {
        try {
            return Jsoup.parse(IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
