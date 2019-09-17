package controllers;

import exceptions.DukeException;
import models.tasks.Deadline;
import models.tasks.Event;
import models.tasks.ITask;
import models.tasks.ToDos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TaskFactory {
    /**
     * Factory class responsible for creation of objects based on interface.
     *
     * @param input : Command typed into CLI
     * @return : returns an models.tasks.ITask based on command typed into CLI
     * @throws DukeException : when command entered does not match existing Tasks
     */
    public ITask createTask(String input) throws DukeException {
        String[] allArgs = input.split(" ");
        List<String> listArgs = new ArrayList<>(Arrays.asList(allArgs));
        String tempString;
        String[] parsedStrings;
        Date date;
        String formattedDate;
        switch (allArgs[0]) {
        case "todo":
            listArgs.remove(0);
            tempString = String.join(" ", listArgs);
            parsedStrings = tempString.split(" /in ");
            if (listArgs.size() == 0) {
                throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
            } else if (parsedStrings.length == 1) {
                throw new DukeException("☹ OOPS!!! The todo duration cannot be empty.");
            }
            return new ToDos(parsedStrings[0], parsedStrings[1]);
        case "deadline":
            listArgs.remove(0); // Remove "deadline"
            tempString = String.join(" ", listArgs);
            parsedStrings = tempString.split(" /by ");
            try {
                // Correct format as 2 December 2019 6 PM
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
                date = formatter.parse(parsedStrings[1]);

                String hour = new SimpleDateFormat("hh").format(date);
                String min = new SimpleDateFormat("mm").format(date);
                String periodMarker = new SimpleDateFormat("a").format(date);
                String formattedTime = hour + "." + min + " " + periodMarker;

                formattedDate = new SimpleDateFormat("d MMMM yyyy").format(date) + " " + formattedTime;

            } catch (ParseException e) {
                // Invalid Date and Time, revert back to lazyTiming
                return new Deadline(parsedStrings[0], parsedStrings[1]);
            }
            return new Deadline(parsedStrings[0], formattedDate);
        case "event":
            listArgs.remove(0); // Remove "event"
            tempString = String.join(" ", listArgs);
            parsedStrings = tempString.split(" /at ");
            try {
                // Correct format as 2 December 2019, 6 PM
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HHmm");
                date = formatter.parse(parsedStrings[1]);

                String hour = new SimpleDateFormat("hh").format(date);
                String min = new SimpleDateFormat("mm").format(date);
                String periodMarker = new SimpleDateFormat("a").format(date);
                String formattedTime = hour + "." + min + " " + periodMarker;

                formattedDate = new SimpleDateFormat("d MMMM yyyy").format(date) + " " + formattedTime;

            } catch (ParseException e) {
                // Invalid Date and Time, revert back to lazyTiming
                return new Event(parsedStrings[0], parsedStrings[1]);
            }
            return new Event(parsedStrings[0], formattedDate);
        default:
            throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }
}
