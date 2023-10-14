import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Config {
    boolean verbose;
    boolean median;
    boolean general;
    boolean watcher;
    LongOpt[] longOptions = {
            new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, 'v'),
            new LongOpt("median", LongOpt.NO_ARGUMENT, null, 'm'),
            new LongOpt("general-eval", LongOpt.NO_ARGUMENT, null, 'g'),
            new LongOpt("watcher", LongOpt.NO_ARGUMENT, null, 'w'),
    };

    public Config(String [] args) {


        Getopt g = new Getopt("Project2", args, "vmgw", longOptions);
        int choice = 0;
        while( (choice = g.getopt()) != -1) {
            switch (choice) {
                case 'v':
                    verbose = true;
                    break;
                case 'm':
                    median = true;
                    break;
                case 'g':
                    general = true;
                    break;
                case 'w':
                    watcher = true;
                    break;
                default:
                    System.out.println("Command arguments not set up yet");
            }
        }




    }


}
