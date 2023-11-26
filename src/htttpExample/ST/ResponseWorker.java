package htttpExample.ST;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResponseWorker implements Runnable {
    private final RequestTypes responseType;
    private final Document documentResponse;
    private final HTTPClientWorker clientWorker;

    public ResponseWorker(HTTPClientWorker clientWorker, Document documentResponse, RequestTypes responseType) {
        this.responseType = responseType;
        this.documentResponse = documentResponse;
        this.clientWorker = clientWorker;
    }

    @Override
    public void run() {
        switch (responseType) {
            case GET_AUTH -> getAuthResponse(documentResponse);
            case GET_CMN_SCHEDULE -> getCmnSchedResponse(documentResponse);
            case POST_AUTH -> postAuthResponse(documentResponse);
            case GET_MAIN -> getMainResponse(documentResponse);
            case GET_LK -> getLkResponse(documentResponse);
            case GET_GROUP_SCHEDULE -> getGrSchResponse(documentResponse);
            default -> {
            }
        }
    }

    private void getAuthResponse(Document responseDocument) {
        Elements elements = responseDocument.getElementsByAttributeValue("name", "codeYSTU");
        for (Element element : elements) {
            String codeYSTU = element.attr("value");
            synchronized (clientWorker.getData()){
                clientWorker.getData().setCodeYSTU(codeYSTU);
                clientWorker.getData().notifyAll();
            }
        }
    }

    private void getCmnSchedResponse(Document responseDocument) {

    }

    private void postAuthResponse(Document responseDocument) {

    }

    private void getMainResponse(Document responseDocument) {
    }

    private void getLkResponse(Document responseDocument) {
        Elements elements = responseDocument.getElementsByAttributeValue("title", "В расписание группы!");
        for (Element element : elements){
           String groupScheduleUrl = ("https://www.ystu.ru"+element.attr("href"));
           synchronized (clientWorker.getData()){
               clientWorker.getData().setScheduleUrl(groupScheduleUrl);
               clientWorker.getData().notifyAll();
           }

        }

    }
    private void getGrSchResponse(Document responseDocument){
        Elements elements = responseDocument.getAllElements();
        synchronized (clientWorker.getData()){
            clientWorker.getData().setIsScheduleLoaded("yep");
            clientWorker.getData().notifyAll();
        }

    }
}
