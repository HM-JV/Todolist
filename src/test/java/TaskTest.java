import com.humanbooster.todolist.HelloWorldTask;
import com.humanbooster.todolist.TaskManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaskTest {


    @Test
    public void canCreateTask(){
        TaskManager manager = new TaskManager();
        HelloWorldTask task = new HelloWorldTask();
        task.nom = "mother";
        task.dateFin = "22-07-2018";
        manager.addTask(task, "Aucun");
        HelloWorldTask toCheck = manager.allTask().get(0);
        assertEquals("mother", toCheck.nom);
        assertEquals("22-07-2018", toCheck.dateFin);
    }

    @Test
    public void daughterShouldBeBeforeMotherDate(){
        TaskManager manager = new TaskManager();
        HelloWorldTask mother = new HelloWorldTask();
        mother.nom = "mother";
        mother.dateFin = "22-07-2018";
        manager.addTask(mother, "Aucun");

        boolean toLateCantCreate = manager.compareDate("31-12-2020", "mother");
        assertEquals(false, toLateCantCreate);

        boolean okCanCreate = manager.compareDate("31-12-2010", "mother");
        assertEquals(true, okCanCreate);

        boolean okWithSameDate = manager.compareDate("22-07-2018", "mother");
        assertEquals(true, okWithSameDate);
    }

    @Test
    public void canAddDaughter(){
        TaskManager manager = new TaskManager();
        HelloWorldTask mother = new HelloWorldTask();
        mother.nom = "mother";
        mother.dateFin = "22-07-2018";
        manager.addTask(mother, "Aucun");


        HelloWorldTask daughter = new HelloWorldTask();
        daughter.nom = "daughter1";
        daughter.dateFin = "01-01-2017";
        daughter.tachePere = 0;

        HelloWorldTask daughterWrong = new HelloWorldTask();
        daughterWrong.nom = "daughter2";
        daughterWrong.dateFin = "01-01-2030";
        daughterWrong.tachePere = 0;

        manager.addTask(daughter, "mother");
        assertEquals(2, manager.allTask().size());
        assertEquals("daughter1", manager.allTask().get(1).Nom);

        manager.addTask(daughterWrong, "mother");
        assertEquals( 2, manager.allTask().size());
    }
}
