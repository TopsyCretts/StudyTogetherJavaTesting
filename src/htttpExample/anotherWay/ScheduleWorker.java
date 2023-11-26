package htttpExample.anotherWay;

public class ScheduleWorker implements HttpWorker{
    private final ClientWorker clientWorker;

    public ScheduleWorker(ClientWorker clientWorker) {
        this.clientWorker = clientWorker;
    }

    @Override
    public void run() {

    }
}
