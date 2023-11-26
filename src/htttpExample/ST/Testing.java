package htttpExample.ST;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Date;

public class Testing {
    static String getAuthUrl = "https://www.ystu.ru/WPROG/auth.php";
    static String getMainCmnUrl = "https://www.ystu.ru/WPROG/main.php";
    static String getLkUrl = "https://www.ystu.ru/WPROG/lk/lkstud.php";
    static String getSched = "https://www.ystu.ru/WPROG/rasp/raspz.php";
    static String login = "borisovgd.22";
    static String password = "Zgwtc,15";
    public static void main(String[] args) {
        System.out.println(new Date());
        System.out.println("On create");
        Data data = new Data();
        CloseableHttpClient client = HttpClients.createDefault();
        HTTPClientWorker clientAuthWorker = new HTTPClientWorker(client, data);
        clientAuthWorker.getRequest(getAuthUrl, RequestTypes.GET_AUTH);

        synchronized (data){
            try {
                data.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("code ystu: "+data.getCodeYSTU());
        System.out.println("On start");
        HTTPClientWorker clientLoginWorker = new HTTPClientWorker(client, data);
        clientLoginWorker.postRequest(data.getCodeYSTU(), login, password);
        clientLoginWorker.getRequest(getMainCmnUrl, RequestTypes.GET_MAIN);
        clientLoginWorker.getRequest(getLkUrl, RequestTypes.GET_LK);

        synchronized (data){
            try {
                data.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("url "+data.getScheduleUrl());
        System.out.println(new Date());
        HTTPClientWorker scheduleWorker = new HTTPClientWorker(client, data);
        scheduleWorker.getRequest(data.getScheduleUrl(), RequestTypes.GET_GROUP_SCHEDULE);
        synchronized (data){
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
