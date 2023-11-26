package htttpExample.ST;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HTTPGetReqWorker implements Runnable {
    private final HTTPClientWorker clientWorker;
    private final String url;
    private final RequestTypes responseType;

    public HTTPGetReqWorker(HTTPClientWorker clientWorker, String url, RequestTypes responseType) {
        this.clientWorker = clientWorker;
        this.url = url;
        this.responseType = responseType;
    }

    @Override
    public void run() {
        HttpGet getRequest = creatingRequest();
        try {
            convertingResponseToDocument(getRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void convertingResponseToDocument(HttpGet getRequest) throws IOException {
        try (CloseableHttpResponse response = (CloseableHttpResponse) clientWorker.getHttpClient().execute(getRequest)) {
            Document responseDoc = parsingResponse(response, responseType);
            clientWorker.getExecutor().submit(new ResponseWorker(clientWorker,responseDoc,responseType));
        }
    }

    private HttpGet creatingRequest() {
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader(HttpHeaders.HOST, "www.ystu.ru");
        getRequest.addHeader(HttpHeaders.CONNECTION, "keep-alive");
        return getRequest;
    }

    private Document parsingResponse(CloseableHttpResponse response, RequestTypes responseType) {
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            try {
                String responseString = new String((IOUtils.toString(responseEntity.getContent(), "cp1251")).getBytes("UTF-8"));
                Document document = Jsoup.parse(responseString);
                Elements elements = document.getElementsByAttributeValue("content", "text/html; charset=windows-1251");
                for (Element element : elements){
                    element.attr("content", "text/html; charset=UTF-8");
                }
                ResponseSaver.saveResponse(document.outerHtml(), responseType.name()+".html");
                return Jsoup.parse(responseString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else return null;
        /*
         *TODO: send info to user, that response is null
         */
    }
}




