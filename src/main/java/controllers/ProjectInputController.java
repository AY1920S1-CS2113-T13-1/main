package controllers;

import models.member.IMember;
import models.member.Member;
import models.project.IProject;
import models.project.Project;
import models.reminder.IReminder;
import models.reminder.Reminder;
import models.task.ITask;
import models.task.Task;
import repositories.ProjectRepository;
import util.AssignmentViewHelper;
import util.CommandHelper;
import util.ParserHelper;
import util.ViewHelper;
import util.factories.MemberFactory;
import util.factories.ReminderFactory;
import util.factories.TaskFactory;
import util.log.DukeLogger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ProjectInputController implements IController {
    private Scanner manageProjectInput;
    private ProjectRepository projectRepository;
    private MemberFactory memberFactory;
    private TaskFactory taskFactory;
    private boolean isManagingAProject;
    private ViewHelper viewHelper;
    private CommandHelper commandHelper;

    /**
     * Constructor for ProjectInputController takes in a View model and a ProjectRepository.
     * ProjectInputController is responsible for handling user input when user chooses to manage a project.
     * @param projectRepository The object holding all projects.
     */
    public ProjectInputController(ProjectRepository projectRepository) {
        this.manageProjectInput = new Scanner(System.in);
        this.projectRepository = projectRepository;
        this.memberFactory = new MemberFactory();
        this.taskFactory = new TaskFactory();
        this.isManagingAProject = true;
        this.viewHelper = new ViewHelper();
        this.commandHelper = new CommandHelper();
    }

    /**
     * Allows the user to manage the project by branching into the project of their choice.
     * @param input User input containing project index number (to add to project class).
     */
    @Override
    public String[] onCommandReceived(String input) {
        //DukeLogger.logInfo(ProjectInputController.class, "Managing project: " + input);
        int projectNumber;
        try {
            projectNumber = Integer.parseInt(input);
        } catch (NumberFormatException err) {
            isManagingAProject = false;
            return new String[] {"Input is not a number! Please input a proper project index!"};
        }
        Project projectToManage = projectRepository.getItem(projectNumber);
        isManagingAProject = true;
        return manageProject(projectToManage);
    }

    /**
     * Manages the project.
     * @param projectToManage The project specified by the user.
     * @return Boolean variable giving status of whether the exit command is entered.
     */
    private String[] manageProject(Project projectToManage) {
        String[] responseToView = {"Please enter a command."};
        if (manageProjectInput.hasNextLine()) {
            String projectFullCommand = manageProjectInput.nextLine();
            DukeLogger.logInfo(ProjectInputController.class, "Managing:"
                    + projectToManage.getName() + ",input:'"
                    + projectFullCommand + "'");
            if (projectFullCommand.matches("exit")) {
                isManagingAProject = false;
                responseToView = projectExit(projectToManage);
            } else if (projectFullCommand.matches("add member.*")) {
                responseToView =  projectAddMember(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("edit member.*")) {
                responseToView = projectEditMember(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("delete member.*")) {
                responseToView = projectDeleteMember(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("view members.*")) {
                responseToView = projectViewMembers(projectToManage);
            } else if (projectFullCommand.matches("role.*")) {
                responseToView = projectRoleMembers(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("view credits.*")) {
                responseToView = projectViewCredits(projectToManage);
            } else if (projectFullCommand.matches("add task.*")) {
                responseToView = projectAddTask(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("view tasks.*")) {
                responseToView = projectViewTasks(projectToManage, projectFullCommand);
            } else if (projectFullCommand.matches("view assignments.*")) {
                responseToView = projectViewAssignments(projectToManage, projectFullCommand);
            } else if (projectFullCommand.matches("view task requirements.*")) { // need to refactor this
                responseToView = projectViewTaskRequirements(projectToManage, projectFullCommand);
            } else if (projectFullCommand.matches("edit task requirements.*")) {
                responseToView = projectEditTaskRequirements(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("edit task.*")) {
                responseToView = projectEditTask(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("delete task.*")) {
                responseToView = projectDeleteTask(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("assign task.*")) {
                responseToView = projectAssignTask(projectToManage, projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("add reminder.*")) {
                responseToView = projectAddReminder(projectToManage,projectFullCommand);
                // jsonConverter.saveProject(projectToManage);
            } else if (projectFullCommand.matches("help")) {
                responseToView = projectHelp();
            } else if (projectFullCommand.matches("bye")) {
                return end();
            } else {
                return new String[] {"Invalid command. Try again!"};
            }
        }
        return responseToView;
    }

    private String[] projectHelp() {
        ArrayList<ArrayList<String>> toPrintAll = new ArrayList<>();
        toPrintAll.add(commandHelper.getCommandsForProject());
        return viewHelper.consolePrintTable(toPrintAll);
    }

    /**
     * Adds roles to Members in a Project.
     * @param projectToManage : The project specified by the user.
     * @param projectFullCommand : User input.
     */
    public String[] projectRoleMembers(Project projectToManage, String projectFullCommand) {
        String parsedCommands = projectFullCommand.substring(5);
        String[] commandOptions = parsedCommands.split(" -n ");
        if (commandOptions.length != 2) {
            return new String[] {"Wrong command format! Please enter role INDEX -n ROLE_NAME"};
        }
        int memberIndex = Integer.parseInt(commandOptions[0]);
        IMember selectedMember = projectToManage.getMembers().getMember(memberIndex);
        selectedMember.setRole(commandOptions[1]);
        return new String[] {"Successfully changed the role of " + selectedMember.getName() + " to "
                                + selectedMember.getRole() + "."};
    }

    /**
     * Adds a member to the current project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectAddMember(Project projectToManage, String projectCommand) {
        if (projectCommand.length() < 11) {
            return new String[] {"Add member command minimum usage must be \"add member -n NAME\"!",
                                 "Please refer to user guide for additional details."};
        }
        String memberDetails = projectCommand.substring(11);
        int numberOfCurrentMembers = projectToManage.getNumOfMembers();
        memberDetails = memberDetails + " -x " + numberOfCurrentMembers;
        IMember newMember = memberFactory.create(memberDetails);
        if (newMember.getName() != null) {
            projectToManage.addMember((Member) newMember);
            return new String[] {"Added new member to: " + projectToManage.getName(), ""
                    + "Member details " + newMember.getDetails()};
        } else {
            return new String[] {newMember.getDetails()};
        }
    }

    /**
     * Updates the details of a given member in the current project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectEditMember(Project projectToManage, String projectCommand) {
        try {
            int memberIndexNumber = Integer.parseInt(projectCommand.substring(12).split(" ")[0]);
            if (projectToManage.getNumOfMembers() >= memberIndexNumber && memberIndexNumber > 0) {
                String updatedMemberDetails = projectCommand.substring(projectCommand.indexOf("-"));
                projectToManage.editMember(memberIndexNumber,updatedMemberDetails);
                return new String[] { "Updated member details with the index number " + memberIndexNumber};
            } else {
                return new String[] {"The member index entered is invalid."};
            }
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return new String[] {"Please enter the updated member details format correctly."};
        }
    }

    /**
     * Deletes a member from the current project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectDeleteMember(Project projectToManage, String projectCommand) {
        try {
            int memberIndexNumber = Integer.parseInt(projectCommand.substring(14).split(" ")[0]);
            if (projectToManage.getNumOfMembers() >= memberIndexNumber) {
                Member memberToRemove = projectToManage.getMembers().getMember(memberIndexNumber);
                projectToManage.removeMember(memberToRemove);
                return new String[]{"Removed member with the index number " + memberIndexNumber};
            } else {
                return new String[]{"The member index entered is invalid."};
            }
        } catch (IndexOutOfBoundsException e) {
            return new String[] {"Please enter the index number of the member to be deleted correctly."};
        }
    }

    /**
     * Displays all the members in the current project.
     * Can be updated later on to include more information (tasks etc).
     * @param projectToManage The project specified by the user.
     */
    public String[] projectViewMembers(Project projectToManage) {
        ArrayList<String> allMemberDetailsForTable = projectToManage.getMembers().getAllMemberDetailsForTable();
        String header = "Members of " + projectToManage.getName() + ":";
        allMemberDetailsForTable.add(0, header);
        DukeLogger.logDebug(ProjectInputController.class, allMemberDetailsForTable.toString());
        ArrayList<ArrayList<String>> tablesToPrint = new ArrayList<>();
        tablesToPrint.add(allMemberDetailsForTable);
        return viewHelper.consolePrintTable(tablesToPrint);
    }

    /**
     * Displays the members’ credits, their index number, name, and name of tasks completed.
     * @param projectToManage The project specified by the user.
     */
    public String[] projectViewCredits(IProject projectToManage) {
        ArrayList<String> allCredits = projectToManage.getCredits();
        DukeLogger.logDebug(ProjectInputController.class, allCredits.toString());
        if (allCredits.isEmpty()) {
            allCredits.add(0, "There are no members in this project.");
        } else {
            allCredits.add(0, "Here are all the member credits: ");
        }
        return allCredits.toArray(new String[0]);
    }


    /**
     * Adds a task to the current project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectAddTask(Project projectToManage, String projectCommand) {
        try {
            ITask newTask = taskFactory.createTask(projectCommand.substring(9));
            if (newTask.getDetails() != null) {
                projectToManage.addTask((Task) newTask);
                return new String[] {"Added new task to the list."};
            }
            return new String[] {"Failed to create new task. Please ensure all "
                        + "necessary parameters are given"};

        } catch (NumberFormatException | ParseException e) {
            return new String[] {"Please enter your task format correctly."};
        }
    }

    /**
     * Updates the task details of a given task in the project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectEditTask(Project projectToManage, String projectCommand) {
        try {
            int taskIndexNumber = Integer.parseInt(projectCommand.substring(10).split(" ")[0]);
            String updatedTaskDetails = projectCommand.substring(projectCommand.indexOf("-"));

            if (projectToManage.getNumOfTasks() >= taskIndexNumber && taskIndexNumber > 0) {
                projectToManage.editTask(taskIndexNumber, updatedTaskDetails);
                return new String[] { "The task has been updated!" };
            }
            return new String[] {"The task index entered is invalid."};

        } catch (NumberFormatException e) {
            return new String[] {"Please enter your task format correctly."};
        }
    }

    /**
     * Deletes a task from the project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectDeleteTask(Project projectToManage, String projectCommand) {
        int taskIndexNumber = Integer.parseInt(projectCommand.substring(12).split(" ")[0]);
        if (projectToManage.getNumOfTasks() >= taskIndexNumber) {
            String removedTaskString = "Removed " + projectToManage.getTask(taskIndexNumber).getTaskName();
            projectToManage.removeTask(taskIndexNumber);
            return new String[] {removedTaskString};
        } else {
            return new String[] {"The task index entered is invalid."};
        }
    }

    /**
     * Updates the task requirements of a given task in the project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectEditTaskRequirements(Project projectToManage, String projectCommand) {
        try {
            int taskIndexNumber = Integer.parseInt(projectCommand.substring(23).split(" ")[0]);
            String updatedTaskRequirements = projectCommand.substring(projectCommand.indexOf("-"));
            if (projectToManage.getNumOfTasks() >= taskIndexNumber && taskIndexNumber > 0) {
                projectToManage.editTaskRequirements(taskIndexNumber,updatedTaskRequirements);
                return new String[] {"The requirements of your specified task has been updated!"};
            }
            return new String[] {"The task index entered is invalid."};
        } catch (NumberFormatException e) {
            return new String[] {"Task index is missing! Please input a proper task index!"};
        }
    }

    /**
     * Displays the tasks in the current project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectViewTaskRequirements(Project projectToManage, String projectCommand) {
        if (projectCommand.length() < 23) {
            return new String[] {"Please indicate the index of the task to be viewed."};
        } else {
            try {
                int taskIndex = Integer.parseInt(projectCommand.substring(23));
                if (projectToManage.getNumOfTasks() >= taskIndex && taskIndex > 0) {
                    if (projectToManage.getTask(taskIndex).getNumOfTaskRequirements() == 0) {
                        return new String[] {"This task has no specific requirements."};
                    } else {
                        ArrayList<String> taskRequirements = projectToManage.getTask(taskIndex).getTaskRequirements();
                        return taskRequirements.toArray(new String[0]);
                    }
                }
                return new String[] {"The task index entered is invalid."};
            } catch (NumberFormatException e) {
                return new String[] {"Input is not a number! Please input a proper task index!"};
            }
        }
    }

    /**
     * Manages the assignment to and removal of tasks from members.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectAssignTask(Project projectToManage, String projectCommand) {
        AssignmentController assignmentController = new AssignmentController(projectToManage);
        assignmentController.assignAndUnassign(projectCommand.substring(12));
        ArrayList<String> errorMessages = assignmentController.getErrorMessages();
        ArrayList<String> successMessages = assignmentController.getSuccessMessages();
        if (!errorMessages.isEmpty()) {
            return errorMessages.toArray(new String[0]);
        }
        return successMessages.toArray(new String[0]);
    }

    /**
     * Displays list of assignments according to specifications of user.
     * @param projectToManage The project to manage.
     * @param projectFullCommand The full command by the user.
     */
    public String[] projectViewAssignments(Project projectToManage, String projectFullCommand) {
        if (projectFullCommand.length() <= 18) {
            return (new String[]{"Please input the parameters to view assignments:",
                "-m for viewing by member, -t for viewing by task.",
                "You may refer to the user guide for the list of possible commands."});
        } else {
            String input = projectFullCommand.substring(17);
            if (input.charAt(0) == '-' && input.charAt(1) == 'm') {
                return projectViewMembersAssignments(projectToManage,
                    projectFullCommand.substring(20));
            } else if (input.charAt(0) == '-' && input.charAt(1) == 't') {
                return projectViewTasksAssignments(projectToManage,
                    projectFullCommand.substring(20));
            } else {
                return (new String[] {"Could not understand your command! Please use -m for member, -t for task"});
            }
        }
    }

    /**
     * Displays all the tasks in the given project.
     * @param projectToManage The project specified by the user.
     * @param projectCommand The user input.
     */
    public String[] projectViewTasks(Project projectToManage, String projectCommand) {
        try {
            if (("view tasks").equals(projectCommand)) {
                HashMap<Task, ArrayList<Member>> tasksAndAssignedMembers = projectToManage.getTasksAndAssignedMembers();
                ArrayList<ArrayList<String>> tableToPrint = new ArrayList<>();
                ArrayList<String> allTaskDetailsForTable
                        = projectToManage.getTasks().getAllTaskDetailsForTable(tasksAndAssignedMembers, "/PRIORITY");
                allTaskDetailsForTable.add(0, "Tasks of " + projectToManage.getName() + ":");
                DukeLogger.logDebug(ProjectInputController.class, allTaskDetailsForTable.toString());
                tableToPrint.add(allTaskDetailsForTable);
                return viewHelper.consolePrintTable(tableToPrint);
            } else if (projectCommand.length() >= 11) {
                String sortCriteria = projectCommand.substring(11);
                HashMap<Task, ArrayList<Member>> tasksAndAssignedMembers = projectToManage.getTasksAndAssignedMembers();
                ArrayList<ArrayList<String>> tableToPrint = new ArrayList<>();
                ArrayList<String> allTaskDetailsForTable =
                        projectToManage.getTasks().getAllTaskDetailsForTable(tasksAndAssignedMembers, sortCriteria);
                DukeLogger.logDebug(ProjectInputController.class, allTaskDetailsForTable.toString());
                allTaskDetailsForTable.add(0, "Tasks of " + projectToManage.getName() + ":");
                tableToPrint.add(allTaskDetailsForTable);
                return viewHelper.consolePrintTable(tableToPrint);
            }
        } catch (IndexOutOfBoundsException e) {
            return (new String[] {"Currently there are no tasks with the specified attribute."});
        }
        return null;
    }

    /**
     * Prints a list of members' individual list of tasks.
     * @param projectToManage the project being managed.
     * @param projectCommand The command by the user containing index numbers of the members to view.
     */
    public String[] projectViewMembersAssignments(Project projectToManage, String projectCommand) {
        ParserHelper parserHelper = new ParserHelper();
        ArrayList<Integer> validMembers = parserHelper.parseMembersIndexes(projectCommand,
            projectToManage.getNumOfMembers());
        if (!parserHelper.getErrorMessages().isEmpty()) {
            return parserHelper.getErrorMessages().toArray(new String[0]);
        }
        return AssignmentViewHelper.getMemberOutput(validMembers,
            projectToManage).toArray(new String[0]);
    }

    /**
     * Prints a list of tasks and the members assigned to them.
     * @param projectToManage The project to manage.
     * @param projectCommand The user input.
     */
    private String[] projectViewTasksAssignments(Project projectToManage, String projectCommand) {
        ParserHelper parserHelper = new ParserHelper();
        ArrayList<Integer> validTasks = parserHelper.parseTasksIndexes(projectCommand,
            projectToManage.getNumOfTasks());
        if (!parserHelper.getErrorMessages().isEmpty()) {
            return parserHelper.getErrorMessages().toArray(new String[0]);
        }
        return AssignmentViewHelper.getTaskOutput(validTasks,
            projectToManage).toArray(new String[0]);
    }

    /**
     * Exits the current project.
     * @param projectToManage The project specified by the user.
     * @return Boolean variable specifying the exit status.
     */
    public String[] projectExit(Project projectToManage) {
        return new String[] {"Exited project: " + projectToManage.getName()};
    }

    public boolean getIsManagingAProject() {
        return isManagingAProject;
    }

    public String[] end() {
        return new String[] {"Bye. Hope to see you again soon!"};
    }


    /**
     * Add reminder to the default list list of tasks and the members assigned to them.
     * @param projectToManage The project to manage.
     * @param projectCommand The user input.
     */
    private String [] projectAddReminder(Project projectToManage, String projectCommand) {
        try {
            ReminderFactory reminderFactory = new ReminderFactory();
            IReminder newReminder = reminderFactory.createReminder(projectCommand.substring(13));
            if (newReminder.getReminderName() != null) {
                projectToManage.addReminderToList((Reminder) newReminder);
                return new String[] {"Added new reminder to the Reminder List in project."};
            }
            return new String[] {"Failed to create new task. Please ensure all "
                    + "necessary parameters are given"};

        } catch (NumberFormatException | ParseException e) {
            return new String[] {"Please enter your reminder date format correctly."};
        }
    }
}
