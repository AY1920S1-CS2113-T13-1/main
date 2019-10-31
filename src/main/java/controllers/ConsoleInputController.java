package controllers;

import repositories.ProjectRepository;
import util.CommandHelper;
import util.ViewHelper;
import util.log.ArchDukeLogger;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleInputController implements IController {

    private ProjectRepository projectRepository;
    private String managingProjectIndex;
    private ViewHelper viewHelper;
    private CommandHelper commandHelper;

    /**
     * Constructor.
     * @param projectRepository : takes in a projectRepository.
     */
    public ConsoleInputController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.managingProjectIndex = "";
        this.viewHelper = new ViewHelper();
        this.commandHelper = new CommandHelper();
    }

    /**
     * Method that is called upon receiving commands from CLI.
     * @param input : Input typed by user into CLI
     */
    @Override
    public String[] onCommandReceived(String input) {
        ArchDukeLogger.logInfo(ConsoleInputController.class.getName(), "User input: '" + input + "'");
        Scanner inputReader = new Scanner(input);
        String command = inputReader.next();

        switch (command) {
        case "bye":
            return end();
        case "create":
            return commandCreate(input);
        case "list":
            return commandList();
        case "manage":
            return commandManage(inputReader);
        case "delete":
            return commandDelete(inputReader);
        case "help":
            return commandHelp();
        default:
            return new String[] {"Invalid inputs. Please refer to User Guide or type help!"};
        }
    }

    /**
     * Creates a new project with a given name and a number of numbers.
     * @param input To read the input from the user.
     */
    private String[] commandCreate(String input) {
        ArchDukeLogger.logDebug(ConsoleInputController.class.getName(), "[commandCreate] User input: '" + input + "'");
        boolean isProjectCreated = projectRepository.addToRepo(input);
        if (!isProjectCreated) {
            return new String[] {"Creation of Project failed. Please check parameters given!"};
        } else {
            return new String[] {"Project created!"};
        }
    }

    /**
     * Method called when users wishes to view all Projects
     * that are currently created or stored.
     */
    private String[] commandList() {
        ArchDukeLogger.logDebug(ConsoleInputController.class.getName(), "[commandList]");
        ArrayList<ArrayList<String>> allProjectsDetails = projectRepository.getAllProjectsDetailsForTable();
        if (allProjectsDetails.size() == 0) {
            return new String[] {"You currently have no projects!"};
        } else {
            System.out.println("Here are all the Projects you are managing:"); // Need to change this out.
            return viewHelper.consolePrintTable(allProjectsDetails);
        }
    }

    /**
     * Manage the project.
     * @param inputReader To read the input from the user.
     */
    private String[] commandManage(Scanner inputReader) {
        ArchDukeLogger.logDebug(ConsoleInputController.class.getName(), "[commandManage] User input: " + inputReader);
        if (inputReader.hasNext()) {
            this.managingProjectIndex = inputReader.next();
            try {
                ArchDukeLogger.logInfo(ConsoleInputController.class.getName(), "Managing project: "
                        + projectRepository.getItem(Integer.parseInt(managingProjectIndex)).getName());
                return new String[] {"Now managing "
                        + projectRepository.getItem(Integer.parseInt(managingProjectIndex)).getName()};
            } catch (IndexOutOfBoundsException err) {
                return new String[] {"Please enter the correct index of an existing Project!"};
            }
        } else {
            return new String[] {"Please enter a project number!"};
        }
    }


    /**
     * Deletes a project.
     * @param inputReader To read the input from the user.
     */
    private String[] commandDelete(Scanner inputReader) {
        ArchDukeLogger.logDebug(ConsoleInputController.class.getName(), "[commandDelete] User input: " + inputReader);
        if (inputReader.hasNext()) {
            int projectIndex = Integer.parseInt(inputReader.next());
            boolean isProjectDeleted = this.projectRepository.deleteItem(projectIndex);
            if (isProjectDeleted) {
                return new String[] {"Project " + projectIndex + " has been deleted"};
            } else {
                return new String[] {"Index out of bounds! Please check project index!"};
            }
        } else {
            return new String[] {"Please enter a project number to delete"};
        }
    }

    /**
     * Displays the set of the commands which can be used.
     */
    private String[] commandHelp() {
        ArrayList<ArrayList<String>> toPrintAll = new ArrayList<>();
        toPrintAll.add(commandHelper.getCommandsForConsole());

        return viewHelper.consolePrintTable(toPrintAll);
    }

    /**
     * Method to be called when user says bye to exit the program.
     */
    public String[] end() {
        ArchDukeLogger.logInfo(ConsoleInputController.class.getName(), "ArchDuke have stopped.");
        return new String[] { "Bye. Hope to see you again soon!" };
    }

    public String getManagingProjectIndex() {
        return managingProjectIndex;
    }


}
