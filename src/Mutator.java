import java.util.Random;

public class Mutator {

    // swamp the order of the customers for a single vehicle
    public static void MutateS(Pop pop, Random r){
        int v = r.nextInt(pop.vehicles);
        while(pop.customerOrder[v].length == 1){
            v = r.nextInt(pop.vehicles);
        }
        int i = r.nextInt(pop.customerOrder[v].length);
        int j = r.nextInt(pop.customerOrder[v].length);
        while(i==j){
            j = r.nextInt(pop.customerOrder[v].length);
        }
        int first = pop.customerOrder[v][i];
        pop.customerOrder[v][i] = pop.customerOrder[v][j];
        pop.customerOrder[v][j] = first;
    }

    // move a customer from one route to another
    public static void MutateM(Pop pop, Random r){
        int v1 = r.nextInt(pop.vehicles);
        int v2 = r.nextInt(pop.vehicles);
        int tries = 0;
        while(v1==v2 ||  pop.customerOrder[v1].length == 1){
            v1 = r.nextInt(pop.vehicles);
            if (tries++>pop.vehicles) return; //lazy abort
        }
        int rl1 = pop.customerOrder[v1].length; // route length
        int rl2 = pop.customerOrder[v2].length;

        int i1 = r.nextInt(rl1); //customer to move
        int i2 = r.nextInt(rl2); //position to move to

        int nrl1 = rl1-1; //new route length
        int nrl2 = rl2+1;

        int[] nr1 = new int[nrl1];//the new routes
        int[] nr2 = new int[nrl2];
        int i;
        for (i = 0; i < i1; i++) { //create the shorter new route
            nr1[i] = pop.customerOrder[v1][i];
        }
        for (i = i1; i < nrl1; i++) {
            nr1[i] = pop.customerOrder[v1][i+1];
        }

        for (i = 0; i < i2; i++) {//create the longer route
            nr2[i] = pop.customerOrder[v2][i];
        }
        nr2[i2] = pop.customerOrder[v1][i1];
        for (i = i2+1; i < nrl2; i++) {
            nr2[i] = pop.customerOrder[v2][i-1];
        }
        pop.customerOrder[v1] = nr1;
        pop.customerOrder[v2] = nr2;
    }

    // split a route into two routes - add a vehicle
    public static void MutateA(Pop pop, Random r, Bean bean){
        if(pop.vehicles==bean.totalVehicles){
            return;
        }
        int v = r.nextInt(pop.vehicles);
        int tries = 0;
        while(pop.customerOrder[v].length==1){
            v = r.nextInt(pop.vehicles);
            if (tries++>pop.vehicles) return; //lazy abort
        }
        int s = r.nextInt(pop.customerOrder[v].length-1); //last index of first route
        int[] r1 = new int[s+1];
        int[] r2 = new int[pop.customerOrder[v].length-s-1];
        int i;
        for (i = 0; i < s+1; i++) {
            r1[i] = pop.customerOrder[v][i];
        }
        for (i = s+1; i < pop.customerOrder[v].length; i++) {
            r2[i-s-1] = pop.customerOrder[v][i];
        }

        int[][] co = new int[pop.vehicles+1][];
        for ( i = 0; i < v; i++) {
            co[i] = pop.customerOrder[i];
        }
        co[i] = r1;
        for ( i = v+1; i < pop.vehicles; i++) {
            co[i] = pop.customerOrder[i];
        }
        co[i] = r2;
        pop.customerOrder = co;
        pop.vehicles++;
    }

    // concatenate two routes into one route - remove a vehicle
    public static void MutateR(Pop pop, Random r, Bean bean){
        if(pop.vehicles==bean.minVehicles){
            return;
        }
        int v1 = r.nextInt(pop.vehicles);
        int v2 = r.nextInt(pop.vehicles);
        int tries = 0;
        while(v1==v2 ||  pop.customerOrder[v1].length == 1){
            v1 = r.nextInt(pop.vehicles);
            if (tries++>pop.vehicles) return; //lazy abort
        }
        //TODO calculate current capacity here to merge smaller routes???

        int vl1 = pop.customerOrder[v1].length;
        int vl2 = pop.customerOrder[v2].length;

        int[] nr = new int[vl1+vl2]; //new route
        int i;
        for (i = 0; i < vl1; i++) {
            nr[i] = pop.customerOrder[v1][i];
        }
        for (i = 0; i < pop.customerOrder[v2].length; i++) {
            nr[vl1+i] = pop.customerOrder[v2][i];
        }

        if(v1>v2){
            int a = v1;
            v1 = v2;
            v2 = a;
        }
        int[][] co = new int[pop.vehicles-1][];
        for ( i = 0; i < v1; i++) {
            co[i] = pop.customerOrder[i];
        }
        co[i] = nr;
        for ( i = v1+1; i < v2; i++) {
            co[i] = pop.customerOrder[i];
        }
        for ( i = v2+1; i < pop.vehicles; i++) {
            co[i-1] = pop.customerOrder[i];
        }

        pop.customerOrder = co;
        pop.vehicles--;
    }
}
