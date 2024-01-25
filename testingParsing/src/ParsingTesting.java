import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import parsingComponents.DayParser;
import parsingComponents.ScheduleData;
import parsingComponents.WeekParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class ParsingTesting {
    ScheduleData scheduleData = new ScheduleData();

    public static void main(String[] args) throws IOException {
        ParsingTesting parsingTesting = new ParsingTesting();

        Document document = Jsoup.parse(new File("GET_GROUP_SCHEDULE.html"));

        Elements weeksElements = document.getElementsByTag("option");
        for (Element week : weeksElements) {
            WeekParser.parseWeek(parsingTesting.scheduleData, week);
        }

        Elements daysElements = document.getElementsByAttributeValue(" style", "\"margin-left: 30px;\"");
        parsingTesting.getScheduleName(daysElements);
        Elements days = DayParser.getDaysElements(daysElements);
        for (Element day : days) {
            DayParser.getDayInfo(parsingTesting.scheduleData, day);
        }
        File data = new File("data");
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(data));
        outputStream.writeObject(parsingTesting.scheduleData);
        outputStream.flush();
        System.out.println("done");
    }

    private void getScheduleName(Elements scheduleInfo) {
        Element nameElement = scheduleInfo.get(1);
        TextNode nameNode = (TextNode) nameElement.childNode(0);
        scheduleData.setName(nameNode.text());
    }







}
