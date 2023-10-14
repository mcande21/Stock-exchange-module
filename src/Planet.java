import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Planet {

    private PriorityQueue<Deployment> jediDeployment;
    private PriorityQueue<Deployment> sithDeployment;

    // 2 priority Q's for the median
    private PriorityQueue<Integer> lowerMed = new PriorityQueue<>(Collections.reverseOrder());
    private PriorityQueue<Integer> higherMed = new PriorityQueue<>();

    private int Median;

    private int id;

    private int numBattles = 0;

    public Planet(int id) {
        this.id = id;

        jediDeployment = new PriorityQueue<>();
        sithDeployment = new PriorityQueue<>();
    }

    public void addDeployment(Deployment d) {
        if (d.isSith()) {
            sithDeployment.add(d);
        } else {
            jediDeployment.add(d);
        }
    }

    public boolean canBattle() {
        //check to see if there is a valid battle
        if (jediDeployment.isEmpty() || sithDeployment.isEmpty())
            return false;
        return jediDeployment.peek().force <= sithDeployment.peek().force;
    }

    public void performBattles(Config c) {

        while (canBattle()) {
            // a battle can occur
            // subtracts the same number of troops from both sith and jedi deployments
            int troopsLost = Math.min(jediDeployment.peek().quantity, sithDeployment.peek().quantity);

            // subtract these from both deployments
            jediDeployment.peek().quantity -= troopsLost;
            sithDeployment.peek().quantity -= troopsLost;

            // print output before updating the PQ
            if (c.verbose) {
                System.out.print("General " + sithDeployment.peek().general + "'s battalion attacked General "
                + jediDeployment.peek().general + "'s battalion on planet " + id + ". " + (troopsLost *2) + " troops were lost. \n");
            }

            // one of these deployments will have lost all the troops
            // remove that deployment from the PQ
            if (jediDeployment.peek().quantity == 0) {
                jediDeployment.remove();
            }
            if (sithDeployment.peek().quantity == 0) {
                sithDeployment.remove();
            }
            //update the count of our battles
            numBattles++;

            // adding the troops lost to the running median
            if (c.median) {
                updateMedian(troopsLost * 2);
            }
        }
    }

    private void updateMedian(int troopsLost) {
        // add to the PQs
        if (lowerMed.isEmpty() && higherMed.isEmpty()) {
            lowerMed.add(troopsLost);
            Median = troopsLost;
        } else {
            // all other cases
            //determining half to insert
            if (troopsLost < Median) {
                lowerMed.add(troopsLost);
            } else {
                higherMed.add(troopsLost);
            }

            // replace the size of PQs
            if (lowerMed.size() - higherMed.size() == 2) {
                // shift one element from bottom --> top
                higherMed.add(lowerMed.remove());
            } else if (higherMed.size() - lowerMed.size() == 2){
                // shift one element from top --> bottom
                lowerMed.add(higherMed.remove());
            }
            // update median
            if (lowerMed.size() > higherMed.size()) {
                Median = lowerMed.peek();
            } else if (lowerMed.size() < higherMed.size()) {
                Median = higherMed.peek();
            } else {
                Median = (higherMed.peek() + lowerMed.peek()) / 2;
            }
        }
    }

    public int getNumBattles() {
        return numBattles;
    }

    public void printMedian(Long timestamp) {
        if (getNumBattles() == 0) {
            return;
        }

        //print out the current median value
        System.out.printf("Median troops lost on planet %d at time %d is %d.\n", id, timestamp, Median);


    }

    public void countSurvivors(ArrayList<General> generals) {
        // go through the Sith and Jedi PQ's and add survivors.
        for (Deployment s : sithDeployment) {
            // pop each deployment and add remaining troops to the survivor count with appropriate general
            generals.get(s.general).addSurvivors(s.quantity);
        }
        // pop each jedi deployment and do the same thing
        for (Deployment j : jediDeployment) {
            generals.get(j.general).addSurvivors(j.quantity);
        }
    }
}
