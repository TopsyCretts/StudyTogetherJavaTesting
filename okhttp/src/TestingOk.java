import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TestingOk {
    static String getAuthUrl = "https://www.ystu.ru/WPROG/auth.php";
    static String getMainCmnUrl = "https://www.ystu.ru/WPROG/main.php";
    static String getLkUrl = "https://www.ystu.ru/WPROG/lk/lkstud.php";
    static String getSched = "https://www.ystu.ru/WPROG/rasp/raspz.php";
    static String login = "borisovgd.22";
    static String password = "Zgwtc,15";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<>();
                }
            })
            .build();

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println(new Date());
        System.out.println("On create");

        HTTPDataRequests data = new HTTPDataRequests();
        HTTPClientHandler clientAuthWorker = new HTTPClientHandler(client, data);
        clientAuthWorker.getRequest(getAuthUrl, RequestTypes.GET_AUTH);

        synchronized (data) {
            try {
                data.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("code ystu: " + data.getCodeYSTU());
        System.out.println("On start");

        HTTPClientHandler clientLoginWorker = new HTTPClientHandler(client, data);
        clientLoginWorker.postRequest(data.getCodeYSTU(), login, password);

        synchronized (data) {
            try {
                data.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (data.getIsLogged().equals("false")) {
            System.out.println("Wrong data");
            System.exit(3);
        }

        clientLoginWorker.getRequest(getMainCmnUrl, RequestTypes.GET_MAIN);
        clientLoginWorker.getRequest(getLkUrl, RequestTypes.GET_LK);

        synchronized (data) {
            try {
                data.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("url " + data.getScheduleUrl());
        System.out.println(new Date());

        HTTPClientHandler scheduleWorker = new HTTPClientHandler(client, data);
        scheduleWorker.getRequest(data.getScheduleUrl(), RequestTypes.GET_GROUP_SCHEDULE);

        synchronized (data) {
            try {
                data.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(data.getIsScheduleLoaded());
        System.out.println(new Date());
    }
}