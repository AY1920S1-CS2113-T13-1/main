package util;


import models.project.Project;
import models.task.Task;
import util.log.ArchDukeLogger;
import util.validation.ValidityHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static util.constant.ConstantHelper.BLANK;
import static util.constant.ConstantHelper.ASSIGNEE_MARKER;
import static util.constant.ConstantHelper.ASSIGNMENT_INDEX_NUMBER_MARKER;
import static util.constant.ConstantHelper.ALL_MARKER;
import static util.constant.ConstantHelper.TASK_FLAG_LENGTH;
import static util.constant.ConstantHelper.MEMBER_FLAG_LENGTH;
import static util.constant.ConstantHelper.UNASSIGNEE_MARKER;

public class ParserHelper {
    private SortHelper sortHelper;
    private ArrayList<String> errorMessages;

    public ParserHelper() {
        this.sortHelper = new SortHelper();
        this.errorMessages = new ArrayList<>();
    }

    //@@author iamabhishek98
    /**
     * Parses the string input to extract the name, phone number and email.
     * @param input Contains the name, phone number and email.
     * @return An array consisting of name in index 0, phone number in index 1 and email in index 2.
     */
    public String[] parseMemberDetails(String input) {
        String[] memberDetails = new String[5];
        memberDetails[0] = "--";
        memberDetails[1] = "--";
        memberDetails[2] = "--";
        memberDetails[3] = "0";
        memberDetails[4] = "member";

        String[] newMemberDetails = input.split("-");
        ArrayList<String> newMemberDetailsA = new ArrayList<>(Arrays.asList(newMemberDetails));
        newMemberDetailsA.remove(0);
        for (String s : newMemberDetailsA) {
            if (s.length() <= 0) {
                continue;
            }
            switch (s.charAt(0)) {
            case 'n':
                memberDetails[0] = s.substring(1).trim();
                break;
            case 'i':
                memberDetails[1] = s.substring(1).trim();
                break;
            case 'e':
                memberDetails[2] = s.substring(1).trim();
                break;
            case 'x':
                memberDetails[3] = s.substring(1).trim();
                break;
            case 'r':
                memberDetails[4] = s.substring(1).trim();
                break;
            default:
                break;
            }
        }
        return memberDetails;
    }

    /**
     * Parses the string input to extract the name, priority, due date, credit and task state.
     * @param input Contains the name, priority, due date, credit and task state.
     * @return An ArrayList consisting of name in index 0, priority in index 1, due date in index 2,
     *         credit in index 3, task state in index 4.
     */
    public ArrayList<String> parseTaskDetails(String input) {
        errorMessages.clear();
        ArrayList<String> newTask = new ArrayList<>();

        String newTaskName = "--";
        String newTaskPriority = "-1";
        String newTaskDate = null;
        String newTaskCredit = "-1";
        String newTaskState = "NONE";

        String [] newTaskDetails = input.split("-");
        if (newTaskDetails.length == 0) {
            errorMessages.add("Please input a complete flag! Examples for valid flags include '-n', '-p', '-d', "
                    + "'-c' and '-s'. Refer to the user guide for more help!");
        } else {
            ArrayList<String> newTaskDetailsA = new ArrayList<>(Arrays.asList(newTaskDetails));
            newTaskDetailsA.remove(0);
            for (String s : newTaskDetailsA) {
                String trimmedString = s.trim();
                if (trimmedString.length() < 2) {
                    if ("n".equals(trimmedString) || "p".equals(trimmedString) || "d".equals(trimmedString)
                            || "c".equals(trimmedString) || "s".equals(trimmedString)) {
                        errorMessages.add("'-" + trimmedString + "' is an empty flag!");
                    } else {
                        errorMessages.add("'An invalid flag is used here: -" + trimmedString);
                    }
                    continue;
                }
                switch (trimmedString.substring(0, 2)) {
                case "n ":
                    newTaskName = trimmedString.substring(2);
                    break;
                case "p ":
                    newTaskPriority = trimmedString.substring(2);
                    break;
                case "d ":
                    newTaskDate = trimmedString.substring(2);
                    break;
                case "c ":
                    newTaskCredit = trimmedString.substring(2);
                    break;
                case "s ":
                    newTaskState = trimmedString.substring(2).toLowerCase();
                    break;
                default:
                    errorMessages.add("An invalid flag is used here: -" + trimmedString);
                    break;
                }
            }
        }
        newTask.add(newTaskName);
        newTask.add(newTaskPriority);
        newTask.add(newTaskDate);
        newTask.add(newTaskCredit);
        newTask.add(newTaskState);

        return newTask;
    }

