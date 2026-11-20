package com.studysync.config;

import com.studysync.model.*;
import com.studysync.repository.*;
import com.studysync.util.PasswordHasher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Runs once when the app starts. If the tables are empty it adds sample courses,
 * 4 demo students (password: demo123), one demo group and two sessions,
 * so the app has data to show in the demo.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final GroupRepository groupRepository;
    private final SessionRepository sessionRepository;

    public DataSeeder(CourseRepository courseRepository, StudentRepository studentRepository,
                      AvailabilityRepository availabilityRepository, GroupRepository groupRepository,
                      SessionRepository sessionRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.availabilityRepository = availabilityRepository;
        this.groupRepository = groupRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void run(String... args) {
        if (courseRepository.count() == 0) {
            courseRepository.saveAll(List.of(
                    new Course("SE-301", "Software Construction and Development", "Software Engineering"),
                    new Course("CS-302", "Computer Networks", "Computer Science"),
                    new Course("CS-303", "Database Systems", "Computer Science"),
                    new Course("CS-304", "Operating Systems", "Computer Science"),
                    new Course("SE-305", "Software Requirements Engineering", "Software Engineering"),
                    new Course("SE-306", "Software Quality Engineering", "Software Engineering"),
                    new Course("CS-307", "Artificial Intelligence", "Computer Science"),
                    new Course("CS-308", "Web Engineering", "Computer Science")));
            System.out.println("[DataSeeder] Sample courses added");
        }

        if (studentRepository.count() == 0) {
            Course scd = courseRepository.findByCode("SE-301").orElseThrow();
            Course cn = courseRepository.findByCode("CS-302").orElseThrow();
            Course db = courseRepository.findByCode("CS-303").orElseThrow();

            Student ali = demoStudent("Ali Khan", "ali@demo.com", 3.4, StudyStyle.DISCUSSION_PHYSICAL, scd, cn);
            Student sara = demoStudent("Sara Ahmed", "sara@demo.com", 3.6, StudyStyle.DISCUSSION_PHYSICAL, scd, db);
            Student hamza = demoStudent("Hamza Raza", "hamza@demo.com", 2.9, StudyStyle.QUIET_ONLINE, scd, cn);
            Student fatima = demoStudent("Fatima Noor", "fatima@demo.com", 3.5, StudyStyle.DISCUSSION_ONLINE, scd, cn);

            slot(ali, DayOfWeek.MONDAY, 14, 17);
            slot(ali, DayOfWeek.WEDNESDAY, 10, 12);
            slot(sara, DayOfWeek.MONDAY, 15, 18);
            slot(sara, DayOfWeek.THURSDAY, 9, 11);
            slot(hamza, DayOfWeek.WEDNESDAY, 11, 13);
            slot(hamza, DayOfWeek.FRIDAY, 16, 18);
            slot(fatima, DayOfWeek.MONDAY, 14, 16);
            slot(fatima, DayOfWeek.FRIDAY, 17, 19);

            StudyGroup group = new StudyGroup("SCD Final Prep", "Revising design patterns and UML before the final.",
                    scd, ali, 6);
            group.getMembers().add(ali);
            group.getMembers().add(sara);
            group = groupRepository.save(group);

            PhysicalSession past = new PhysicalSession("Library Room 2");
            past.setTitle("Design Patterns Revision");
            past.setGroup(group);
            past.setCreatedBy(ali);
            past.setStartTime(LocalDate.now().minusDays(2).atTime(14, 0));
            past.setDurationMinutes(90);
            past.setReminderSent(true);
            sessionRepository.save(past);

            OnlineSession upcoming = new OnlineSession("https://meet.google.com/abc-defg-hij");
            upcoming.setTitle("UML Diagrams Practice");
            upcoming.setGroup(group);
            upcoming.setCreatedBy(sara);
            upcoming.setStartTime(LocalDate.now().plusDays(3).atTime(15, 0));
            upcoming.setDurationMinutes(60);
            sessionRepository.save(upcoming);

            System.out.println("[DataSeeder] Demo students added (password: demo123)");
        }
    }

    private Student demoStudent(String name, String email, double cgpa, StudyStyle style, Course... courses) {
        Student student = new Student(name, email, PasswordHasher.hash("demo123"));
        student.setSemester(6);
        student.setDepartment("Software Engineering");
        student.setCgpa(cgpa);
        student.setStudyStyle(style);
        student.getCourses().addAll(List.of(courses));
        return studentRepository.save(student);
    }

    private void slot(Student student, DayOfWeek day, int fromHour, int toHour) {
        availabilityRepository.save(new AvailabilitySlot(student, day, LocalTime.of(fromHour, 0), LocalTime.of(toHour, 0)));
    }
}