package main;

import java.util.Arrays;
import java.util.Random;

public class Crosser {

    public static Pop cross(Pop mom, Pop dad, Bean bean, Random r){
        return new Pop(mom.customerOrder, dad.vehicles);
    }


    public static Pop crossPMX(Pop mom, Pop dad, Bean bean, Random r){
        int [] customerOrder = pmx(mom.customerOrder,dad.customerOrder,r);
        return new Pop(customerOrder, mom.vehicles);
    }

    /*  1.  Choose  two crossover  points  at  random,  and  copy the  segment  between
        them from the first  parent (PI) into the first  offspring.
        2.  Starting from  the first  crossover point look for  elements in that segment
        of the second parent (P2)  that have not been copied.
        3.  For each of these (say i), look in the offspring to see what element (say j)
        has been copied in its place from  PI.
        4.  Place i into the position occupied by j  in P2, since we know that we will not
        be putting j  there (as we  already have it in our string).
        5.  If the place occupied by j  in P2 has already been filled in the offspring by
        an element k,  put i  in the position occupied by k in P2.
        5.  Having dealt with the elements from the crossover segment, the rest of the
        offspring can be filled from P2, and the second child is created analogously
        with the parental roles reversed. */
    private static int[] pmx(int[] p1, int[] p2, Random r){
        int[] c = new int[p1.length];
        Arrays.fill(c, -1);
        int cp1 = r.nextInt(p1.length);
        int cp2 = r.nextInt(p1.length);
        while(cp1==cp2){
            cp2 = r.nextInt(p1.length);
        }
        if(cp2<cp1){
            int a = cp2;
            cp2 = cp1;
            cp1 = a;
        }
        System.arraycopy(p1,cp1, c,cp1,cp2-cp1);
        for (int k = cp1; k <cp2; k++) {
            int i = p2[k];
            boolean present = false;
            for (int l = cp1; l < cp2 ; l++) {
                if(c[l]==i){
                    present = true;
                    break;
                }
            }
            if (!present){
                int j = p1[k];
                place(i,j,cp1,cp2,p1,p2,c);
            }
        }
        for (int i = 0; i < c.length; i++) {
            if(c[i]==-1) c[i] = p2[i];
        }

        return c;
    }

    private static void place(int i, int j, int cp1, int cp2, int[] p1, int[] p2, int c[]){
        for (int l = 0; l < cp1 ; l++) {
            if(p2[l]==j){
                c[l] = i;
                return;
            }
        }
        for (int l = cp1; l < cp2; l++) {
            if(p2[l]==j){
                place(i,p1[l],cp1,cp2,p1,p2,c);
                return;
            }
        }
        for (int l = cp2; l < c.length ; l++) {
            if(p2[l]==j){
                c[l] = i;
                return;
            }
        }
    }

}