    /**
     * Parses the string input to extract the name, and reminder date.
     * @param input Contains the name, and reminder date
     * @return An ArrayList consisting of name in index 0,due date in index 1.
     */
    public ArrayList<String> parseReminderDetails(String input) {
        ArrayList<String> newReminderInfo = new ArrayList<>();
        String newReminderName = "--";
        String newReminderRemarks = "--";
        String newReminderDate = null;
        String newReminderCategory = "DEFAULT";
        String [] newReminderDetails = input.split("-");
        ArrayList<String> newReminderInfoInput  =  new ArrayList<>(Arrays.asList(newReminderDetails));
        newReminderInfoInput.remove(0); // Remove the first empty string in newReminderInfoInput
        for (String s : newReminderInfoInput) {
            switch (s.charAt(0)) {
            case 'n':
                newReminderName = s.substring(1).trim();
                break;
            case 'r':
                newReminderRemarks = s.substring(1).trim();
                break;
            case 'd':
                newReminderDate = s.substring(1).trim();
                break;
            case'l':
                newReminderCategory = s.substring(1).trim().toUpperCase();
                break;
            default:
                break;
            }
        }

        newReminderInfo.add(newReminderName);
        newReminderInfo.add(newReminderRemarks);
        newReminderInfo.add(newReminderDate);
        newReminderInfo.add(newReminderCategory);
        return newReminderInfo;
    }

    /**
     * Parse input to extract the index of the reminder specify by the user.
     * @param input Contain the input from the user.
     * @return index in integer.
     */
    public int parseDeleteReminder(String input) {
        errorMessages.clear();
        String [] deleteReminderDetails = input.split(" ");
        ValidityHelper validityHelper = new ValidityHelper();
        if (deleteReminderDetails.length > 3 || !validityHelper.digitChecker(deleteReminderDetails[2])) {
            errorMessages.add("Please input the correct command! Example, delete reminder REMINDER_INDEX");
            return 0;
        } else {
            return Integer.parseInt(deleteReminderDetails[2]);
        }
    }

    /**
     * Parse input to extract the index of the reminder specify by the user.
     * @param input Contain the input from the user.
     * @return index in integer.
     */
    public int parseEditReminder(String input) {
        errorMessages.clear();
        String [] editReminderDetails = input.split("-");
        String []  editReminderCommand = editReminderDetails[0].split(" ");

        ValidityHelper validityHelper = new ValidityHelper();
        if (editReminderCommand.length > 3 || !validityHelper.digitChecker(editReminderCommand[2])) {
            errorMessages.add("Please input the correct command! "
                    + "Example, edit reminder REMINDER_INDEX -n REMINDER_NAME");
            return 0;
        } else {
            return Integer.parseInt(editReminderCommand[2]);
        }
    }

    /**
     * Parses string input to extract information on marking/un-marking Reminder.
     * @param input Contains the command of marking/un-marking reminder.
     * @return An ArrayList String consisting of the status and indexes to be updated or null.
     */
    public ArrayList<String> parseCheckReminder(String input) {
        errorMessages.clear();
        String [] checkReminderDetails = input.split(" ");
        ArrayList<String> newCheckReminderInfo = new ArrayList<>();
        String booleanString = "";
        String indexString = "";
        ValidityHelper validityHelper = new ValidityHelper();
        if (checkReminderDetails.length > 3 || !validityHelper.digitChecker(checkReminderDetails[2])) {
            errorMessages.add("Please input the correct command! Example, mark reminder REMINDER_INDEX");
            return null;
        } else if (checkReminderDetails[0].equals("unmark") && validityHelper.digitChecker(checkReminderDetails[2])) {
            booleanString = "false";
            indexString = checkReminderDetails[2];
        } else if (checkReminderDetails[0].equals("mark") && validityHelper.digitChecker(checkReminderDetails[2])) {
            booleanString = "true";
            indexString = checkReminderDetails[2];
        }
        newCheckReminderInfo.add(booleanString);
        newCheckReminderInfo.add(indexString);

        return newCheckReminderInfo;
    }

