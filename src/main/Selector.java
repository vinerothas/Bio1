package main;
/*
import java.util.*;

public class Selector {


    double slice;
    double halfSlice;
    double reverseSlice;
    double reverseHalfSlice;
    int reverseSliceInt;
    int reverseHalfSliceInt;
    Bean bean;
    Random r;

    public Selector(int popSize, Bean bean, Random r){
        slice = popSize/(100/ Param.elitismPercent);
        halfSlice = slice/2;
        reverseSlice = popSize-slice;
        reverseHalfSlice = reverseSlice/2;
        reverseSliceInt = (int)reverseSlice;
        reverseHalfSliceInt = (int)reverseHalfSlice;
        this.bean = bean;
        this.r = r;
    }

    public Pop[] select(Pop[] children, Pop[] parents){
        int sel = 5;
        if(sel==1){
            return elitistXpercent(children, parents);
        }else if(sel==2){
            return elitistChildren(children, parents);
        }else if(sel==3){
            return elitistUniqueChildren(children, parents);
        }else if(sel==4){
            return elitistUnique(children, parents);
        }else if(sel==5){
            return roulette2(children, parents);
        }
        return children;
    }


    public Pop[] roulette2(Pop[] children, Pop[] parents){
        Pop[] nextGen = new Pop[parents.length];

        Pop[] newArray = Util.concatenate(Arrays.copyOfRange(parents, 0, parents.length),
                Arrays.copyOfRange(children, 0, children.length));
        List<Pop> list = Arrays.asList(newArray);
        Collections.sort(list);
        LinkedList ll = new LinkedList(list);

        ListIterator itr = ll.listIterator();
        nextGen[0] = (Pop)itr.next();
        Pop pop2 = nextGen[0];
        itr.previous();
        itr.remove();

        Pop pop;
        int k = 1;
        while(k<nextGen.length && !ll.isEmpty()){
            itr = ll.listIterator();
            int i = 0;
            while(itr.hasNext()){
                itr.next();
                if(r.nextDouble() >= ++i/(double)parents.length){
                    pop = (Pop)itr.previous();
                    if(pop.fitness-nextGen[k-1].fitness>Param.fitnessComparison) {
                        nextGen[k++] = pop;
                    }
                    itr.remove();
                }
                if(k==nextGen.length)break;
            }
        }
        while(k<nextGen.length){
            for (int i = 0; i < k-1; i++) {
                pop = Crosser.crossPMX(nextGen[i],nextGen[i+1],bean, r);
                nextGen[k++] = pop;
                if(k==nextGen.length)break;
            }
        }

        Arrays.sort(nextGen, new SortPop());
        return nextGen;
    }

    //take the X% best pops, rest is random from both generations
    public Pop[] elitistXpercent(Pop[] children, Pop[] parents){
        Arrays.sort(children, new SortPop());
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
        Arrays.sort(nextGen, new SortPop());
        return nextGen;
    }

    //take the X% best unique pops, rest is in order unique from children
    // if leftover place, generate random
    public Pop[] elitistUnique(Pop[] children, Pop[] parents){
        Arrays.sort(children, new SortPop());
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

        Pop[] newArray = Util.concatenate(Arrays.copyOfRange(parents, (int)slice, parents.length),
                Arrays.copyOfRange(children, (int)slice, children.length));
        List<Pop> list = Arrays.asList(newArray);
        Collections.shuffle(list);

        Pop pop;
        for(i= 0; i<list.size() && k<nextGen.length;i++){
            pop = list.get(i);
            if(pop.fitness-lastChild.fitness>Param.fitnessComparison){
                nextGen[k++] = pop;
                lastChild = pop;
            }
        }

        for (; k < nextGen.length; k++) {
            nextGen[k] = new Pop(r,bean);
        }
        Arrays.sort(nextGen,new SortPop());
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
        Arrays.sort(children, new SortPop());
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
        Arrays.sort(nextGen,new SortPop());
        return nextGen;
    }


    class SortPop implements Comparator<Pop> {
        public int compare(Pop a, Pop b) {
            if ( a.fitness > b.fitness ) return 1;
            else if ( a.fitness == b.fitness ) return 0;
            else return -1;
        }
    }
}
*/