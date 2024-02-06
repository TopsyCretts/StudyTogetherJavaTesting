import okhttp3.*;
import okio.BufferedSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;

public class TestingSoloOk {
    static String getAuthUrl = "https://www.ystu.ru/WPROG/auth.php";
    static String getMainCmnUrl = "https://www.ystu.ru/WPROG/main.php";
    static String getLkUrl = "https://www.ystu.ru/WPROG/lk/lkstud.php";
    static String getSched = "https://www.ystu.ru/WPROG/rasp/raspz.php";
    static String login = "borisovgd.22";
    static String password = "Zgwtc,15";

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println(new Date());
        System.out.println("On create");

        OkHttpClient httpClient = new OkHttpClient();

        HTTPDataRequests data = new HTTPDataRequests();
        HTTPClientHandler clientWorker = new HTTPClientHandler(httpClient, data);

        System.out.println("On start");

        // GET запрос для получения кода YSTU
        Request getRequest = new Request.Builder()
                .url(getAuthUrl)
                .addHeader("Host", "www.ystu.ru")
                .addHeader("Connection", "keep-alive")
                .build();

        try (Response response = httpClient.newCall(getRequest).execute()) {
            Document responseDoc = parsingResponse(response, RequestTypes.GET_AUTH);
            String codeYSTU = getCodeYSTU(responseDoc);
            System.out.println("code ystu: " + codeYSTU);

            // POST запрос для авторизации
            RequestBody requestBody = new FormBody.Builder()
                    .add("codeYSTU", codeYSTU)
                    .add("login", login)
                    .add("password", password)
                    .build();

            Request postRequest = new Request.Builder()
                    .url("https://www.ystu.ru/WPROG/auth1.php")
                    .addHeader("Connection", "keep-alive")
                    .post(requestBody)
                    .build();

            try (Response postResponse = httpClient.newCall(postRequest).execute()) {
                if (postResponse.isSuccessful()) {
                    Document postResponseDoc = parsingResponse(postResponse, RequestTypes.POST_AUTH);
                    boolean isLogged = checkIsLogged(postResponseDoc);
                    if (!isLogged) {
                        System.out.println("Wrong data");
                        System.exit(3);
                    }

                    // GET запросы для получения данных
                    clientWorker.getRequest(getMainCmnUrl, RequestTypes.GET_MAIN);
                    clientWorker.getRequest(getLkUrl, RequestTypes.GET_LK);

                    // GET запрос для получения расписания
                    String scheduleUrl = getScheduleUrlFromDocument(postResponseDoc);
                    clientWorker.getRequest(scheduleUrl, RequestTypes.GET_GROUP_SCHEDULE);
                }
            }
        }

        System.out.println(new Date());
    }

    private static Document parsingResponse(Response response, RequestTypes responseType) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        String responseString = source.readString(Charset.forName("windows-1251"));
        Document document = Jsoup.parse(responseString);
        Elements elements = document.getElementsByAttributeValue("content", "text/html; charset=windows-1251");
        for (Element element : elements) {
            element.attr("content", "text/html; charset=UTF-8");
        }
        ResponseSaver.saveResponse(document.outerHtml(), responseType.name() + ".html");
        return document;
    }

    private static String getCodeYSTU(Document responseDocument) {
        Elements elements = responseDocument.getElementsByAttributeValue("name", "codeYSTU");
        for (Element element : elements) {
            String codeYSTU = element.attr("value");
            return codeYSTU;
        }
        return null;
    }

    private static boolean checkIsLogged(Document responseDocument) {
        Elements elements = responseDocument.getElementsByTag("title");
        if (elements.size() > 0) {
            return true;
        } else return false;
    }

    private static String getScheduleUrlFromDocument(Document document) {
        Elements elements = document.getElementsByAttributeValue("title", "В расписание группы!");
        for (Element element : elements){
            String groupScheduleUrl = ("https://www.ystu.ru"+element.attr("href"));
            return groupScheduleUrl;
        }
        return null;
    }
}
