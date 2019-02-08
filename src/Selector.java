import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Selector {

    public static Pop[] select(int sel, Pop[] children, Pop[] parents){
        if(sel==0){
            return selection_elitist5050(children, parents);
        }else if(sel==1){
            return selectionFullReplacement(children, parents);
        }else if(sel==2){
            return selection_elitist10percent(children, parents);
        }
        return children;
    }

    public static Pop[] selection_elitist5050(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        for(int i = 0; i<parents.length/2;i++){
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }
        return nextGen;
    }

    public static Pop[] selectionFullReplacement(Pop[] children, Pop[] parents){
        return children;
    }

    //take the 10% best pops, rest is random from both generations
    public static Pop[] selection_elitist10percent(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        for(int i = 0; i<parents.length/20;i++){
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }
        Pop[] newArray = Util.concatenate(Arrays.copyOfRange(parents, parents.length/10, parents.length),
                Arrays.copyOfRange(children, children.length/10, children.length));
        List<Pop> list = Arrays.asList(newArray);
        Collections.shuffle(list);
        for(int i = parents.length/10; i<parents.length;i++){
            nextGen[i] = list.get(i);
        }
        return nextGen;
    }

}
