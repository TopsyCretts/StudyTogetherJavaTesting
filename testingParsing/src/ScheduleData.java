import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleData implements Serializable {
    private String name;
    private List<Week> weeks = new ArrayList<>();
    private List<Day> days = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    class Day implements Serializable{

        DayOfWeek dayName;
        List<Line> lines;

        public DayOfWeek getDayName() {
            return dayName;
        }

        public void setDayName(DayOfWeek dayName) {
            this.dayName = dayName;
        }

        public List<Line> getLines() {
            return lines;
        }

        public void setLines(List<Line> lines) {
            this.lines = lines;
        }

        class Line implements Serializable{

            private String numberOfLesson;
            private LessonInfo lessonInfo;

            public LessonInfo getLessonInfo() {
                return lessonInfo;
            }

            public void setLessonInfo(LessonInfo lessonInfo) {
                this.lessonInfo = lessonInfo;
            }

            public String getNumberOfLesson() {
                return numberOfLesson;
            }


            public void setNumberOfLesson(String numberOfLesson) {
                this.numberOfLesson = numberOfLesson;
            }

            class LessonInfo implements Serializable{
                private String lessonTime;
                private List<Week> weeks = new ArrayList<>();
                private String discipline;
                private String lessonType;
                private String classroom;
                private String teacher;

                public String getLessonTime() {
                    return lessonTime;
                }

                public void setLessonTime(String lessonTime) {
                    this.lessonTime = lessonTime;
                }

                public List<Week> getWeeks() {
                    return weeks;
                }

                public void setWeeks(List<Week> weeks) {
                    this.weeks = weeks;
                }

                public String getDiscipline() {
                    return discipline;
                }

                public void setDiscipline(String discipline) {
                    this.discipline = discipline;
                }

                public String getLessonType() {
                    return lessonType;
                }

                public void setLessonType(String lessonType) {
                    this.lessonType = lessonType;
                }

                public String getClassroom() {
                    return classroom;
                }

                public void setClassroom(String classroom) {
                    this.classroom = classroom;
                }

                public String getTeacher() {
                    return teacher;
                }

                public void setTeacher(String teacher) {
                    this.teacher = teacher;
                }
            }

        }
    }

    class Week implements Serializable{
        private String weekNumber;
        private WeekType weekType;
        private LocalDate start;
        private LocalDate end;

        public LocalDate getStart() {
            return start;
        }

        public void setStart(LocalDate start) {
            this.start = start;
        }

        public LocalDate getEnd() {
            return end;
        }

        public void setEnd(LocalDate end) {
            this.end = end;
        }

        public String getWeekNumber() {
            return weekNumber;
        }

        public void setWeekNumber(String weekNumber) {
            this.weekNumber = weekNumber;
        }

        public WeekType getWeekType() {
            return weekType;
        }

        public void setWeekType(WeekType weekType) {
            this.weekType = weekType;
        }

        enum WeekType{
            EVEN, ODD
        }
    }


}
