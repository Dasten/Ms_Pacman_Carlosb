package pacman.controllers.Genetico;

import com.fuzzylite.Engine;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;
import java.util.Arrays;
import java.util.Random;


public class Genotipo {

    // Numero de evaluaciones necesarias para obtener el fitness del individuo (cantidad de veces que un individuo ejecutara el juego)
    static int NUM_EVALUACIONES = 5;

    protected float mFitness; // Fitness del individuo
    protected float mCromosoma[]; // Array de floats que simboliza el genotipo del individuo
    protected boolean evaluado; // Variable para saber si un individuo ha sido evaluado o no (hemos obtenido su fitness)

    // Fenotipo del individuo (de tipo Engine ya que nosotros usamos un controlador borroso (en el se encuentran todas las reglas y funciones de pertenencia)
    protected Engine mFenotipo;


    Genotipo(){
        mCromosoma = new float[AlgoritmoGenetico.NUM_CROMOSOMA];
        mFitness = 0.f;
        evaluado = false;
        mFenotipo = null;
    }

    // Funcion para obtener un Genotipo de forma aleatoria para un individuo
    public void randomizeCromosoma(){
        int randoms[];
        for (int j=0; j<36; j+=4){
            randoms = getRandomCromosomaForValue();
            for(int i=0; i<4; i++){
                mCromosoma[j+i] = randoms[i];
            }
        }
    }

    // Funcion para obtener el genotipo correspondiente al valor de un atributo de nuestro motor de logica borroso
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

    // Funcion para evaluar un individuo
    // Lo que hacemos es ejecutar el juego NUM_EVALUACIONES veces y obtenemos la puntuacion media, siendo esta el fitness del individuo
    public void evaluarGenotipo(){
        Executor exec = new Executor();
        ControladorFuzzyGen controlador = new ControladorFuzzyGen(this); // Creamos un controlador borroso y le pasamos nuestro individuo
        mFitness = (float)exec.runGenetico(controlador, new StarterGhosts(), NUM_EVALUACIONES); // Ejecutamos el juego en modo experiment sin interfaz para obtener la puntuacion media
        mFenotipo = controlador.getEngine(); // Le asignamos el fenotipo al individuo (el motor usado en el controlador borroso para su evaluacion)
        evaluado = true; // Seteamos a true la variable para saber que este individuo ya ha sido evaluado
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

    // Funcion para imprimir el cromosoma por pantalla (su genotipo y fitness)
    void printCromosoma(){
        System.out.print(this.toString());
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

    // Funcion para saber si el individuo ha sido evaluado o no
    public boolean isEvaluado(){
        return evaluado;
    }

    // Funcion para imprimir el Fenotipo por pantalla
    // Imprimimos tanto los puntos de las ecuaciones de la recta de cada valor para cada atributo como las reglas del motor de logica borrosa
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

    // Funcion que devuelve formateado en una cadena el genotipo de un individuo
    public String getGenotipoFormateado(){
        String formato = "";
        for(int i = 0; i < mCromosoma.length; i++){
            formato += mCromosoma[i] + " ";
        }
        return formato;
    }

}
