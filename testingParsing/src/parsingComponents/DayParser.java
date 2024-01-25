package parsingComponents;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.time.DayOfWeek;

import static parsingComponents.ClassesParser.getClassesElements;
import static parsingComponents.ClassesParser.getClassesInfo;

public class DayParser {
    public static Elements getDaysElements(Elements parsedElements) {
        Elements days = new Elements();
        for (int i = 2; i < parsedElements.size(); i++) {
            days.add(parsedElements.get(i));
        }
        return days;
    }

    public static void getDayInfo(ScheduleData scheduleData, Element dayElement) {
        ScheduleData.Day day = scheduleData.new Day();
        setDayName(day, (Element) dayElement.childNode(1));
        Elements classesElements = getClassesElements((Element) dayElement.childNode(3));
        getClassesInfo(scheduleData, day, classesElements);
        scheduleData.getDays().add(day);

    }

    public static void setDayName(ScheduleData.Day day, Element dayNameElement) {
        switch (((TextNode) dayNameElement.childNode(1).childNode(0)).text()) {
            case "Понедельник" -> day.setDayName(DayOfWeek.MONDAY);
            case "Вторник" -> day.setDayName(DayOfWeek.TUESDAY);
            case "Среда" -> day.setDayName(DayOfWeek.WEDNESDAY);
            case "Четверг" -> day.setDayName(DayOfWeek.THURSDAY);
            case "Пятница" -> day.setDayName(DayOfWeek.FRIDAY);
            case "Суббота" -> day.setDayName(DayOfWeek.SATURDAY);
            case "Воскресенье" -> day.setDayName(DayOfWeek.SUNDAY);
        }
    }
}
