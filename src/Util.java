import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Util {

    //Fisher–Yates shuffle
    //https://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
    static void shuffleArray(int[] ar) {
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    // Euclidean distance
    static float edist(int x1, int y1, int x2, int y2){
        return (float)Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    }
}