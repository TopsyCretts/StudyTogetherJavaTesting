import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class GettingInfoTesting {
    public static void main(String[] args) {
        ScheduleData scheduleData;
        File data = new File("data");
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(data));
            scheduleData = (ScheduleData) inputStream.readObject();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        LocalDate today = LocalDate.of(2023,11,27);
        ScheduleData.Week currentWeek = null;
        for (ScheduleData.Week week : scheduleData.getWeeks()) {
            LocalDate start = week.getStart();
            LocalDate end = week.getEnd();
            if (today.isEqual(start) | today.isEqual(end) | (today.isAfter(start) && today.isBefore(end))) {
                currentWeek = week;
                break;
            }
        }
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        ScheduleData.Day currentDay = null;
        for (ScheduleData.Day day : scheduleData.getDays()) {
            if (day.getDayName() == dayOfWeek) {
                currentDay = day;
                break;
            }
        }
        if (currentDay==null){
            System.out.println("Занятий нет!");
            System.exit(0);
        }
        for (ScheduleData.Day.Line line : currentDay.getLines()) {
            if (line.getLessonInfo().getWeeks().contains(currentWeek)){
                System.out.println("Номер пары:" + line.getNumberOfLesson());
                ScheduleData.Day.Line.LessonInfo info = line.getLessonInfo();
                System.out.println("Время: " + info.getLessonTime());
                System.out.println("Дисциплина: " + info.getDiscipline());
                System.out.println("Аудитория: " + info.getClassroom());
                System.out.println("Тип занятия: " + info.getLessonType());
                System.out.println("Преподаватель: " + info.getTeacher());
                System.out.println("--------------");
            }

        }

    }
}
