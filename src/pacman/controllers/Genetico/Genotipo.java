package pacman.controllers.Genetico;

import com.fuzzylite.Engine;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;
import java.util.Arrays;
import java.util.Random;


public class Genotipo {

    static int NUM_EVALUACIONES = 5;

    protected float mFitness;
    protected float mCromosoma[];
    protected boolean evaluado;
    protected Engine mFenotipo;



    Genotipo(){
        mCromosoma = new float[AlgoritmoGenetico.NUM_CROMOSOMA];
        mFitness = 0.f;
        evaluado = false;
        mFenotipo = null;
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

    public float getGenCromosoma(int numGen){
        return mCromosoma[numGen];
    }

    public void setGenCromosoma(int numGen, float valorCromosoma){
        mCromosoma[numGen] = valorCromosoma;
    }

    public int getNumCromosoma(){
        return mCromosoma.length;
    }

    public float[] getmCromosoma() {
        return mCromosoma;
    }

    public void setmCromosoma(float[] mCromosoma) {
        this.mCromosoma = mCromosoma;
    }

    public void evaluarGenotipo(){
        Executor exec = new Executor();
        ControladorFuzzyGen controlador = new ControladorFuzzyGen(this);
        mFitness = (float)exec.runGenetico(controlador, new StarterGhosts(), NUM_EVALUACIONES);
        mFenotipo = controlador.getEngine();
        evaluado = true;
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

        System.out.print(this.toString());

        /*
        for(int i = 0; i < mCromosoma.length; i++){
            System.out.print(mCromosoma[i] + " ");
        }
        System.out.print("\n");
        */
    }

    public String toString(){
        String cromosoma = "Genotipo: ";
        for(int i = 0; i < mCromosoma.length; i++){
            cromosoma += mCromosoma[i] + " ";
        }
        cromosoma += "\n";
        cromosoma += "Fitness: " + mFitness + "\n";
        return cromosoma;
    }

    public boolean isEvaluado(){
        return evaluado;
    }

    public void printFenotipo(){
        System.out.println("Fenotipo: ");
        System.out.println("Distancia - CERCA - (" + mCromosoma[0] + "," + mCromosoma[1] + "," + mCromosoma[2] + "," + mCromosoma[3] + ")");
        System.out.println("Distancia - NORMAL - (" + mCromosoma[4] + "," + mCromosoma[5] + "," + mCromosoma[6] + "," + mCromosoma[7] + ")");
        System.out.println("Distancia - LEJOR - (" + mCromosoma[8] + "," + mCromosoma[9] + "," + mCromosoma[10] + "," + mCromosoma[11] + ")");

        System.out.println("Tiempo - CERCA - (" + mCromosoma[12] + "," + mCromosoma[13] + "," + mCromosoma[14] + "," + mCromosoma[15] + ")");
        System.out.println("Tiempo - NORMAL - (" + mCromosoma[16] + "," + mCromosoma[17] + "," + mCromosoma[18] + "," + mCromosoma[19] + ")");
        System.out.println("Tiempo - MUCHO - (" + mCromosoma[20] + "," + mCromosoma[21] + "," + mCromosoma[22] + "," + mCromosoma[23] + ")");

        System.out.println("DistanciaPowerPills - CERCA - (" + mCromosoma[24] + "," + mCromosoma[25] + "," + mCromosoma[26] + "," + mCromosoma[27] + ")");
        System.out.println("DistanciaPowerPills - NORMAL - (" + mCromosoma[28] + "," + mCromosoma[29] + "," + mCromosoma[30] + "," + mCromosoma[31] + ")");
        System.out.println("DistanciaPowerPills - LEJOS - (" + mCromosoma[32] + "," + mCromosoma[33] + "," + mCromosoma[34] + "," + mCromosoma[35] + ")");

        RuleBlock reglas = mFenotipo.getRuleBlock(0);
        for (Rule regla : reglas.getRules()) {
            System.out.println(regla.toString());
        }
    }

    public String getGenotipoFormateado(){
        String formato = "";
        for(int i = 0; i < mCromosoma.length; i++){
            formato += mCromosoma[i] + " ";
        }
        return formato;
    }

}
