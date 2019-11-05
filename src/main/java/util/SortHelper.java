package util;

import models.member.Member;
import models.task.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;

public class SortHelper {
    /**
     * Sorts the list of tasks by the task name in alphabetical order.
     * @param taskList The list of tasks.
     * @return The list of tasks sorted by the task name.
     */
    public ArrayList<String> sortTaskName(ArrayList<Task> taskList) {
        ArrayList<String> taskDetails = new ArrayList<>();
        taskList.sort(Comparator.comparing(Task::getTaskName));
        int taskIndex = 1;
        for (Task task : taskList) {
            taskDetails.add(taskIndex + ". " + task.getDetails());
            taskIndex++;
        }
        return taskDetails;
    }

    /**
     * Sorts the list of tasks by the task due date in descending order.
     * @param taskList The list of tasks.
     * @return The list of tasks sorted by the task due date.
     */
    public ArrayList<String> sortTaskDueDate(ArrayList<Task> taskList) {
        ArrayList<String> taskDetails = new ArrayList<>();
        ArrayList<Task> sortedTaskList = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getDueDate() != null) {
                sortedTaskList.add(task);
            }
        }
        sortedTaskList.sort((task1, task2) -> task2.getDueDate().compareTo(task1.getDueDate()));
        Collections.reverse(sortedTaskList);
        int taskIndex = 1;
        for (Task task : sortedTaskList) {
            taskDetails.add(taskIndex + ". " + task.getDetails());
            taskIndex++;
        }
        return taskDetails;
    }

    /**
     * Sorts the list of tasks by the task priority in descending order.
     * @param taskList The list of tasks.
     * @return The list of tasks sorted by the task priority.
     */
    public ArrayList<String> sortTaskPriority(ArrayList<Task> taskList) {
        ArrayList<String> taskDetails = new ArrayList<>();
        taskList.sort((task1, task2) -> task1.getTaskPriority() - task2.getTaskPriority());
        int taskIndex = 1;
        for (Task task : taskList) {
            taskDetails.add(taskIndex + ". " + task.getDetails());
            taskIndex++;
        }
        return taskDetails;
    }

    /**
     * Sorts the list of tasks by the task credit in descending order.
     * @param taskList The list of tasks.
     * @return The list of tasks sorted by the task credit.
     */
    public ArrayList<String> sortTaskCredit(ArrayList<Task> taskList) {
        ArrayList<String> taskDetails = new ArrayList<>();
        taskList.sort((task1, task2) -> task2.getTaskCredit() - task1.getTaskCredit());
        int taskIndex = 1;
        for (Task task : taskList) {
            taskDetails.add(taskIndex + ". " + task.getDetails());
            taskIndex++;
        }
        return taskDetails;
    }

    /**
     * Sorts the list of tasks by the name of the members assigned to the tasks in alphabetical order.
     * @param tasksAndAssignedMembers HashMap containing tasks with assigned members.
     * @param taskList The list of tasks.
     * @return The list of tasks sorted by the name of the members assigned to the tasks in alphabetical order.
     */
    public ArrayList<String> sortTaskMember(HashMap<Task, ArrayList<Member>> tasksAndAssignedMembers,
                                            ArrayList<Task> taskList, String memberName) {
        ArrayList<Task> allAssignedTasks = new ArrayList<>();
        // to be made more efficient
        for (Task task: taskList) {
            for (Member member : tasksAndAssignedMembers.get(task)) {
                if (member.getName().equals(memberName)) {
                    allAssignedTasks.add(task);
                    break;
                }
            }
        }
        return this.sortTaskName(allAssignedTasks);
    }

    /**
     * Filters the list of tasks by the task state.
     * @param taskList The List of tasks.
     * @param state The task state required to filter the task list.
     * @return The list of tasks filtered by the task state.
     */
    public ArrayList<String> sortTaskState(ArrayList<Task> taskList, String state) {
        ArrayList<String> taskDetails = new ArrayList<>();
        int taskIndex = 1;
        for (Task task : taskList) {
            if (state.equals(task.getTaskState().toString())) {
                taskDetails.add(taskIndex + ". " + task.getDetails());
            }
            taskIndex++;
        }
        return taskDetails;
    }
}
