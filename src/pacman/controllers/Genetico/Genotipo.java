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
        evaluado = false;
    }

    public void randomizeCromosoma(){
        int randoms[];
        for (int j=0; j<36; j+=4){
            randoms = getRandomCromosomaForValue();
            for(int i=0; i<4; i++){
                mCromosoma[j+i] = randoms[i];
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

    public void setFitness(float fitness)
    {
        mFitness = fitness;
    }

    public float getFitness(){
        return mFitness;
    }

    public int getGenCromosoma(int numGen){
        return mCromosoma[numGen];
    }

    public void setGenCromosoma(int numGen, int valorCromosoma){
        mCromosoma[numGen] = valorCromosoma;
    }

    public int getNumCromosoma(){
        return mCromosoma.length;
    }

    public int[] getmCromosoma() {
        return mCromosoma;
    }

    public void setmCromosoma(int[] mCromosoma) {
        this.mCromosoma = mCromosoma;
    }



    // Para mutar obtendremos un numero aleatorio de cromosomas (entre 1 y 36)
    // Se escogeran de forma aleatoria en el genotipo tantos cromosomas como numero hayamos obtenido
    // Se sustituiran por valores aleatorios entre 0 y 200
    public void mutar(){

        Random rand = new Random();
        int cantidadCromosomasAMutar = (rand.nextInt(35 - 0 + 1) + 0);

        for(int i = 0; i < cantidadCromosomasAMutar; i++){
            int indiceRandom = (rand.nextInt(35 - 0 + 1) + 0);
            int valorMutado = (rand.nextInt(200 - 0 + 1) + 0);
            mCromosoma[indiceRandom] = valorMutado;
        }
    }

    void printCromosoma(){
        for(int i = 0; i < mCromosoma.length; i++){
            System.out.print(mCromosoma[i] + " ");
        }
        System.out.print("\n");
    }

    public String toString(){
        String cromosoma = "";
        for(int i = 0; i < mCromosoma.length; i++){
            cromosoma += mCromosoma[i] + " ";
        }
        cromosoma += "\n";
        cromosoma += "Fitness: " + mFitness + "\n";
        return cromosoma;
    }

}
