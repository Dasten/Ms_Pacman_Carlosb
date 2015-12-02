package pacman.controllers.Genetico;


import java.util.ArrayList;

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

        int n = 50;
        AlgoritmoGenetico poblacion = new AlgoritmoGenetico(n);
        int generationCount = 0;

        for(int i = 0; i < poblacion.getNumPoblacion(); i++){
            System.out.print("Individuo " + i + " genotipo: " + poblacion.getGenotipoOfIndividuo(i).toString());
        }



    }
}
