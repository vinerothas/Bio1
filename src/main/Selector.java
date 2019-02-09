package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Selector {

    public static Pop[] select(Pop[] children, Pop[] parents){
        int sel = 4;
        if(sel==0){
            return elitist5050(children, parents);
        }else if(sel==1){
            //seems useless
            return fullReplacement(children, parents);
        }else if(sel==2){
            return elitist10percent(children, parents);
        }else if(sel==3){
            return twoBest(children, parents);
        }else if(sel==4){
            return elitist5percent(children, parents);
        }else if(sel==5){
            return elitist1percent(children, parents);
        }
        return children;
    }

    public static Pop[] elitist5050(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        for(int i = 0; i<parents.length/2;i++){
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }
        return nextGen;
    }

    public static Pop[] fullReplacement(Pop[] children, Pop[] parents){
        return children;
    }

    //take the 10% best pops, rest is random from both generations
    public static Pop[] elitist10percent(Pop[] children, Pop[] parents){
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

    //take the 5% best pops, rest is random from both generations
    public static Pop[] elitist5percent(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        int i;
        for(i = 0; i<parents.length/40;i++){
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }
        if((float)i!=(float)parents.length/40){
            i = parents.length/40;
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }else{
            i = i*2;
        }
        Pop[] newArray = Util.concatenate(Arrays.copyOfRange(parents, parents.length/20, parents.length),
                Arrays.copyOfRange(children, children.length/20, children.length));
        List<Pop> list = Arrays.asList(newArray);
        Collections.shuffle(list);
        for(; i<parents.length;i++){
            nextGen[i] = list.get(i);
        }
        return nextGen;
    }

    //take the 1% best pops, rest is random from both generations
    public static Pop[] elitist1percent(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        int i;
        for(i = 0; i<parents.length/200;i++){
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }
        if((float)i!=(float)parents.length/200){
            i = parents.length/200;
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
            i = i*2+2;
        }else{
            i = i*2;
        }
        Pop[] newArray = Util.concatenate(Arrays.copyOfRange(parents, parents.length/100, parents.length),
                Arrays.copyOfRange(children, children.length/100, children.length));
        List<Pop> list = Arrays.asList(newArray);
        Collections.shuffle(list);
        for(; i<parents.length;i++){
            nextGen[i] = list.get(i);
        }
        return nextGen;
    }

    public static Pop[] twoBest(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        nextGen[0] = parents[0];
        nextGen[1] = children[0];
        for(int i = 1; i<children.length;i++){
            nextGen[i] = children[i];
        }
        return nextGen;
    }

}
