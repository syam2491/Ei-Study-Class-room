import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class VirtualClassroomManager {
    private static final Logger LOGGER = Logger.getLogger(VirtualClassroomManager.class.getName());
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClassroomService classroomService = new ClassroomService();

    public static void main(String[] args) {
        LOGGER.info("Virtual Classroom Manager started");
        boolean running = true;

        while (running) {
            System.out.println("\nVirtual Classroom Manager");
            System.out.println("1. Add Classroom");
            System.out.println("2. Remove Classroom");
            System.out.println("3. Add Student");
            System.out.println("4. Schedule Assignment");
            System.out.println("5. Submit Assignment");
            System.out.println("6. List Classrooms");
            System.out.println("7. List Students in Classroom");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        addClassroom();
                        break;
                    case 2:
                        removeClassroom();
                        break;
                    case 3:
                        addStudent();
                        break;
                    case 4:
                        scheduleAssignment();
                        break;
                    case 5:
                        submitAssignment();
                        break;
                    case 6:
                        listClassrooms();
                        break;
                    case 7:
                        listStudentsInClassroom();
                        break;
                    case 8:
                        running = false;
                        LOGGER.info("Virtual Classroom Manager stopped");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred", e);
                System.out.println("An error occurred. Please try again.");
            }
        }
    }

    private static void addClassroom() {
        System.out.print("Enter classroom name: ");
        String name = scanner.nextLine();
        try {
            classroomService.addClassroom(name);
            System.out.println("Classroom " + name + " has been created.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeClassroom() {
        System.out.print("Enter classroom name to remove: ");
        String name = scanner.nextLine();
        try {
            classroomService.removeClassroom(name);
            System.out.println("Classroom " + name + " has been removed.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addStudent() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter classroom name: ");
        String className = scanner.nextLine();
        try {
            classroomService.addStudent(studentId, className);
            System.out.println("Student " + studentId + " has been enrolled in " + className);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void scheduleAssignment() {
        System.out.print("Enter classroom name: ");
        String className = scanner.nextLine();
        System.out.print("Enter assignment details: ");
        String assignmentDetails = scanner.nextLine();
        try {
            classroomService.scheduleAssignment(className, assignmentDetails);
            System.out.println("Assignment for " + className + " has been scheduled.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void submitAssignment() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        System.out.print("Enter classroom name: ");
        String className = scanner.nextLine();
        System.out.print("Enter assignment details: ");
        String assignmentDetails = scanner.nextLine();
        try {
            classroomService.submitAssignment(studentId, className, assignmentDetails);
            System.out.println("Assignment submitted by Student " + studentId + " in " + className);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listClassrooms() {
        List<String> classrooms = classroomService.listClassrooms();
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
        } else {
            System.out.println("Classrooms:");
            for (String classroom : classrooms) {
                System.out.println("- " + classroom);
            }
        }
    }

    private static void listStudentsInClassroom() {
        System.out.print("Enter classroom name: ");
        String className = scanner.nextLine();
        try {
            List<String> students = classroomService.listStudentsInClassroom(className);
            if (students.isEmpty()) {
                System.out.println("No students in " + className);
            } else {
                System.out.println("Students in " + className + ":");
                for (String student : students) {
                    System.out.println("- " + student);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}

class Classroom {
    private String name;
    private List<String> students;
    private List<String> assignments;

    public Classroom(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addStudent(String studentId) {
        students.add(studentId);
    }

    public void addAssignment(String assignmentDetails) {
        assignments.add(assignmentDetails);
    }

    public List<String> getStudents() {
        return new ArrayList<>(students);
    }

    public boolean hasAssignment(String assignmentDetails) {
        return assignments.contains(assignmentDetails);
    }
}

class ClassroomService {
    private Map<String, Classroom> classrooms;

    public ClassroomService() {
        this.classrooms = new HashMap<>();
    }

    public void addClassroom(String name) {
        if (classrooms.containsKey(name)) {
            throw new IllegalArgumentException("Classroom already exists");
        }
        classrooms.put(name, new Classroom(name));
    }

    public void removeClassroom(String name) {
        if (!classrooms.containsKey(name)) {
            throw new IllegalArgumentException("Classroom does not exist");
        }
        classrooms.remove(name);
    }

    public void addStudent(String studentId, String className) {
        Classroom classroom = getClassroom(className);
        classroom.addStudent(studentId);
    }

    public void scheduleAssignment(String className, String assignmentDetails) {
        Classroom classroom = getClassroom(className);
        classroom.addAssignment(assignmentDetails);
    }

    public void submitAssignment(String studentId, String className, String assignmentDetails) {
        Classroom classroom = getClassroom(className);
        if (!classroom.getStudents().contains(studentId)) {
            throw new IllegalArgumentException("Student not enrolled in this classroom");
        }
        if (!classroom.hasAssignment(assignmentDetails)) {
            throw new IllegalArgumentException("Assignment not found for this classroom");
        }
    }

    public List<String> listClassrooms() {
        return new ArrayList<>(classrooms.keySet());
    }

    public List<String> listStudentsInClassroom(String className) {
        Classroom classroom = getClassroom(className);
        return classroom.getStudents();
    }

    private Classroom getClassroom(String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            throw new IllegalArgumentException("Classroom not found");
        }
        return classroom;
    }
}
