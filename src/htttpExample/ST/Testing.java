package htttpExample.ST;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Date;

public class Testing {
    static String getAuthUrl = "https://www.ystu.ru/WPROG/auth.php";
    static String getMainCmnUrl = "https://www.ystu.ru/WPROG/main.php";
    static String getLkUrl = "https://www.ystu.ru/WPROG/lk/lkstud.php";
    static String getSched = "https://www.ystu.ru/WPROG/rasp/raspz.php";
    static String login = "borisovgd.2";
    static String password = "Zgwtc,15";
    public static void main(String[] args) throws InterruptedException {
        System.out.println(new Date());
        System.out.println("On create");
        HTTPDataRequests data = new HTTPDataRequests();
        CloseableHttpClient client = HttpClients.createDefault();
        HTTPClientHandler clientAuthWorker = new HTTPClientHandler(client, data);
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
        HTTPClientHandler clientLoginWorker = new HTTPClientHandler(client, data);

        clientLoginWorker.postRequest(data.getCodeYSTU(), login, password);
        synchronized (data){
            try {
                data.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (data.getIsLogged().equals("false")){
            System.out.println("Wrong data");
            System.exit(3);
        }
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
        HTTPClientHandler scheduleWorker = new HTTPClientHandler(client, data);
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
