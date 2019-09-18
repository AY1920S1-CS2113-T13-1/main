package models.tasks;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskList implements Serializable {
    private static final int DAYS_FROM_NOW = 7;
    private ArrayList<ITask> listOfTasks;
    private ArrayList<ITask> searchedTasks;
    private ArrayList<ITask> schedule;

    public TaskList() {
        listOfTasks = new ArrayList<>();
    }

    /**
     * Adds the task to the tasklist.
     * @param newTask : A new task that is added by the user. Task is created by Factory.
     * @return : Boolean value which gives status of anomaly detection.
     */
    public boolean addToList(ITask newTask) {
        if (!detectAnomalies(newTask)) {
            this.listOfTasks.add(newTask);
            return false;
        }
        return true;
    }

    public void deleteFromList(ITask oldTask) {
        listOfTasks.remove(oldTask);
    }

    public ArrayList<ITask> getAllTasks() {
        return this.listOfTasks;
    }

    public ITask getTask(int index) {
        return this.listOfTasks.get(index);
    }

    public int getNumOfTasks() {
        return this.listOfTasks.size();
    }

    /**
     * Method that returns tasks that match with search keywords.
     * @param input : Keyword that users wish to search for
     * @return : Returns an ArrayList of ITask that matches keyword
     */
    public ArrayList<ITask> getSearchedTasks(String input) {
        String [] allInputs = input.split(" ");
        searchedTasks = new ArrayList<>();
        for (ITask task : listOfTasks) {
            if (task.getDescription().contains(allInputs[1])) {
                searchedTasks.add(task);
            }
        }
        return searchedTasks;
    }

    /**
     * Returns the schedule for the date.
     * @param date : The date input by the user.
     * @return : Returns an ArrayList of ITask which are in the schedule
     */
    public ArrayList<ITask> getSchedule(String date) {
        schedule = new ArrayList<>();
        for (ITask task : listOfTasks) {
            if (task.getDateTime().contains(date)) {
                schedule.add(task);
            }
        }
        return schedule;
    }

    /**
     * Checks for anomalies with the current schedule.
     * @param newTask : A new task that is added by the user. Task is created by Factory.
     * @return : Boolean value which gives status of anomaly detection.
     */
    private boolean detectAnomalies(ITask newTask) {
        if (newTask instanceof ToDos) {
            return false;
        } else if (newTask.getDateTime().equals("")) {
            return false;
        }
        schedule = getSchedule(newTask.getDateTime());
        for (ITask task : schedule) {
            if (task.getDateTime().equals(newTask.getDateTime())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns upcoming tasks within the next 7 days by default, or within a date and time given by the user.
     *
     * @param limit The date and time limit given by the user.
     * @return The list of tasks from the current time until the time limit.
     * @throws ParseException Parsing error (If date and time are not entered in dd/MM/yyyy HHmm)
     */
    public ArrayList<ITask> getUpcomingTasks(String limit) throws ParseException {
        ArrayList<ITask> upcomingTasks = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy hh.mm a");

        Date remindWithin;
        Date currentDateTime = new Date(System.currentTimeMillis());

        //If no limit given by user, by default will remind user of tasks in coming 7 days
        if (limit.equals("")) {
            Calendar c = Calendar.getInstance();
            c.setTime(currentDateTime);
            c.add(Calendar.DATE, DAYS_FROM_NOW);
            remindWithin = c.getTime();
        } else {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy HHmm");
            remindWithin = inputDateFormat.parse(limit);
        }

        for (ITask task: this.listOfTasks) {
            String taskInitial = task.getInitials();
            if (taskInitial.equals("T")) {
                continue;
            }
            Date taskDate = dateFormat.parse(task.getDateTime());
            if (taskDate.compareTo(currentDateTime) >= 0 && taskDate.compareTo(remindWithin) <= 0) {
                upcomingTasks.add(task);
            }
        }
        return upcomingTasks;
    }
}
