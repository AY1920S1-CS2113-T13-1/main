package logic.command;

import model.Member;
import model.Model;
import model.Task;
import utils.DukeException;

public class LinkCommand extends Command{
    int[] tasksIndexes;
    String[] membersNames;

    public LinkCommand(int[] tasksIndexes, String[] membersNames) {
        this.tasksIndexes = tasksIndexes;
        this.membersNames = membersNames;
    }

    @Override
    public CommandOutput execute(Model model) throws DukeException {
        checkAvailability(model);
        String output = "";
        for (int i = 0; i < tasksIndexes.length; i++) {
            for (int j = 0; j < membersNames.length; j++) {
                //model.link(tasksIndexes[i], membersNames[j]);
                //uncomment above when link in model finished
                output += "Noted, assigned task "
                        + model.getTasksManager().getTaskById(tasksIndexes[i]).getName()
                        + " to member "
                        + membersNames[j];
            }
        }
        return new CommandOutput(output);
    }

    public void checkAvailability(Model model) throws DukeException {
        int taskListLength = model.getTaskList().size();
        for (int i = 0; i < tasksIndexes.length; i++) {
            if (tasksIndexes[i] < 0 || tasksIndexes[i] > taskListLength - 1) {
                throw new DukeException("Index range out of range, please check and try again.");
            }
        }
        for (int i = 0; i < membersNames.length; i++) {
            if (model.getMemberManager().getMemberByName(membersNames[i]) == null) {
                throw new DukeException("Cannot find member " + membersNames[i] +", please check and try again");
            }
        }
    }
}