    /**
     * Parses string input to extract task requirements to be added and indexes of task requirements to be removed.
     * @param input Contains the new task requirements and indexes of task requirements to be removed.
     * @return An ArrayList consisting of indexes to be removed in index 0, new task requirements and error messages
     */
    public ArrayList<String> parseTaskRequirementDetails(String input) {
        ArrayList<String> taskRequirementDetails = new ArrayList<>();

        ArrayList<String> taskReqIndexesToBeRemoved = new ArrayList<>();
        ArrayList<String> taskRequirementsToBeAdded = new ArrayList<>();
        errorMessages.clear();

        String[] newTaskRequirementsArray = input.split("-");
        if (newTaskRequirementsArray.length == 0) {
            errorMessages.add("Please input a complete flag! Examples for valid flags include '-r' and '-rm'."
                    + " Refer to the user guide for more help!");
        } else {
            ArrayList<String> newTaskRequirementsArrayList = new ArrayList<>(Arrays.asList(newTaskRequirementsArray));
            newTaskRequirementsArrayList.remove(0);
            for (String s : newTaskRequirementsArrayList) {
                String trimmedString = s.trim();
                if (trimmedString.length() <= 2) {
                    if ("r".equals(trimmedString) || "rm".equals(trimmedString)) {
                        errorMessages.add("There is an empty flag '-" + trimmedString + "'");
                    } else {
                        errorMessages.add("'-" + trimmedString + "' is an invalid flag");
                    }
                    continue;
                }

                switch (trimmedString.split(" ")[0]) {
                case "rm":
                    String[] splitTrimmedString = trimmedString.substring(3).split(" ");
                    taskReqIndexesToBeRemoved.addAll(Arrays.asList(splitTrimmedString));
                    break;
                case "r":
                    taskRequirementsToBeAdded.add(trimmedString.substring(2));
                    break;
                default:
                    errorMessages.add("Invalid flag is used in this entry: -" + trimmedString);
                    break;
                }
            }
        }

        if (taskReqIndexesToBeRemoved.size() == 0) {
            taskRequirementDetails.add("--");
        } else {
            StringBuilder taskReqIndexesToBeRemovedString = new StringBuilder();
            for (String s1 : taskReqIndexesToBeRemoved) {
                taskReqIndexesToBeRemovedString.append(s1);
                taskReqIndexesToBeRemovedString.append(" ");
            }
            taskRequirementDetails.add(taskReqIndexesToBeRemovedString.toString().trim());
        }

        taskRequirementDetails.addAll(taskRequirementsToBeAdded);
        return taskRequirementDetails;
    }

    //@@author iamabhishek98
    /**
     * Parses the criteria specified by the user to sort the list of tasks.
     * @param tasksAndAssignedMembers HashMap containing tasks with assigned members.
     * @param taskList A list of all tasks in the project.
     * @param sortCriteria Criteria to sort the list of tasks
     * @return An ArrayList with String descriptions of task details sorted by the criteria specified by the user.
     */
    public ArrayList<String> parseSortTaskDetails(
            HashMap<String, ArrayList<String>> tasksAndAssignedMembers, ArrayList<Task> taskList,
            String sortCriteria, Project project) {
        ArrayList<String> taskDetails = new ArrayList<>();
        if (sortCriteria.length() >= 4) {
            String[] detailedCriteria = sortCriteria.split(" ",2);
            switch (detailedCriteria[0]) {
            case "-name":
                taskDetails = this.sortHelper.sortTaskName(taskList);
                break;
            case "-date":
                taskDetails = this.sortHelper.sortTaskDueDate(taskList);
                break;
            case "-priority":
                taskDetails = this.sortHelper.sortTaskPriority(taskList);
                break;
            case "-credits":
                taskDetails = this.sortHelper.sortTaskCredit(taskList);
                break;
            case "-who":
                taskDetails = this.sortHelper.sortTaskMember(tasksAndAssignedMembers, taskList,
                        detailedCriteria[1], project);
                break;
            case "-state":
                taskDetails = this.sortHelper.sortTaskState(taskList, detailedCriteria[1].toUpperCase());
                break;
            default:
                break;
            }
        }
        return taskDetails;
    }

