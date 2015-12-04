package pacman.controllers.Genetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class AlgoritmoGenetico {

    static int NUM_CROMOSOMA = 36;
    static int NUM_POBLACION = 10; // La poblacion tiene que ser SIEMPRE DE NUMEROS PARES

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

    public void reemplazarGeneracion(ArrayList<Genotipo> nuevaGeneracion){

        // Eliminamos los elementos de la generacion con peor fitness
        // (eliminamos tantos individuos como hijos hayamos creado)
        for(int i = 0; i < nuevaGeneracion.size(); i++){
            mPoblacion.remove(i);
        }

        // Ahora insertamos los nuevos individuos (hijos) en la poblacion
        for(int i = 0; i < nuevaGeneracion.size(); i++){
            mPoblacion.add(nuevaGeneracion.get(i));
        }

    }

    // Obtenemos una lista con los hijos de la nueva generacion
    public ArrayList<Genotipo> generarSiguienteGeneracion(){

        ArrayList<Genotipo> listaPadres = seleccionarIndividuos();
        ArrayList<Genotipo> nuevaGeneracion = new ArrayList<Genotipo>();
        float probMutacion = 0.1f;
        Random rand = new Random();

        for(int i = 0; i < listaPadres.size(); i+= 2){

            ArrayList<Genotipo> hijos = reproducir(listaPadres.get(i), listaPadres.get(i + 1));

            // Si obtenemos un random menor que la probabilidad mutamos al hijo 1
            if(((rand.nextInt((100-0)+1) + 0) / 100.0f) <= probMutacion){
                hijos.get(0).mutar();
            }

            // Si obtenemos un random menor que la probabilidad mutamos al hijo 2
            if(((rand.nextInt((100-0)+1) + 0) / 100.0f) <= probMutacion){
                hijos.get(1).mutar();
            }

            nuevaGeneracion.add(hijos.get(0));
            nuevaGeneracion.add(hijos.get(1));
        }

        return nuevaGeneracion;
    }


    // A la hora de Cruzar los individuos usamos una mascara de bits y combinamos los genotipos de los padres
    public ArrayList<Genotipo> reproducir(Genotipo padre, Genotipo madre){

        ArrayList<Genotipo> hijos = new ArrayList<Genotipo>();
        float r = 0.4f;
        Genotipo hijo0 = new Genotipo();
        Genotipo hijo1 = new Genotipo();

        // Creamos los hijos por Cruce Aritmetico
        for(int i = 0; i < NUM_CROMOSOMA; i++){
            float gen1 = (     (padre.getGenCromosoma(i) * r) +  (madre.getGenCromosoma(i) * (1 - r))     );
            float gen2 = (     (padre.getGenCromosoma(i) * (1 - r) + (madre.getGenCromosoma(i) * r))      );

            hijo0.setGenCromosoma(i, gen1);
            hijo1.setGenCromosoma(i, gen2);
        }

        hijos.add(hijo0);
        hijos.add(hijo1);

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

        Random rand = new Random();
        ArrayList<Genotipo> listaPadres = new ArrayList<Genotipo>();

        ArrayList<Genotipo> copiaPobacion = (ArrayList<Genotipo>) mPoblacion.clone();

        // Vamos a cruzar a la pobacion cogiendo grupos de 2 individuos y sleccionando al de mayor fitness
        // por lo tanto la mitad de la poblacion seran padres
        int numIndividuosSelec = mPoblacion.size() / 2;

        if(numIndividuosSelec % 2 != 0){
            numIndividuosSelec--;
        }

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
        int numMaxGeneraciones = 1;
        float fitnessObjetivo = 600f;
        float currentFitness = 0f;

        System.out.println("Evaluamos la primera generacion...");

        // Evlauamos la poblacion por primera vez antes de empezar a iterar
        poblacion.evaluarGeneracion();
        currentFitness = poblacion.getMaxFitnessPoblacion();

        System.out.println("Entramos en el while...");

        //Condicion de parada, mientras que el fitness del mejor elemento de la poblacion no sea 600 o mayor, o si llegamos a 10 generaciones
        while ((numGeneraciones < numMaxGeneraciones) && (currentFitness < fitnessObjetivo)){

            System.out.println("Generamos nueva generacion...");
            // Generamos la nueva generacion de genotipos (los hijos resultantes de los elemtnos crien)
            ArrayList<Genotipo> nuevaGeneracion = poblacion.generarSiguienteGeneracion();

            System.out.println("Ordenamos la generacion segun su fitness...");
            // Ordenamos nuestra poblacion segun su Fitness
            ordenarPoblacionByFitness(poblacion.getPoblacion());

            System.out.println("Reemplazamos generacion...");
            // Reemplazamos la generacionCreada (hijos) en la poblacion (quitamos los individuos con menos fitness)
            poblacion.reemplazarGeneracion(nuevaGeneracion);

            System.out.println("Evaluamos la generacion...");
            // Volvemos a evaluar a la generacion para obtener los fitness de los hijos creados
            poblacion.evaluarGeneracion();

            // Aumentamos la cantidad de generaciones creadas
            numGeneraciones++;

            currentFitness = poblacion.getMaxFitnessPoblacion();

            System.out.println("Generacion: " + numGeneraciones + " Mejor Fitness: " + currentFitness);
        }

        Genotipo mejorIndividuo = poblacion.getGenotipoOfIndividuo(numPoblacion- 1);
        System.out.println("El mejor individuo de la poblacion es: ");
        mejorIndividuo.printCromosoma();
        mejorIndividuo.printFenotipo();

        // TO-DO Guardar el genotipo en un txt para cargarlo posteriormente

    }
}
