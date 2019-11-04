package factorytests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.util.ArrayList;
import models.task.ITask;
import models.task.NullTask;
import models.task.Task;
import models.task.TaskState;
import org.junit.jupiter.api.Test;
import util.date.DateTimeHelper;
import util.factories.TaskFactory;

//@@author Lucria
class TaskFactoryTests {
    private TaskFactory taskFactory;
    private DateTimeHelper dateTimeHelper;
    private String simulatedFactoryInput;

    TaskFactoryTests() {
        this.taskFactory = new TaskFactory();
        this.dateTimeHelper = new DateTimeHelper();
    }

    /**
     * Always true test just to test if Junit is working.
     */
    @Test
    void alwaysTrue() {
        assertEquals(2, 2);
    }

    @Test
    void taskCreation_fullCorrectInputs_creationSuccess() {
        simulatedFactoryInput = "-t Killing Thanos -p 1 -d 28/09/2019 -c 100 -s done -r Use Iron Man";
        try {
            ITask simulatedTask = taskFactory.create(simulatedFactoryInput);
            ArrayList<String> expectedRequirements = new ArrayList<>();
            expectedRequirements.add("Use Iron Man");
            Task expectedTask = new Task("Killing Thanos", 1,
                    dateTimeHelper.formatDate("28/09/2019"), 100, TaskState.DONE, expectedRequirements);
            assertEquals(expectedTask.getDetails(), simulatedTask.getDetails());
            assertEquals(expectedTask.getTaskName(),simulatedTask.getTaskName());
            assertEquals(expectedTask.getTaskCredit(), simulatedTask.getTaskCredit());
            assertEquals(expectedTask.getTaskPriority(), simulatedTask.getTaskPriority());
        } catch (ParseException err) {
            fail("ParseException was detected when it shouldn't have");
        }
    }

    @Test
    void taskCreation_correctPartialInputs_creationSuccess() {
        simulatedFactoryInput = "-t Turning back time -p 5 -c 5";
        ITask simulatedTask = taskFactory.create(simulatedFactoryInput);
        Task expectedTask = new Task("Turning back time", 5,
                null, 5, TaskState.OPEN, null);
        assertEquals(expectedTask.getDetails(), simulatedTask.getDetails());
        assertEquals(expectedTask.getTaskName(),simulatedTask.getTaskName());
        assertEquals(expectedTask.getTaskCredit(), simulatedTask.getTaskCredit());
        assertEquals(expectedTask.getTaskPriority(), simulatedTask.getTaskPriority());
    }

    @Test
    void taskCreation_wrongInputs_creationFailed() {
        simulatedFactoryInput = "-t Missing priority -c 5";
        ITask simulatedTask = taskFactory.create(simulatedFactoryInput);
        NullTask expectedTask = new NullTask();
        assertEquals(expectedTask.getDetails(), simulatedTask.getDetails());
        assertEquals(expectedTask.getTaskName(),simulatedTask.getTaskName());
        assertEquals(expectedTask.getTaskCredit(), simulatedTask.getTaskCredit());
        assertEquals(expectedTask.getTaskPriority(), simulatedTask.getTaskPriority());
        assertEquals(expectedTask.getDueDate(), simulatedTask.getDueDate());
        assertEquals(expectedTask.getTaskState(), simulatedTask.getTaskState());
        assertEquals(expectedTask.getTaskRequirements(), simulatedTask.getTaskRequirements());
    }
}
