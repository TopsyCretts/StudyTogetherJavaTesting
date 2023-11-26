package htttpExample.anotherWay;

import htttpExample.ST.RequestTypes;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;

public interface HttpWorker extends Runnable{
    default HttpGet createGetRequest(RequestTypes requestType){
        HttpGet getRequest=null;
        switch (requestType){
            case GET_AUTH -> getRequest = new HttpGet("https://www.ystu.ru/WPROG/auth.php");
            case GET_LK -> getRequest = new HttpGet("https://www.ystu.ru/WPROG/lk/lkstud.php");
            case GET_MAIN -> getRequest = new HttpGet("https://www.ystu.ru/WPROG/main.php");
        }
        getRequest.addHeader(HttpHeaders.HOST, "www.ystu.ru");
        getRequest.addHeader(HttpHeaders.CONNECTION, "keep-alive");
        return getRequest;
    }

}
