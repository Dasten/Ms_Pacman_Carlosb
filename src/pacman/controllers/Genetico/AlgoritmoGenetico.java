package pacman.controllers.Genetico;


import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class AlgoritmoGenetico {

    static int NUM_CROMOSOMA = 36;
    static int NUM_POBLACION = 60; // La poblacion tiene que ser SIEMPRE DE NUMEROS PARES

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

        for(int i = 0; i < mPoblacion.size(); i++){

            Genotipo individuo = mPoblacion.get(i);

            if(!individuo.isEvaluado()){
                individuo.evaluarGenotipo();
            }
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
        float probMutacion = 0.1f;
        Random rand = new Random();

        for(int i = 0; i < listaPadres.size(); i+=2){
            Genotipo[] hijos = reproducir(listaPadres.get(i), listaPadres.get(i+1));

            // Si obtenemos un random menor que la probabilidad mutamos al hijo 1
            if(((rand.nextInt((100-0)+1) + 0) / 100.0f) <= probMutacion){
                hijos[0].mutar();
            }

            // Si obtenemos un random menor que la probabilidad mutamos al hijo 2
            if(((rand.nextInt((100-0)+1) + 0) / 100.0f) <= probMutacion){
                hijos[1].mutar();
            }

            nuevaGeneracion.add(hijos[0]);
            nuevaGeneracion.add(hijos[1]);
        }

        return nuevaGeneracion;
    }

    // A la hora de Cruzar los individuos usamos una mascara de bits y combinamos los genotipos de los padres
    public Genotipo[] reproducir(Genotipo padre, Genotipo madre){

        Genotipo[] hijos = new Genotipo[2];
        float r = 0.4f;

        // Creamos los hijos por Cruce Aritmetico
        for(int i = 0; i < NUM_CROMOSOMA; i++){
            hijos[0].setGenCromosoma(i, (((padre.getGenCromosoma(i) * r) + (madre.getGenCromosoma(i) * (1-r)))));
            hijos[0].setGenCromosoma(i, (((padre.getGenCromosoma(i) * (1 - r)) + (madre.getGenCromosoma(i) * r))));
        }

        return hijos;


        // #####################
        /*
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
       */
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

        Random rand = new Random();
        ArrayList<Genotipo> listaPadres = new ArrayList<Genotipo>();

        ArrayList<Genotipo> copiaPobacion = (ArrayList<Genotipo>) mPoblacion.clone();

        // Vamos a cruzar a la pobacion cogiendo grupos de 2 individuos y sleccionando al de mayor fitness
        // por lo tanto la mitad de la poblacion seran padres
        int numIndividuosSelec = mPoblacion.size() / 2;

        for(int i = 0; i < numIndividuosSelec; i++){
            Genotipo individuo1 = copiaPobacion.remove((rand.nextInt((copiaPobacion.size() - 1) - 0 + 1) + 0));
            Genotipo individuo2 = copiaPobacion.remove((rand.nextInt((copiaPobacion.size() - 1) - 0 + 1) + 0));

            //Metemos en la lista de los padres al individuo con mejor fenotipo
            if(individuo1.getFitness() >= individuo2.getFitness()){
                listaPadres.add(individuo1);
            }else{
                listaPadres.add(individuo2);
            }
        }

        return listaPadres;

        // ####################33 ESTO TENGO QUE AMBIARLO POR EL METODO DE LAS DIAPOS (HACIENDO LA MEDIA)
        /*
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
    */
    }

    public ArrayList<Genotipo> getPoblacion() {
        return mPoblacion;
    }

    public void setPoblacion(ArrayList<Genotipo> mPoblacion) {
        this.mPoblacion = mPoblacion;
    }

    public float getMaxFitnessPoblacion(){
        // Ordenamos los individuos de la poblacion segun su fitness de menor a mayor
        AlgoritmoGenetico.ordenarPoblacionByFitness(mPoblacion);

        // Devolvemos el fitness del ultimo individuo de la pobacion (n) que sera el de mejor fitness
        return (mPoblacion.get(mPoblacion.size() - 1).getFitness());
    }

    public static void main(String[] args ) {


        int numPoblacion = NUM_POBLACION;
        AlgoritmoGenetico poblacion = new AlgoritmoGenetico(numPoblacion);
        int numGeneraciones = 0;
        float fitnessObjetivo = 600f;

        // Evlauamos la poblacion por primera vez antes de empezar a iterar
        poblacion.evaluarGeneracion();

        //Condicion de parada, mientras que el fitness del mejor elemento de la poblacion no sea 600 o mayor, o si llegamos a 10 generaciones
        while ( (numGeneraciones < 10) || (poblacion.getMaxFitnessPoblacion() < fitnessObjetivo)){




            ArrayList<Genotipo> nuevaGeneracion = poblacion.generarSiguienteGeneracion();

                // Ordenamos la poblacion segun su fitness
                ordenarPoblacionByFitness(Poblacion);

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
        */









        }






        // Ejecutamos un numero nControlador de veces el juego pasandole cada uno de los individuos de la poblacion
        for(int i = 0; i < poblacion.getNumPoblacion(); i++){

        }


        /*
        ordenarPoblacionByFitness(poblacion.getPoblacion());
        for(int i = 0; i < poblacion.getNumPoblacion(); i++){
            System.out.println("Individuo " + i + " Fitness: " + poblacion.getGenotipoOfIndividuo(i).getFitness());
        }
        */


    }
}
