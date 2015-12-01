package pacman.controllers.Genetico;


import java.util.ArrayList;

public class AlgoritmoGenetico {

    static int CROMOSOMA_TAMANO = 32;
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

}
