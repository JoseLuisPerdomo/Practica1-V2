import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        TableCreator.tablesCreation();

        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
           @Override
           public void run() {
        TableCreator.tablesupdates();
                System.out.println("Executing HTTP request function every six hours...");
            }
        };

        long delay = 0;
        long period = 6 * 60 * 60 * 1000;
        timer.scheduleAtFixedRate(task, delay, period);
    }
}