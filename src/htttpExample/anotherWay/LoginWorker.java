package htttpExample.anotherWay;

public class LoginWorker implements HttpWorker{
    private final ClientWorker clientWorker;

    public LoginWorker(ClientWorker clientWorker) {
        this.clientWorker = clientWorker;
    }

    @Override
    public void run() {

    }
}
