package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Selector {


    double slice;
    double halfSlice;
    Bean bean;
    Random r;

    public Selector(int popSize, Bean bean, Random r){
        slice = popSize/(100/ Param.elitismPercent);
        halfSlice = slice/2;
        this.bean = bean;
        this.r = r;
    }

    public Pop[] select(Pop[] children, Pop[] parents){
        int sel = 5;
        if(sel==0){
            return elitist5050(children, parents);
        }else if(sel==1){
            //seems useless
            return fullReplacement(children, parents);
        }else if(sel==2){
            return twoBest(children, parents);
        }else if(sel==3){
            return elitistXpercent(children, parents);
        }else if(sel==4){
            return elitistChildren(children, parents);
        }else if(sel==5){
            return elitistUniqueChildren(children, parents);
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

    //take the X% best pops, rest is random from both generations
    public Pop[] elitistXpercent(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        int i;
        for(i = 0; i<halfSlice;i++){
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }
        if(i!=halfSlice){
            i = (int)halfSlice+1;
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
            i = i*2+2;
        }else{
            i = i*2;
        }
        Pop[] newArray = Util.concatenate(Arrays.copyOfRange(parents, (int)slice, parents.length),
                Arrays.copyOfRange(children, (int)slice, children.length));
        List<Pop> list = Arrays.asList(newArray);
        Collections.shuffle(list);
        for(; i<parents.length;i++){
            nextGen[i] = list.get(i);
        }
        return nextGen;
    }

    //take the X% best pops, rest is random from children
    public Pop[] elitistChildren(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        int i;
        for(i = 0; i<halfSlice;i++){
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
        }
        if(i!=halfSlice){
            i = (int)halfSlice+1;
            nextGen[i*2] = parents[i];
            nextGen[i*2+1] = children[i];
            i = i*2+2;
        }else{
            i = i*2;
        }
        Pop[] newArray = Arrays.copyOfRange(children, (int)slice, children.length);
        List<Pop> list = Arrays.asList(newArray);
        Collections.shuffle(list);
        for(int j=i; j<nextGen.length;j++){
            nextGen[j] = list.get(j-i);
        }
        return nextGen;
    }

    //take the X% best unique pops, rest is in order unique from children
    // if leftover place, generate random
    public Pop[] elitistUniqueChildren(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];
        int i;
        nextGen[0] = parents[0];
        nextGen[1] = children[0];
        int k = 2;
        Pop lastParent = parents[0];
        Pop lastChild = children[0];
        for(i = 1; i<halfSlice;i++){
            if(parents[i].fitness-lastParent.fitness>Param.fitnessComparison){
                nextGen[k++] = parents[i];
                lastParent = parents[i];
            }
            if(children[i].fitness-lastChild.fitness>Param.fitnessComparison){
                nextGen[k++] = children[i];
                lastChild = children[i];
            }
        }
        for(; i<children.length && k<nextGen.length;i++){
            if(children[i].fitness-lastChild.fitness>Param.fitnessComparison){
                nextGen[k++] = children[i];
                lastChild = children[i];
            }
        }
        for (; k < nextGen.length; k++) {
            nextGen[k] = new Pop(r,bean);
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
