

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;


public class HTTPGetRequestHandler implements Runnable {
    private final HTTPClientHandler clientWorker;
    private final String url;
    private final RequestTypes responseType;

    public HTTPGetRequestHandler(HTTPClientHandler clientWorker, String url, RequestTypes responseType) {
        this.clientWorker = clientWorker;
        this.url = url;
        this.responseType = responseType;
    }

    @Override
    public void run() {
        Request getRequest = creatingRequest();
        try {
            convertingResponseToDocument(getRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void convertingResponseToDocument(Request getRequest) throws IOException {
        OkHttpClient httpClient = clientWorker.getHttpClient();
        try (Response response = httpClient.newCall(getRequest).execute()) {
            Document responseDoc = parsingResponse(response, responseType);
            clientWorker.getExecutor().submit(new ResponseHandler(clientWorker,responseDoc,responseType));
        }
    }

    private Request creatingRequest() {
        return new Request.Builder()
                .url(url)
                .addHeader("Host", "www.ystu.ru")
                .addHeader("Connection", "keep-alive")
                .build();
    }

    private Document parsingResponse(Response response, RequestTypes responseType) {
        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            String responseString = source.readString(Charset.forName("windows-1251"));
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
    }
}




