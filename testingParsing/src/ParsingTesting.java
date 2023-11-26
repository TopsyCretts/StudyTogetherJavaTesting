import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParsingTesting {
    ScheduleData scheduleData = new ScheduleData();

    public static void main(String[] args) throws IOException {
        ParsingTesting parsingTesting = new ParsingTesting();

        Document document = Jsoup.parse(new File("GET_GROUP_SCHEDULE.html"));

        Elements weeksElements = document.getElementsByTag("option");
        for (Element week : weeksElements) {
            parsingTesting.parseWeek(week);
        }

        Elements daysElements = document.getElementsByAttributeValue(" style", "\"margin-left: 30px;\"");
        parsingTesting.getScheduleName(daysElements);
        Elements days = parsingTesting.getDaysElements(daysElements);
        for (Element day : days) {
            parsingTesting.getDayInfo(day);
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

    private Elements getDaysElements(Elements parsedElements) {
        Elements days = new Elements();
        for (int i = 2; i < parsedElements.size(); i++) {
            days.add(parsedElements.get(i));
        }
        return days;
    }

    private void getDayInfo(Element dayElement) {
        ScheduleData.Day day = scheduleData.new Day();
        setDayName(day, (Element) dayElement.childNode(1));
        Elements classesElements = getClassesElements((Element) dayElement.childNode(3));
        getClassesInfo(day, classesElements);
        scheduleData.getDays().add(day);

    }

    private void setDayName(ScheduleData.Day day, Element dayNameElement) {
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

    private void getClassesInfo(ScheduleData.Day day, Elements classesElements) {
        List<ScheduleData.Day.Line> lines = new ArrayList<>();
        int hasMore = 0;
        for (int i = 0; i < classesElements.size(); i++) {

            Element lineElement = classesElements.get(i);
            ScheduleData.Day.Line line = day.new Line();
            line.setLessonInfo(line.new LessonInfo());
            if (hasMore > 0) {

                getLessonTime(line, lines.get(i - 1));
                Element weeks = (Element) lineElement.childNode(1);
                getWeeksInfo(weeks, line);
                Element discipline = (Element) lineElement.childNode(3);
                line.getLessonInfo().setDiscipline(((TextNode) discipline.childNode(discipline.childNodeSize() - 1)).text());

                Element lessonType = (Element) lineElement.childNode(5);
                if (lessonType.childNode(0).getClass().equals(TextNode.class)) {
                    line.getLessonInfo().setLessonType(((TextNode) lessonType.childNode(0)).text());
                }


                Element classRoom = (Element) lineElement.childNode(7);
                if (classRoom.childNodeSize() != 0) {
                    line.getLessonInfo().setClassroom(((TextNode) classRoom.childNode(0)).text());
                }


                Element teacher = (Element) lineElement.childNode(9);
                if (teacher.childNodeSize() != 0) {
                    line.getLessonInfo().setTeacher(((TextNode) teacher.childNode(0)).text());
                }

                lines.add(line);
                if (hasMore > 1) {
                    hasMore--;
                } else {
                    hasMore = hasMore(lineElement);
                }
                continue;
            }
            if (hasMore(lineElement) > 0 || i == classesElements.size() - 1 || i == 0 || isSingle(lineElement)) {
                hasMore = hasMore(lineElement);
                Element classes = (Element) lineElement.childNode(1);
                getLessonTime(classes, line);
                Element weeks = (Element) lineElement.childNode(3);
                getWeeksInfo(weeks, line);

                Element discipline = (Element) lineElement.childNode(5);
                line.getLessonInfo().setDiscipline(((TextNode) discipline.childNode(discipline.childNodeSize() - 1)).text());

                Element lessonType = (Element) lineElement.childNode(7);
                line.getLessonInfo().setLessonType(((TextNode) lessonType.childNode(0)).text());

                Element classRoom = (Element) lineElement.childNode(9);
                line.getLessonInfo().setClassroom(((TextNode) classRoom.childNode(0)).text());

                Element teacher = (Element) lineElement.childNode(11);
                if (teacher.childNodeSize() != 0) {
                    line.getLessonInfo().setTeacher(((TextNode) teacher.childNode(0)).text());
                }
                lines.add(line);
            }
            day.setLines(lines);
        }

    }

    private Elements getClassesElements(Element dayTableElement) {
        Elements classesElements = new Elements();

        Element tableDataElement = (Element) dayTableElement.childNode(3);
        for (int i = 1; i < tableDataElement.childNodeSize(); i += 2) {
            classesElements.add((Element) tableDataElement.childNode(i));
        }
        return classesElements;
    }

    private int hasMore(Element lineElement) {
        Element lineType = (Element) lineElement.childNode(1);
        if (lineType.hasAttr("rowspan")) {
            String value = lineType.attr("rowspan");
            return Integer.parseInt(value) - 1;
        } else return 0;
    }

    private boolean isSingle(Element lineElement) {
        return lineElement.childNodeSize() == 13 && hasMore(lineElement) == 0;
    }

    private void getLessonTime(Element element, ScheduleData.Day.Line line) {
        TextNode timeTextNode = (TextNode) element.childNode(0);
        String[] timeString = timeTextNode.text().split(" ");
        line.setNumberOfLesson(timeString[0]);
        line.getLessonInfo().setLessonTime(timeString[1]);
    }

    private void getLessonTime(ScheduleData.Day.Line currentLine, ScheduleData.Day.Line prevLine) {
        currentLine.setNumberOfLesson(prevLine.getNumberOfLesson());
        currentLine.getLessonInfo().setLessonTime(prevLine.getLessonInfo().getLessonTime());
    }

    private void getWeeksInfo(Element element, ScheduleData.Day.Line line) {
        String[] weeksStringArray = ((TextNode) element.childNode(0)).text().split(",");
        Pattern patternSingleDigit = Pattern.compile("\\d*");
        Pattern patternNoType = Pattern.compile("(\\d*-\\d*)");
        Pattern patternTyped = Pattern.compile("([нч])/н (\\d*-\\d*|\\d*)");
        for (String string : weeksStringArray) {
            String value;
            Matcher matcher = patternTyped.matcher(string);
            if (matcher.find() && !(value = matcher.group()).isEmpty()) {
                getWeeks(value, 3, line);
            } else {
                if ((matcher = patternNoType.matcher(string)).find() && !(value = matcher.group()).isEmpty()) {
                    getWeeks(value, 2, line);
                } else {
                    if ((matcher = patternSingleDigit.matcher(string)).find() && !(value = matcher.group()).isEmpty()) {
                        getWeeks(value, 1, line);
                    }
                }
            }
        }
    }

    private void getWeeks(String weeksString, int type, ScheduleData.Day.Line line) {
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

    private void parseWeek(Element weekElement) {
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
