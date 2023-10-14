public class Main {

    public static void main(String[] args) {
        Config config = new Config(args);
        Simulation War = new Simulation(config);
        War.performWarfare();
    }
}