    //@@author sinteary
    /**
     * Parses input String to get valid task and member index numbers, as well as error messages
     * for invalid index numbers.
     * @param input The assignment input from the user.
     */
    public ArrayList<ArrayList<Integer>> parseAssignmentParams(String input, Project project) {
        errorMessages.clear();
        String [] inputParts = input.split("-");
        String allTaskIndexes = BLANK;
        String allAssigneeIndexes = BLANK;
        String allUnassigneeIndexes = BLANK;

        for (String commandPart : inputParts) {
            String [] part = commandPart.split(" ");
            if (part.length == 0) {
                continue;
            }
            switch (part[0]) {
            case ASSIGNMENT_INDEX_NUMBER_MARKER:
                if (commandPart.length() >= TASK_FLAG_LENGTH) {
                    allTaskIndexes = commandPart.substring(TASK_FLAG_LENGTH - 1).trim();
                }
                break;
            case ASSIGNEE_MARKER:
                if (commandPart.length() >= MEMBER_FLAG_LENGTH) {
                    allAssigneeIndexes = commandPart.substring(MEMBER_FLAG_LENGTH - 1).trim();
                }
                break;
            case UNASSIGNEE_MARKER:
                if (commandPart.length() >= MEMBER_FLAG_LENGTH) {
                    allUnassigneeIndexes = commandPart.substring(MEMBER_FLAG_LENGTH - 1).trim();
                }
                break;
            default:
            }
        }

        /**
         * The ArrayList assignmentParams contains 3 ArrayLists: containing valid index numbers of tasks,
         * assignees and unassignees respectively.
         */
        ArrayList<ArrayList<Integer>> assignmentParams = new ArrayList<>();
        ArrayList<Integer> taskIndexes = parseTasksIndexes(allTaskIndexes, project.getNumOfTasks());
        ArrayList<Integer> assignees = parseMembersIndexes(allAssigneeIndexes, project.getNumOfMembers());
        ArrayList<Integer> unassignees = parseMembersIndexes(allUnassigneeIndexes, project.getNumOfMembers());
        checkForSameMemberIndexes(assignees, unassignees, project);
        assignmentParams.add(taskIndexes);
        assignmentParams.add(assignees);
        assignmentParams.add(unassignees);

        return assignmentParams;
    }

    /**
     * Parses a string containing member index numbers and returns only valid ones.
     * @param input a string containing member index numbers.
     * @param numberOfMembersInProject the total number of members in project.
     * @return An ArrayList containing only valid member index numbers
     */
    public ArrayList<Integer> parseMembersIndexes(String input, int numberOfMembersInProject) {
        ArrayList<Integer> validMembers = new ArrayList<>();
        if (ALL_MARKER.equals(input)) {
            for (int i = 1; i <= numberOfMembersInProject; i++) {
                validMembers.add(i);
            }
            return validMembers;
        }
        String[] inputParts = input.split(" ");
        for (String index : inputParts) {
            if (BLANK.equals(index)) {
                continue;
            }
            try {
                Integer indexNumber = Integer.parseInt(index);
                if (indexNumber > 0 && indexNumber <= numberOfMembersInProject) {
                    validMembers.add(indexNumber);
                } else {
                    errorMessages.add("Member with index " + index + " does not exist.");
                }
            } catch (NumberFormatException e) {
                ArchDukeLogger.logError(ParserHelper.class.getName(), "[parseMemberIndexes] "
                        + "Invalid member index: " + index);
                errorMessages.add("Could not recognise member " + index
                        + ", please ensure it is an integer.");
            }
        }
        return validMembers;
    }

    /**
     * Returns a list of valid task numbers.
     * @param input List of task index numbers input by user.
     * @param numberOfTasksInProject the total number of tasks in project.
     * @return An ArrayList containing valid task index numbers to show assigned members.
     */
    public ArrayList<Integer> parseTasksIndexes(String input, int numberOfTasksInProject) {
        ArrayList<Integer> tasksToView = new ArrayList<>();
        if (ALL_MARKER.equals(input)) {
            for (int i = 1; i <= numberOfTasksInProject; i++) {
                tasksToView.add(i);
            }
            return tasksToView;
        }
        String[] inputParts = input.split(" ");
        for (String index : inputParts) {
            if (BLANK.equals(index)) {
                continue;
            }
            try {
                Integer indexNumber = Integer.parseInt(index);
                if (indexNumber > 0 && indexNumber <= numberOfTasksInProject) {
                    tasksToView.add(indexNumber);
                } else {
                    errorMessages.add("Task with index " + index + " does not exist.");
                }
            } catch (NumberFormatException e) {
                ArchDukeLogger.logError(ParserHelper.class.getName(), "[parseTasksIndexes]"
                        + "Invalid task number: " + index);
                errorMessages.add("Could not recognise task " + index
                        + ", please ensure it is an integer.");
            }
        }
        return tasksToView;
    }

    private void checkForSameMemberIndexes(ArrayList<Integer> assignees, ArrayList<Integer> unassignees,
                                           Project project) {
        ArrayList<Integer> repeated = new ArrayList<>();
        for (Integer index: assignees) {
            if (unassignees.contains(index)) {
                repeated.add(index);
                ArchDukeLogger.logError(ParserHelper.class.getName(), "[checkForSameMemberIndexes] "
                        + " Same index in assign and unassign: " + index);
                errorMessages.add("Cannot assign and unassign task to member " + index + " ("
                        + project.getMember(index).getName() + ") at the same time");

            }
        }
        for (Integer index: repeated) {
            assignees.remove(index);
            unassignees.remove(index);
        }
    }

    public ArrayList<String> getErrorMessages() {
        return this.errorMessages;
    }
    //@@author
}
