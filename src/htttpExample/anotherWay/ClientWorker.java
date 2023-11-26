package htttpExample.anotherWay;

import org.apache.http.impl.client.CloseableHttpClient;

public class ClientWorker {
    private final CloseableHttpClient httpClient;
    private Thread authThread,loginThread,getScheduleThread;

    public ClientWorker(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void auth(){
        authThread = new Thread(new AuthWorker(this));
        authThread.setName("AuthThread");
    }
    public void login(){
        loginThread = new Thread(new LoginWorker(this));
        loginThread.setName("LoginThread");
    }
    public void getSchedule(){
        getScheduleThread = new Thread(new ScheduleWorker(this));
        getScheduleThread.setName("ScheduleThread");
    }
}
