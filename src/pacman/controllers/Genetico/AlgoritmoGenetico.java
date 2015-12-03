package pacman.controllers.Genetico;


import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

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
        ArrayList<Genotipo> nuevaGenracion = generarSiguienteGeneracion();

        // Ordenamos la poblacion segun su fitness
        ordenarPoblacionByFitness(mPoblacion);

        //Eliminamos tantos elementos de la poblacion como nuevos elementos hayamos genrado
        //Eliminamos los elementos con peor fitness de la poblacion
        for(int i = 0; i < nuevaGenracion.size(); i++){
            mPoblacion.remove(0);
        }

        // Sumamos uno a las veces que han evaluado los individuos que quedan
        for(int i = 0; i < mPoblacion.size(); i++){
            mPoblacion.get(i).evaluarGenotipo();
        }

        // Metemos la nueva generacion en la poblacion
        for(int i = 0; i < nuevaGenracion.size(); i++){
            mPoblacion.add(nuevaGenracion.get(i));
        }

    }

    public static void ordenarPoblacionByFitness(ArrayList<Genotipo> poblacion){
        Collections.sort(poblacion, new Comparator<Genotipo>() {
            @Override public int compare(Genotipo g1, Genotipo g2) {
                return Float.compare(g1.mFitness, g2.mFitness); // Ascending
            }

        });
    }

    // Obtenemos una lista con los hijos de la nueva generacion
    public ArrayList<Genotipo> generarSiguienteGeneracion(){

        ArrayList<Genotipo> listaPadres = seleccionarIndividuos();
        ArrayList<Genotipo> nuevaGeneracion = new ArrayList<Genotipo>();

        for(int i = 0; i < listaPadres.size(); i+=2){
            Genotipo[] hijos = reproducir(listaPadres.get(i), listaPadres.get(i+1));
            nuevaGeneracion.add(hijos[0]);
            nuevaGeneracion.add(hijos[1]);
        }

        return nuevaGeneracion;
    }

    // A la hora de Cruzar los individuos usamos una mascara de bits y combinamos los genotipos de los padres
    public Genotipo[] reproducir(Genotipo padre, Genotipo madre){
        int[] mascara = crearMascaraGenotipo();
        Genotipo[] hijos = new Genotipo[2];

        // Generamos el primer hijo con la mascara - si el cromosoma de la mascara es 1 cogemos el de la madre y si es 0 el del padre
        // Generamos el segundo hijo con la mascara - si el cromosoma de la mascara es 1 cogemos el del padre y si es 0 el de la madre
        for(int i = 0; i < NUM_CROMOSOMA; i++){
            if(mascara[i] == 1){
                hijos[0].setGenCromosoma(i, madre.getGenCromosoma(i));
                hijos[1].setGenCromosoma(i, padre.getGenCromosoma(i));
            }else{
                hijos[0].setGenCromosoma(i, padre.getGenCromosoma(i));
                hijos[1].setGenCromosoma(i, madre.getGenCromosoma(i));
            }
        }

        return hijos;
    }

    // Creamos una mascara aleatoria para el cruce de dos individuos
    private int[] crearMascaraGenotipo(){
        Random rand = new Random();
        int[] mascara = new int [NUM_CROMOSOMA];
        for(int i = 0; i < mascara.length; i++){
            mascara[i] = (rand.nextInt(1 - 0 + 1) + 0);
        }
        return mascara;
    }

    int getNumPoblacion(){
        return mPoblacion.size();
    }

    public Genotipo getGenotipoOfIndividuo(int index){
        return mPoblacion.get(index);
    }

    public ArrayList<Genotipo> seleccionarIndividuos(){
        int numIndividuosSelec = NUM_POBLACION / 4;
        if(numIndividuosSelec % 2 != 0){
            numIndividuosSelec--;
        }

        ArrayList<Genotipo> listaPadres = new ArrayList<Genotipo>();

        for(int i = 0; i < numIndividuosSelec; i++){
            listaPadres.add(mPoblacion.get(mPoblacion.size()-i));
            listaPadres.add(mPoblacion.get(i));
        }

        return listaPadres;
    }

    public ArrayList<Genotipo> getPoblacion() {
        return mPoblacion;
    }

    public void setPoblacion(ArrayList<Genotipo> mPoblacion) {
        this.mPoblacion = mPoblacion;
    }

    public static void main(String[] args ) {

        Executor exec = new Executor();
        ControladorFuzzyGen controlador;

        int numPoblacion = NUM_POBLACION;
        AlgoritmoGenetico poblacion = new AlgoritmoGenetico(numPoblacion);
        int numGeneraciones = 0;
        int nControlador = 10;





        // Ejecutamos un numero nControlador de veces el juego pasandole cada uno de los individuos de la poblacion
        for(int i = 0; i < poblacion.getNumPoblacion(); i++){
            Genotipo individuo = poblacion.getGenotipoOfIndividuo(i);
            controlador = new ControladorFuzzyGen(individuo);
            individuo.setFitness((float)exec.runGenetico(controlador, new StarterGhosts(), nControlador));
            System.out.println("Individuo " + i + " Genotipo: " + individuo.toString());
        }


        /*
        ordenarPoblacionByFitness(poblacion.getPoblacion());
        for(int i = 0; i < poblacion.getNumPoblacion(); i++){
            System.out.println("Individuo " + i + " Fitness: " + poblacion.getGenotipoOfIndividuo(i).getFitness());
        }
        */


    }
}
