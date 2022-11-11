package curvefitting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class smoothcurve {
    private int numberOfPoints;
    private int degree;
    private int numberOfCoef;
    private double max = 10;
    private double min = -10;
    private int populationNum;//it repreasent number of chromosomes created
    private int t = 1;
    private int maxNumberofGeneration;
    private double crossoverProp = 0.6;
    private double mutationProp=1;
    private ArrayList<ArrayList<Double>> generation = new ArrayList<>();
    private ArrayList<point> arrayOfPoints = new ArrayList<point>();
    private ArrayList<ArrayList<Double>> parents = new ArrayList<>();
    private ArrayList<ArrayList<Double>> newOffSpring = new ArrayList<>();
    ArrayList<ArrayList<Double>> offsprings = new ArrayList<>();
    Random r = new Random();

    public smoothcurve() {

    }

    public smoothcurve(int numberOfPoints, int degree) {
        this.numberOfPoints = numberOfPoints;
        this.degree = degree;
        this.numberOfCoef = degree + 1;
        this.populationNum = 10;//r.nextInt(50-numberOfPoints)+numberOfPoints;//generate random number between popluation num as min and 100
        this.maxNumberofGeneration = populationNum * 100;
    }

    public void initializeGeneration() {
        for (int i = 0; i < populationNum; i++) {
            ArrayList<Double> gen = new ArrayList<>();//chromosome
            double num;
            for (int j = 0; j < numberOfCoef; j++) {//generate gene
                num = (Math.random() * (max - min)) + min;//random double
                gen.add(num);//add the created gene in the choromosome
            }
            generation.add(gen);//add the chromosome to generation
        }

    }

    public void printGeneration() {
        System.out.println(generation.toString());
    }

    public void addPoint(point p) {
        arrayOfPoints.add(p);
    }

    public void mutation(){
        double lb=-10, up=10, dlb ,dub, r1 , r ,b=1 ,y, dty, newx;
        for(int i=0; i<offsprings.size();i++)
        {
            for(int j=0; j<numberOfCoef ;j++) {

                double randomMutation=Math.random();
                if(randomMutation<mutationProp)
                {
                    dlb= offsprings.get(i).get(j)-lb;
                    dub= up-offsprings.get(i).get(j);
                    r1=Math.random();
                    if(r1<=0.5)
                    {
                        y=dlb;
                    }
                    else{
                        y=dub;
                    }
                    r=Math.random();
                    dty=y*(1-Math.pow(r, Math.pow((1-t/maxNumberofGeneration),b)));
                    if(r1<=0.5)
                    {
                        newx=offsprings.get(i).get(j)-dty;
                        offsprings.get(i).set(j,newx);
                    }
                    else{
                        System.out.println("before = "+ offsprings.get(i).get(j));
                        newx=offsprings.get(i).get(j)+dty;
                        offsprings.get(i).set(j,newx);
                        System.out.println("after = "+ offsprings.get(i).get(j));
                    }
                }
            }
        }
    }

    public void crossover() {
        int crosspoint1, crosspoint2;
        System.out.println("number of coff ="+numberOfCoef);

        crosspoint1 = ThreadLocalRandom.current().nextInt(1,numberOfCoef-2);
        System.out.println("crosspoint1 = " + crosspoint1);

        crosspoint2 = ThreadLocalRandom.current().nextInt((crosspoint1+1), (numberOfCoef ));
        System.out.println("crosspoint2 = " + crosspoint2);

        for (int i = 0; i < parents.size() - 1; i += 2) {
            System.out.println("this is parents 1");
            System.out.println(parents.get(i).toString());
            System.out.println("this is parents 2");
            System.out.println(parents.get(i + 1).toString());
            double randomselction = Math.random();
            if (randomselction <= crossoverProp) {
                ArrayList<Double> offspringtemp1 = new ArrayList<>();
                ArrayList<Double> offspringtemp2 = new ArrayList<>();
                for (int j = 0; j < crosspoint1; j++) {

                    System.out.print(" this is 1 "+ parents.get(i).get(j));
                    System.out.print(" this is 2 "+ parents.get(i+1).get(j));
                    offspringtemp1.add( parents.get(i).get(j));
                    offspringtemp2.add( parents.get(i + 1).get(j));
                    System.out.println("");

                }
                for (int j = crosspoint1; j <crosspoint2; j++) {
                    System.out.print(" this is a "+ parents.get(i+1).get(j));
                    System.out.print(" this is b "+ parents.get(i).get(j));
                    offspringtemp1.add(parents.get(i + 1).get(j));
                    offspringtemp2.add( parents.get(i).get(j));
                    System.out.println("");

                }
                for (int j = crosspoint2; j < numberOfCoef; j++) {
                    System.out.print(" this is i "+ parents.get(i).get(j));
                    System.out.print(" this is ii "+ parents.get(i+1).get(j));
                    offspringtemp1.add(parents.get(i).get(j));
                    offspringtemp2.add(parents.get(i + 1).get(j));
                    System.out.println("");

                }
//                System.out.println("this is after cross over offspring 1 = "+offspringtemp1.toString());
//                System.out.println("this is after cross over offspring 2 = "+offspringtemp2.toString());
                offsprings.add(offspringtemp1);
                offsprings.add(offspringtemp2);
            }
            else {
                offsprings.add(parents.get(i));
                offsprings.add(parents.get(i + 1));
            }


        }

    }




    public void selection(HashMap<ArrayList,Double> chromoFit){//send hashmap with choromosomes and it's fitness
/*
     //   ArrayList<ArrayList<Double>> copy = (ArrayList<ArrayList<Double>>)generation.clone();

        int indexofmx=0;
        int range1, range2;
        //Random random = new Random();
        for(int k=0; k<chromoFit.size();k++) {
            System.out.println("chromo fit"+ k +" " + chromoFit.get(generation.get(k)).toString());
        }

        for(int i=0; i<chromoFit.size()/2;i++)//select the half best chromosomes from population
        {
            range1 = ThreadLocalRandom.current().nextInt(0, generation.size());
            range2 = ThreadLocalRandom.current().nextInt(0, range1);
            double mx = chromoFit.get(generation.get(range2));
            for (int j = range2; j < range1; j++) {//get max and it's index

                if (chromoFit.get(generation.get(j)) > mx) {
                    mx = chromoFit.get(generation.get(j));
                    indexofmx = j;
                }
            }
            mx = chromoFit.get(generation.get(range2));
            parents.add(generation.get(indexofmx));//add the max value of the tourmenent to parents
            generation.remove(indexofmx);//remove the lightest fitnees from generation to get the next fitness
            System.out.println(parents.get(i));
        }*/
        for(int j=0;j<chromoFit.size() ; j++) {
            int range1 = ThreadLocalRandom.current().nextInt(0, generation.size() / 2);
            int range2 = ThreadLocalRandom.current().nextInt(generation.size() / 2, generation.size());
            double maxfit = chromoFit.get(generation.get(range1));
            int maxindex = range1;
            for (int i = range1; i < range2; i++) {
                if (chromoFit.get(generation.get(i)) > maxfit) {
                    maxfit = chromoFit.get(generation.get(i));
                    maxindex = i;
                }
            }
            parents.add(generation.get(maxindex));
        }
        for (ArrayList<Double> ind : parents) {
            System.out.println(ind);
        }

      /*  for(int k=0; k<chromoFit.size();k++) {
            System.out.println("chromo fit"+ k +" " + chromoFit.get(generation.get(k)).toString());
        }*/
     /*   for(int i=0; i< chromoFit.size()-1;i+=2)
        {
           if(chromoFit.get(generation.get(i)) >= chromoFit.get(generation.get(i+1)))
           {
               parents.add(generation.get(i));
           }
           else{
               parents.add(generation.get(i+1));
           }
           //System.out.println(parents.get(i));
        }
        for (ArrayList<Double> ind : parents) {
            System.out.println(ind);
        }*/

    }


    public HashMap<ArrayList, Double> fitness(ArrayList<ArrayList<Double>> genApha){
        HashMap<ArrayList, Double> chromoFit= new HashMap<>();
        for(int i=0; i<genApha.size(); i++)//each chromosome (key) has it's fitness
        {
            chromoFit.put(genApha.get(i), 1/calcFitness(genApha.get(i)));
            //System.out.println("this is chromo "+ genApha.get(i).toString() +" and it's fitness "+chromoFit.get(genApha.get(i)).toString());

        }
        //System.out.println("the size of chromoFIt = "+chromoFit.size());
        return chromoFit;
    }

    public double calcFitness(ArrayList<Double> chromo)//we send one chromo to get it's total fitness
    {
        double totalFitness=0;
        for(int j=0; j<arrayOfPoints.size();j++) {//iterate over the array of points to get a point
            double y=0;
            double aFitness=0;
            for (int i = 0; i < numberOfCoef; i++)//iterate over coef
            {
                y = chromo.get(i) * (Math.pow(arrayOfPoints.get(j).getX(), i));//return the actual coordinates of a point of x

            }
            aFitness=Math.abs(y-arrayOfPoints.get(j).getY());
            totalFitness+=aFitness;
        }
        return totalFitness/arrayOfPoints.size(); //avarage
    }
    void start(){
        initializeGeneration();
        HashMap<ArrayList,Double> chromoFit= new HashMap();
        //System.out.println("the size of generation is "+ generation.size());
        chromoFit= fitness(generation);
        selection(chromoFit);
        crossover();
        mutation();
    }


    public static void main(String[] args)  {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Number of datasets ");
        int numeberOfDataSets= myObj.nextInt();
        for(int j=0; j<numeberOfDataSets ; j++)
        {
            System.out.println("Number of data points and polynomial degree separated by space");
            int numberOfPoints= myObj.nextInt();
            int degree= myObj.nextInt();
            smoothcurve smoth= new smoothcurve(numberOfPoints,degree);
            smoth.initializeGeneration();
            smoth.printGeneration();
            for(int i=0; i<numberOfPoints;i++)
            {
                System.out.println("x-coordinate and y-coordinate separated by space");
                double x,y;
                x= myObj.nextDouble();
                y= myObj.nextDouble();
                point p1 = new point(x,y);
                smoth.addPoint(p1);
            }
            smoth.start();
        }




    }
}
