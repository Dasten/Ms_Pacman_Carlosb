package pacman.controllers.Genetico;


import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

import java.util.ArrayList;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

public class AlgoritmoGenetico {

    static int NUM_CROMOSOMA = 36;
    static int NUM_POBLACION = 50;

    ArrayList<Genotipo> mPoblacion;

    public AlgoritmoGenetico(int numPoblacion){

        mPoblacion = new ArrayList<Genotipo>();
        for(int i = 0; i < numPoblacion; i++){
            Genotipo individuo = new Genotipo();
            individuo.randomizeCromosoma();
            mPoblacion.add(individuo);
        }
    }

    public void evaluarGeneracion() {

    }

    public void generarSiguienteGeneracion(){

    }

    public void reproducir(Genotipo padre, Genotipo madre){

    }

    int getNumPoblacion(){
        return mPoblacion.size();
    }

    public Genotipo getGenotipoOfIndividuo(int index){
        return mPoblacion.get(index);
    }


    public static void main( String[] args ) {

        Executor exec = new Executor();
        ControladorFuzzyGen controlador;

        int n = 50;
        AlgoritmoGenetico poblacion = new AlgoritmoGenetico(n);
        int generationCount = 0;
        int nControlador = 10;

        for(int i = 0; i < poblacion.getNumPoblacion(); i++){
            System.out.print("Individuo " + i + " genotipo: " + poblacion.getGenotipoOfIndividuo(i).toString());
        }


        // Ejecutamos un numero N de veces el juego pasandole cada uno de los individuos de la poblacion
        for(int i = 0; i < poblacion.getNumPoblacion(); i++){
            Genotipo individuo = poblacion.getGenotipoOfIndividuo(i);
            controlador = new ControladorFuzzyGen(individuo);
            individuo.setFitness((float)exec.runGenetico(controlador, new StarterGhosts(), nControlador));

        }



        //exec.runQLearner(new StarterPacMan(), new StarterGhosts());
    }
}
