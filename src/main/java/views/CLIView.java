package views;

import controllers.ConsoleInputController;
import controllers.ProjectInputController;
import repositories.ProjectRepository;
import util.log.DukeLogger;

import java.util.Scanner;

public class CLIView {
    private static final String HORILINE = "\t____________________________________________________________";
    private static final String INDENTATION = "\t";

    private ConsoleInputController consoleInputController;
    private ProjectRepository projectRepository;

    public CLIView() {
        this.projectRepository = new ProjectRepository();
        this.consoleInputController = new ConsoleInputController(projectRepository);
    }

    /**
     * Method to call when View model is started.
     */
    public void start() {
        DukeLogger.logInfo(CLIView.class, "ArchDuke have started.");
        Scanner sc = new Scanner(System.in);
        boolean isDukeRunning = true;
        consolePrint("Hello! I'm Duke", "What can I do for you?");
        //noinspection InfiniteLoopStatement
        while (isDukeRunning) {
            String commandInput = sc.nextLine();
            String[] outputMessage = consoleInputController.onCommandReceived(commandInput);
            if (outputMessage[0].matches("Now managing.*")) {
                consolePrint(outputMessage);

                ProjectInputController projectInputController = new ProjectInputController(projectRepository);
                String projectNumber = consoleInputController.getManagingProjectIndex();

                while (projectInputController.getIsManagingAProject()) {
                    String[] projectOutputMessage = projectInputController.onCommandReceived(projectNumber);
                    consolePrint(projectOutputMessage);
                    if (projectOutputMessage[0].matches("Bye.*")) {
                        isDukeRunning = false;
                        break;
                    }
                }
            } else if (outputMessage[0].matches("Bye.*")) {
                isDukeRunning = false;
            } else {
                consolePrint(outputMessage);
            }
        }
        System.exit(0);
    }

    /**
     * Prints an indented and formatted message with a top and bottom border.
     * @param lines The lines to be printed in between the border.
     */
    public void consolePrint(String... lines) {
        System.out.println(HORILINE);
        for (String message : lines) {
            System.out.println(INDENTATION + message);
        }
        System.out.println(HORILINE);
    }
}
