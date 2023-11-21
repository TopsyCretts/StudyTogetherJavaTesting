package htttpExample.ST;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Testing {
    static String getAuthUrl = "https://www.ystu.ru/WPROG/auth.php";
    static String getMainCmnUrl = "https://www.ystu.ru/WPROG/main.php";
    static String getLkUrl = "https://www.ystu.ru/WPROG/lk/lkstud.php";
    static String getSched = "https://www.ystu.ru/WPROG/rasp/raspz.php";
    static String login = "borisovgd.22";
    static String password = "Zgwtc,15";
    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createDefault();
        HTTPClientWorker clientWorker = new HTTPClientWorker(client);
        clientWorker.getRequest(getAuthUrl, ResponseTypes.GET_AUTH);
        clientWorker.postRequest(clientWorker.getCodeYSTU(), login, password);
        clientWorker.getRequest(getMainCmnUrl, ResponseTypes.GET_MAIN);
        clientWorker.getRequest(getLkUrl, ResponseTypes.GET_LK);
        clientWorker.getRequest(clientWorker.getGroupScheduleUrl(), ResponseTypes.GET_GROUP_SCHEDULE);

    }
}
