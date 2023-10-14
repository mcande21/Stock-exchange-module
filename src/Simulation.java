import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    private ArrayList<General> genrals;
    private ArrayList<Planet> planets;

    private Config config;

    private Scanner in;

    private int currId = 0;

    public Simulation(Config c) {
        config = c;

        in = new Scanner(System.in);

        // skip first line, it is a comment
        in.nextLine();

        // read the mode
        in.next();
        String mode = in.next();

        // read the nu,mber of generals
        in.next();
        int numGenerals = in.nextInt();

        // read the number of planets
        in.next();
        int numPlanets = in.nextInt();

        genrals = new ArrayList<>(numGenerals);
        for (int i = 0; i < numGenerals; i++) {
            genrals.add(new General());
        }

        planets = new ArrayList<>(numPlanets);
        for (int i = 0; i < numPlanets; i++) {
            planets.add(new Planet(i));
        }

        // if PR mode, finalize set up
        if (mode.equals("PR")) {
            // random seed
            in.next();
            int seed = in.nextInt();

            //num deployments
            in.next();
            int numDeployments = in.nextInt();

            // arrival rate
            in.next();
            int arrivalRate = in.nextInt();

            // create our random deployment generator
            in = P2Random.PRInit(seed, numGenerals, numPlanets, numDeployments, arrivalRate);
        }

        // in scanner
        // DL: system.in
        // PR: P2Random


    }

    public void performWarfare() {
        long currenttime = 0;

        // print out the simulation starting message
        System.out.println("Deploying troops...");

        // make sure this works
        while (in.hasNextLine() && in.hasNext()) {
            Deployment d = getNextDeployment();

            // check the time
            if (currenttime < d.timestamp) {

                if (config.median) {
                    // median mode is on so print out median
                    for (Planet x : planets) {
                        x.printMedian(currenttime);
                    }
                }
                //update time
                currenttime = d.timestamp;

            } if ( currenttime > d.timestamp){
                System.err.println("Timestamp" + currenttime + " is greater then next timestamp.");
                System.exit(1);
            }
            // track the deployment for general eval mode
            if (config.general)
                genrals.get(d.general).addDeployment(d);

            Planet p = planets.get(d.planet);
            p.addDeployment(d);
            p.performBattles(config);

        }

        // no more deployments
        // print out running medians
        if (config.median) {
            // median mode is on so print out median
            for (Planet p : planets) {
                p.printMedian(currenttime);
            }
        }
        // print out summary
        System.out.print("---End of Day---\n");

        // count up battles
        int totalBattles = 0;
        for (Planet p : planets){
            totalBattles += p.getNumBattles();
        }
        System.out.print("Battles: " + totalBattles + "\n");

        // general eval output
        if (config.general) {
            for (Planet p : planets) {
                // count up survivors and add them to appropriate general
                p.countSurvivors(genrals);
            }
            //P Print out general header
            System.out.print("---General Evaluation---\n");

            for (General g : genrals) {
                g.printStats(genrals.indexOf(g));
            }
        }
    }

    private Deployment getNextDeployment() {
        // read all anf construct a deployment object to represent the deployment
        long timestamp = in.nextLong();
        String type = in.next();

        // Integer.parseInt()
        int genralId = Integer.parseInt(in.next().substring(1));
        int planetId = Integer.parseInt(in.next().substring(1));
        int force = Integer.parseInt(in.next().substring(1));
        int numTroops = Integer.parseInt(in.next().substring(1));

        //error checking
        if (timestamp < 0) {
            System.err.println("Timestamp is a negative number.");
            System.exit(1);
        } else if (!(genralId < genrals.size() && genralId >= 0)){
            System.err.println("General ID is out of range");
            System.exit(1);
        } else if (!(planetId < planets.size() && planetId >= 0)){
            System.err.println("Planet ID is out of range");
            System.exit(1);
        } else if (force <= 0) {
            System.err.println("force " + force + " is either negative or zero");
            System.exit(1);
        } else if (numTroops <= 0) {
            System.err.println("numTroops " + numTroops + " is either negative or zero");
            System.exit(1);
        }

        if (type.equals("SITH")) {
            return new sithDeployment(currId++, timestamp, genralId, planetId, force, numTroops);
        } else {
            return new jediDeployment(currId++, timestamp, genralId, planetId, force, numTroops);
        }

    }
}
