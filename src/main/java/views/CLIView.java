package views;

import controllers.ConsoleInputController;
import models.data.IProject;
import models.member.Member;
import models.task.Task;
import models.temp.tasks.ITask;
import models.temp.tasks.TaskList;
import java.util.ArrayList;
import java.util.Scanner;

public class CLIView {
    private static final String horiLine = "\t____________________________________________________________";
    private static final String indentation = "\t";

    private ConsoleInputController consoleInputController;

    public CLIView() {
        this.consoleInputController = new ConsoleInputController(this);
    }

    /**
     * Prints an indented and formatted message with a top and bottom border.
     * @param lines The lines to be printed in between the border.
     */
    public void consolePrint(String... lines) {
        System.out.println(horiLine);
        for (String message : lines) {
            System.out.println(indentation + message);
        }
        System.out.println(horiLine);
    }

    /**
     * Method to call when View model is started.
     */
    public void start() {
        Scanner sc = new Scanner(System.in);
        consoleInputController.readData();

        consolePrint("Hello! I'm Duke", "What can I do for you?");

        while (true) {
            String command = sc.nextLine();
            consoleInputController.onCommandReceived(command);
        }
    }

    /**
     * Method to be called when user says bye to exit the program.
     */
    public void end() {
        consolePrint("Bye. Hope to see you again soon!");
        System.exit(0);
    }

    /**
     * Method to be called when user prompts for a task to be marked as done.
     * @param taskList : list of tasks.
     * @param input : Input containing task numbers to be marked as done by user.
     */
    public void markDone(TaskList taskList, String input) {
        String[] allInputs = input.split(" ");
        ArrayList<String> toPrint = new ArrayList<>();
        toPrint.add("Nice! I've marked the following task(s) as done:");
        for (String i : allInputs) {
            if (!("done").equals(i)) {
                int index = Integer.parseInt(i) - 1;
                ITask chosenToDos = taskList.getTask(index);
                chosenToDos.markAsDone();
                toPrint.add(chosenToDos.getFullDescription());
            }
        }
        consolePrint(toPrint.toArray(new String[0]));
    }

    /**
     * Method to be called when a Invalid Command is input by the user.
     * @param newException : Exception that is thrown when an Invalid Command is detected
     */
    public void invalidCommandMessage(Exception newException) {
        consolePrint(newException.getMessage());
    }

    /**
     * Method that is called when user wishes to delete a task.
     * This method is responsible for handling printing of horizontal lines.
     * @param taskList : List of tasks from which the chosen task should be deleted from.
     * @param input : Input containing task numbers to delete as given by the user.
     */
    public void deleteTask(TaskList taskList, String input) {
        ArrayList<String> toPrint = new ArrayList<>();
        toPrint.add("Noted. I've removed the following task(s):");
        String[] allInputs = input.split(" ");
        for (String i : allInputs) {
            if (!("delete").equals(i)) {
                int index = Integer.parseInt(i) - 1;
                ITask chosenTask = taskList.getTask(index);
                toPrint.add(chosenTask.getFullDescription());
                taskList.deleteFromList(chosenTask);
            }
        }
        String grammarTasks = taskList.getNumOfTasks() == 1 ? "tasks" : "task";
        toPrint.add("Now you have " + taskList.getNumOfTasks() + " " + grammarTasks + " in the list.");
        consolePrint(toPrint.toArray(new String[0]));
    }

    /**
     * Method that is called when user wishes to find a specific task.
     * This method updates the UI (in this case CLI) with relevant print messages and information.
     * @param taskList : Current list of tasks. Users will enter a keyword to search for a task residing in this list.
     * @param input : Full command that user has keyed into CLI.
     */
    public void findTask(TaskList taskList, String input) {
        ArrayList<String> toPrint = new ArrayList<>();
        toPrint.add("Here are the matching tasks in your list:");
        ArrayList<ITask> results = taskList.getSearchedTasks(input);
        for (int i = 0; i < results.size(); i++) {
            toPrint.add("" + (i + 1) + "." + results.get(i).getFullDescription());
        }
        consolePrint(toPrint.toArray(new String[0]));
    }

