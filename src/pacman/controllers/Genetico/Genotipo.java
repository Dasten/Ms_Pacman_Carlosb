package pacman.controllers.Genetico;


import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

public class Genotipo {


    protected float mFitness;
    protected int mCromosoma[];
    protected boolean evaluado;

    Genotipo(){
        mCromosoma = new int[AlgoritmoGenetico.NUM_CROMOSOMA];
        mFitness = 0.f;
    }

    public void randomizeCromosoma(){
        // code for randomization of initial weights goes HERE
        int numAtri = 3;
        int numValores = 3;
        int randoms[];

        for(int i = 0; i < numAtri; i++){
            for(int j = 0; j < numValores; j++)
            {
                randoms = getRandomCromosomaForValue();
                for(int k = 0; k < randoms.length; k++){
                    mCromosoma[i+j+k] = randoms[k];
                }
            }
        }
    }

    private int[] getRandomCromosomaForValue(){
        Random rand = new Random();
        int[] randomValores  = new int[4];
        int max = 200;
        int min = 0;

        for(int i = 0; i < 4; i++){
            randomValores[i] = (rand.nextInt(max - min + 1) + min);
        }

        Arrays.sort(randomValores);
        return randomValores;
    }

    void printCromosoma(){
        for(int i = 0; i < mCromosoma.length; i++){
            System.out.print(mCromosoma[i] + " ");
        }
    }

}
