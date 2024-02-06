import okhttp3.*;
import okio.BufferedSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.Charset;

public class HTTPPostRequestHandler implements Runnable {
    private final HTTPClientHandler clientWorker;
    private final String codeYSTU, login, password;

    public HTTPPostRequestHandler(String codeYSTU, String login, String password, HTTPClientHandler client) {
        this.clientWorker = client;
        this.codeYSTU = codeYSTU;
        this.login = login;
        this.password = password;
    }

    @Override
    public void run() {
        Request postRequest = creatingRequest();
        try {
            convertingResponseToDocument(postRequest, RequestTypes.POST_AUTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request creatingRequest() {

        RequestBody requestBody = new FormBody.Builder()
                .add("codeYSTU", codeYSTU)
                .add("login", login)
                .add("password", password)
                .build();

        return new Request.Builder()
                .url("https://www.ystu.ru/WPROG/auth1.php")
                .addHeader("Connection", "keep-alive")
                .post(requestBody)
                .build();
    }

    private void convertingResponseToDocument(Request postRequest, RequestTypes responseType) throws IOException {
        OkHttpClient httpClient = clientWorker.getHttpClient();
        try (Response response = httpClient.newCall(postRequest).execute()) {
            if (response.isSuccessful()){
                Document responseDoc = parsingResponse(response);
                clientWorker.getExecutor().submit(new ResponseHandler(clientWorker,responseDoc,responseType));
            } else System.out.println("post wrong");

        }
    }

    private Document parsingResponse(Response response) {
        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            String responseString = source.readString(Charset.forName("windows-1251"));
            return Jsoup.parse(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
