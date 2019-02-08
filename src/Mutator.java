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
        while(v1==v2 ||  pop.customerOrder[v1].length == 1){
            v1 = r.nextInt(pop.vehicles);
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


    public static void MutateV(Pop pop, Random r, Bean bean){
        boolean positive = r.nextBoolean();
        if(positive){
            if(pop.vehicles != bean.totalVehicles){
                pop.vehicles++;
            }
        }else{
            if(pop.vehicles != bean.minVehicles){
                pop.vehicles--;
            }
        }
    }
}