    /**
     * Method called when users wishes to view all Projects that are currently created or stored.
     * @param allProjects List of Projects returned to View model by the Controller from the Repository
     */
    public void viewAllProjects(ArrayList<IProject> allProjects) {
        ArrayList<String> toPrint = new ArrayList<>();
        toPrint.add("Here are all the Projects you are managing:");
        for (int i = 0; i < allProjects.size(); i++) {
            toPrint.add("" + (i + 1) + ". " + allProjects.get(i).getDescription() + " "
                + allProjects.get(i).getMembers());
        }
        consolePrint(toPrint.toArray(new String[0]));
    }

    /**
     * Method called to inform the user that the project has exited.
     * @param projectName The name of the project exited.
     */
    public void exitProject(String projectName) {
        consolePrint("Exited project: " + projectName);
    }

    /**
     * Adds a member to the project.
     * @param projectToManage The project specified by the user.
     * @param newMember A new member with details specified by the user.
     */
    public void addMember(IProject projectToManage, Member newMember) {
        projectToManage.addMember(newMember);
        consolePrint("Added new member to: " + projectToManage.getDescription(), ""
            + "Member details: " + newMember.getDetails());
    }

    public void editMember(IProject projectToManage, int memberIndexNumber, String memberDetails) {
        projectToManage.editMember(memberIndexNumber, memberDetails);
        consolePrint("Updated member details with the index number " + memberIndexNumber);
    }

    public void removeMember(IProject projectToManage, int memberIndexNumber) {
        projectToManage.removeMember(memberIndexNumber);
        consolePrint("Removed member with the index number " + memberIndexNumber);
    }

    /**
     * Adds a member to the project.
     * @param projectToManage The project specified by the user.
     * @param newTask A new task with details specified by the user.
     */
    public void addTask(IProject projectToManage, Task newTask) {
        projectToManage.addTask(newTask);
        consolePrint("Added new task to the list.");
    }

    /**
     * Shows the details of all the members in the project.
     * Can be updated later on to include more information (tasks etc).
     * @param projectToManage The project specified by the user.
     */
    public void viewAllMembers(IProject projectToManage) {
        ArrayList<String> allMemberDetails = projectToManage.getMembers().getAllMemberDetails();
        consolePrint(allMemberDetails.toArray(new String[0]));
    }

    /**
     * Shows the details of all the task in the project.
     * @param projectToManage The project specified by the user.
     */
    public void viewAllTasks(IProject projectToManage) {
        ArrayList<String> allTaskDetails = projectToManage.getTasks().getAllTaskDetails();
        consolePrint(allTaskDetails.toArray(new String[0]));
    }

    /**
     * Assigns or unassigns a particular task to members in a project.
     * @param assign ArrayList containing index number of members to be assigned task.
     * @param unassign ArrayList containing index number of members to be unassigned task.
     * @param task Task to assign or unassign to members.
     * @param projectToManage The project in which the aforementioned task belongs to.
     */
    public void assignOrUnassignTask(ArrayList<Integer> assign, ArrayList<Integer> unassign,
        Task task, IProject projectToManage) {
        if (assign.size() > 0) {
            for (Integer i : assign) {
                Member memberToAssign = projectToManage.getMembers().getMember(i);
                task.assignMember(memberToAssign);
                memberToAssign.assignTask(task);
                consolePrint("Assigned task to: " + memberToAssign.getName());
            }
        }
        if (unassign.size() > 0) {
            for (Integer i : unassign) {
                task.removeMember(i);
                consolePrint("Unassigned task to: "
                    + projectToManage.getMembers().getMember(i).getName());
                projectToManage.getMembers().getMember(i).unassignTask(task);
                //recalculate credits for other members assigned to task if necessary
            }
        }
    }

    public void viewTaskRequirements(IProject projectToManage, int taskIndex) {
        ArrayList<String> taskRequirements = projectToManage.getTask(taskIndex).getTaskRequirements();
        consolePrint(taskRequirements.toArray(new String[0]));
    }
}
