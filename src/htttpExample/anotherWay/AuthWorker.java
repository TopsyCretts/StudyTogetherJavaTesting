package htttpExample.anotherWay;

public class AuthWorker implements HttpWorker{
    private final ClientWorker clientWorker;

    public AuthWorker(ClientWorker clientWorker) {
        this.clientWorker = clientWorker;
    }

    @Override
    public void run() {

    }
    private void sendGETAuthRequest(){

    }
}
