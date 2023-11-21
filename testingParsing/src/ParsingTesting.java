import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class ParsingTesting {
    public static void main(String[] args) throws IOException {
        Document document = Jsoup.parse(new File("GET_LK.html"));
        Elements elements = document.getElementsByAttributeValue("title", "В расписание группы!");
        for (Element element : elements){
            String result = element.attr("href");
            System.out.println(result);
        }


    }

}
