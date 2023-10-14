public class jediDeployment extends Deployment {
    public jediDeployment(int id, long ts, int g, int p, int f, int q) {
        super(id, ts, g, p, f, q);
    }


    @Override
    public boolean isSith() {
        return false;
    }

    @Override
    public int compareTo(Deployment d) {
        // this
        // d
        // return a negetive number if "this" is higher prioroity than d
        // return a posotive number if "this"is lower priority
        // first attempt

        if (this.force == d.force) {
            //which came earlier?
            // sith deployment is the same here
            return this.id - d.id;
        }
        // if the forces are different
        // This is different for Sith.
        return this.force - d.force;
    }
}
