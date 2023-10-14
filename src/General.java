public class General {

    private int numJedi, numSith, numSurvive;

    public General() {
        numJedi = 0;
        numSith = 0;
        numSurvive = 0;
    }

    public void addDeployment(Deployment d) {
        // update the running counts for sith and jedi
        if(d.isSith()) {
            numSith += d.quantity;
        } else {
            numJedi += d.quantity;
        }
    }

    public void addSurvivors(int s) {
        numSurvive += s;
    }


    public void printStats(int g) {
        StringBuilder print = new StringBuilder();
        print.append("General ").append(g).append(" deployed ").append(numJedi).append(" Jedi troops and ")
                .append(numSith).append(" Sith ").append("troops, and ").append(numSurvive).append("/")
                .append(numSith + numJedi).append(" troops survived.\n");
        System.out.print(print);
    }
}
