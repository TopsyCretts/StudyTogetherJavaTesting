package parsingComponents;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ClassesParser {
    public static void getClassesInfo(ScheduleData scheduleData, ScheduleData.Day day, Elements classesElements) {
        List<ScheduleData.Day.Line> lines = new ArrayList<>();
        int hasMore = 0;
        for (int i = 0; i < classesElements.size(); i++) {

            Element lineElement = classesElements.get(i);
            ScheduleData.Day.Line line = day.new Line();
            line.setLessonInfo(line.new LessonInfo());
            if (hasMore > 0) {

                getLessonTime(line, lines.get(i - 1));
                Element weeks = (Element) lineElement.childNode(1);
                WeekParser.getWeeksInfo(scheduleData, weeks, line);
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
                WeekParser.getWeeksInfo(scheduleData, weeks, line);

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

    public static Elements getClassesElements(Element dayTableElement) {
        Elements classesElements = new Elements();

        Element tableDataElement = (Element) dayTableElement.childNode(3);
        for (int i = 1; i < tableDataElement.childNodeSize(); i += 2) {
            classesElements.add((Element) tableDataElement.childNode(i));
        }
        return classesElements;
    }

    public static int hasMore(Element lineElement) {
        Element lineType = (Element) lineElement.childNode(1);
        if (lineType.hasAttr("rowspan")) {
            String value = lineType.attr("rowspan");
            return Integer.parseInt(value) - 1;
        } else return 0;
    }

    public static boolean isSingle(Element lineElement) {
        return lineElement.childNodeSize() == 13 && hasMore(lineElement) == 0;
    }

    public static void getLessonTime(Element element, ScheduleData.Day.Line line) {
        TextNode timeTextNode = (TextNode) element.childNode(0);
        String[] timeString = timeTextNode.text().split(" ");
        line.setNumberOfLesson(timeString[0]);
        line.getLessonInfo().setLessonTime(timeString[1]);
    }

    public static void getLessonTime(ScheduleData.Day.Line currentLine, ScheduleData.Day.Line prevLine) {
        currentLine.setNumberOfLesson(prevLine.getNumberOfLesson());
        currentLine.getLessonInfo().setLessonTime(prevLine.getLessonInfo().getLessonTime());
    }
}
