package parsingComponents;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeekParser {
    public static void getWeeksInfo(ScheduleData scheduleData, Element element, ScheduleData.Day.Line line) {
        String[] weeksStringArray = ((TextNode) element.childNode(0)).text().split(",");
        Pattern patternSingleDigit = Pattern.compile("\\d*");
        Pattern patternNoType = Pattern.compile("(\\d*-\\d*)");
        Pattern patternTyped = Pattern.compile("([нч])/н (\\d*-\\d*|\\d*)");
        for (String string : weeksStringArray) {
            String value;
            Matcher matcher = patternTyped.matcher(string);
            if (matcher.find() && !(value = matcher.group()).isEmpty()) {
                getWeeks(scheduleData, value, 3, line);
            } else {
                if ((matcher = patternNoType.matcher(string)).find() && !(value = matcher.group()).isEmpty()) {
                    getWeeks(scheduleData, value, 2, line);
                } else {
                    if ((matcher = patternSingleDigit.matcher(string)).find() && !(value = matcher.group()).isEmpty()) {
                        getWeeks(scheduleData, value, 1, line);
                    }
                }
            }
        }
    }

    public static void getWeeks(ScheduleData scheduleData, String weeksString, int type, ScheduleData.Day.Line line) {
        switch (type) {
            case 1 -> {
                int weekNumber = Integer.parseInt(weeksString);
                line.getLessonInfo().getWeeks().add(scheduleData.getWeeks().get(weekNumber - 1));
            }
            case 2 -> {
                String[] weeksArray = weeksString.split("-");
                int first = Integer.parseInt(weeksArray[0]);
                int last = Integer.parseInt(weeksArray[1]);
                for (int i = first; i <= last; i++) {
                    line.getLessonInfo().getWeeks().add(scheduleData.getWeeks().get(i - 1));
                }
            }
            case 3 -> {
                String[] weeksArrayTyped = weeksString.split(" ");
                String[] weeksTyped = weeksArrayTyped[1].split("-");
                if (weeksTyped.length == 2) {
                    int firstTyped = Integer.parseInt(weeksTyped[0]);
                    int lastTyped = Integer.parseInt(weeksTyped[1]);
                    for (int i = firstTyped; i <= lastTyped; i += 2) {
                        line.getLessonInfo().getWeeks().add(scheduleData.getWeeks().get(i - 1));
                    }
                } else {
                    line.getLessonInfo().getWeeks().add(scheduleData.getWeeks().get(Integer.parseInt(weeksTyped[0])));
                }
            }
        }
    }

    public static void parseWeek(ScheduleData scheduleData, Element weekElement) {
        String[] startInfo = weekElement.attr("value").split(" ");
        String weekNumber = startInfo[0];
        String dateStart = startInfo[2];
        ScheduleData.Week week = scheduleData.new Week();
        week.setWeekNumber(weekNumber);
        if (Integer.parseInt(weekNumber) % 2 == 0) {
            week.setWeekType(ScheduleData.Week.WeekType.EVEN);
        } else {
            week.setWeekType(ScheduleData.Week.WeekType.ODD);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startWeek = LocalDate.parse(dateStart, formatter);
        LocalDate endWeek = startWeek.plusDays(6);
        week.setStart(startWeek);
        week.setEnd(endWeek);
        scheduleData.getWeeks().add(week);
    }

}
