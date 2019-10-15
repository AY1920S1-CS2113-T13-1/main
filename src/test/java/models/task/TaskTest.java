package models.task;

import org.junit.jupiter.api.Test;
import util.factories.TaskFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    private TaskFactory consoleInputFactory = new TaskFactory();

    @Test
    public void alwaysTrue() {
        assertEquals(2, 2);
    }

    @Test
    public void testGetDetails() {
        ArrayList<String> taskRequirements = new ArrayList<>();
        Task task1 = new Task("task1", 5, null,100, TaskState.OPEN,taskRequirements);
        assertEquals("task1 | Priority: 5 | Due: -- | Credit: 100 | State: OPEN",task1.getDetails());
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
            Date date = simpleDateFormat.parse("20/1/2019");
            Task task2 = new Task("task2", 5, date, 100, TaskState.OPEN, taskRequirements);
            assertEquals("task2 | Priority: 5 | Due: 20 Jan 2019 | Credit: 100 | State: OPEN", task2.getDetails());
        } catch (ParseException e) {
            System.out.println("Parsing error");
        }
    }

    @Test
    public void testGetTaskRequirements() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dueDate = formatter.parse("19/10/2019");
            ArrayList<String> taskRequirements = new ArrayList<>();
            taskRequirements.add("requirement1");
            Task dummyTask = new Task("task1",1,dueDate,10, TaskState.TODO, taskRequirements);
            Task task = consoleInputFactory.createTask("t/task1 p/1 d/19/10/2019 c/10 s/todo r/requirement1");
            assertEquals(dummyTask.getDetails(),task.getDetails());
            assertEquals(dummyTask.getTaskRequirements(), task.getTaskRequirements());

            ArrayList<String> taskRequirements2 = new ArrayList<>();
            taskRequirements2.add("requirement1");
            taskRequirements2.add("requirement2");
            Task dummyTask2 = new Task("task2",2,null,10, TaskState.OPEN, taskRequirements2);
            Task task2 = consoleInputFactory.createTask("t/task2 p/2 c/10 r/requirement1 r/requirement2");
            assertEquals(dummyTask2.getDetails(),task2.getDetails());
            assertEquals(dummyTask2.getTaskRequirements(), task2.getTaskRequirements());
        } catch (ParseException e) {
            System.out.println("Parsing error");
        }
    }
}