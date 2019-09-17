import controllers.TaskFactory;
import exceptions.DukeException;
import models.tasks.Deadline;
import models.tasks.Event;
import models.tasks.ToDos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DukeTest {
    @Test
    void testTodoCreation() {
        try {
            assertEquals(new ToDos("write book","2").getDescription(),
                    new TaskFactory().createTask("todo write book /in 2").getDescription());
        } catch (DukeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDeadlineCreation() throws DukeException {
        assertEquals(new Deadline("return book", "Sunday").getDescription(),
                new TaskFactory().createTask("deadline return book /by Sunday").getDescription());
    }

    @Test
    void testEventCreation() throws DukeException {
        assertEquals(new Event("project meeting", "Mon 2-4pm").getDescription(),
                new TaskFactory().createTask("event project meeting /at Mon 2-4pm").getDescription());
    }

    @Test
    void testDeadlineOutput() {
        assertEquals("return book (by: Sunday)",
                new Deadline("return book", "Sunday").getDescription());
    }

    @Test
    void testEventOutput() {
        assertEquals("project meeting (at: Mon 2-4pm)",
                new Event("project meeting", "Mon 2-4pm").getDescription());
    }

    @Test
    void alwaysTrue() {
        assertTrue(true);
    }
}
