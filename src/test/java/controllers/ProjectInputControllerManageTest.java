package controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ProjectRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectInputControllerManageTest {
    private ProjectInputController projectInputController;
    private ProjectRepository projectRepository;
    private String simulatedUserInput;
    private String[] simulatedOutput;
    private String[] expectedOutput;

    public ProjectInputControllerManageTest() {
        this.projectRepository = new ProjectRepository();
        this.projectInputController = new ProjectInputController(this.projectRepository);
    }

    @Test
    void alwaysTrue() {
        assertEquals(2, 2);
    }

    @Test
    void onCommandReceived_noProjectIndex_executionFail() {
        simulatedUserInput = "test";
        simulatedOutput = projectInputController.onCommandReceived(simulatedUserInput);
        expectedOutput = new String[] {"Input is not a number! Please input a proper project index!"};
        assertArrayEquals(expectedOutput, simulatedOutput);
    }

    @BeforeEach
    void initializeProjectInputController() {
        while (projectRepository.getAll().size() != 0) {
            projectRepository.deleteItem(1);
        }
        projectRepository.addToRepo("create Avengers Testing");
        simulatedUserInput = "1";
        simulatedOutput = projectInputController.onCommandReceived(simulatedUserInput);
        expectedOutput = new String[] {"Please enter a new command:"};
        assertArrayEquals(expectedOutput, simulatedOutput);
    }

    @Test
    void onCommandReceived_exitsProject_executionSuccess() {
        simulatedUserInput = "bye";
        simulatedOutput = projectInputController.manageProject(simulatedUserInput);
        expectedOutput = new String[] {"Bye. Hope to see you again soon!"};
        assertArrayEquals(expectedOutput, simulatedOutput);
    }

    @Test
    void manageProject_addMember_executionSuccess() {
        simulatedUserInput = "add member -n Thor";
        simulatedOutput = projectInputController.manageProject(simulatedUserInput);
        expectedOutput = new String[] {"Added new member to: Avengers Testing",
                                       "Member details 1. Thor (Phone: -- | Email: -- | Role: member)"};
        assertArrayEquals(expectedOutput, simulatedOutput);
    }
}