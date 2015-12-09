package pacman.controllers.Genetico;

import pacman.Executor;
import pacman.controllers.examples.StarterGhosts;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.regex.PatternSyntaxException;

public class AlgoritmoGenetico {

    static int NUM_CROMOSOMA = 36; // Numero de cromosomas que tiene el genotipo de un individuo
    static int NUM_POBLACION = 50; // La poblacion tiene que ser SIEMPRE DE NUMEROS PARES
    static float PROB_MUTE = 0.1f; // Probabilidad de que se produzca una mutacion cuando nace un hijo (10%)
    static String FICHERO = "MejorIndividuo.txt"; // Nombre del fichero donde se guarda la informacion del mejor individuo

    ArrayList<Genotipo> mPoblacion; // Poblacion

    public AlgoritmoGenetico(int numPoblacion){

        mPoblacion = new ArrayList<Genotipo>();
        for(int i = 0; i < numPoblacion; i++){
            Genotipo individuo = new Genotipo();
            individuo.randomizeCromosoma();
            mPoblacion.add(individuo);
        }
    }

    // Funcion con la que evaluamos la generacion
    public void evaluarGeneracion() {

        for(int i = 0; i < mPoblacion.size(); i++){

            Genotipo individuo = mPoblacion.get(i);

            if(!individuo.isEvaluado()){
                individuo.evaluarGenotipo();
            }
        }
    }

    // Funcion para ordenar la poblacion segun su Fitness en orden ascendente
    public static void ordenarPoblacionByFitness(ArrayList<Genotipo> poblacion){
        Collections.sort(poblacion, new Comparator<Genotipo>() {
            @Override public int compare(Genotipo g1, Genotipo g2) {
                return Float.compare(g1.mFitness, g2.mFitness); // Ascending
            }

        });
    }

    // Funcion para reemplazar la generacion, quitamos los individuos con menor fitness (peores) y metemos la nueva generacion de individuos
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

    // Obtenemos una arraylist con los hijos de la nueva generacion
    public ArrayList<Genotipo> generarSiguienteGeneracion(){

        ArrayList<Genotipo> listaPadres = seleccionarIndividuos(); // Arraylist con los padres seleccionados para engendrar la nueva generacion
        ArrayList<Genotipo> nuevaGeneracion = new ArrayList<Genotipo>(); // Arraylist con los hijos, es decir, los elementos de la nueva generacion
        Random rand = new Random();

        for(int i = 0; i < listaPadres.size(); i+= 2){

            ArrayList<Genotipo> hijos = reproducir(listaPadres.get(i), listaPadres.get(i + 1)); //Creamos un hijo a partir de un Genotipo Padre y Madre

            // Si obtenemos un random menor que la probabilidad mutamos al hijo 1
            if(((rand.nextInt((100-0)+1) + 0) / 100.0f) <= PROB_MUTE){
                hijos.get(0).mutar();
            }

            // Si obtenemos un random menor que la probabilidad mutamos al hijo 2
            if(((rand.nextInt((100-0)+1) + 0) / 100.0f) <= PROB_MUTE){
                hijos.get(1).mutar();
            }

            // Metemos los hijos obtenidos en la lista el arraylist de la nueva generacion
            nuevaGeneracion.add(hijos.get(0));
            nuevaGeneracion.add(hijos.get(1));
        }

        return nuevaGeneracion;
    }


    // A la hora de cruzar los individuos lo hacemos usando un cruce aritmetico (Pag 46 de los apuntes)
    public ArrayList<Genotipo> reproducir(Genotipo padre, Genotipo madre){

        ArrayList<Genotipo> hijos = new ArrayList<Genotipo>();
        float r = 0.4f;
        Genotipo hijo0 = new Genotipo(); // Hijo 1
        Genotipo hijo1 = new Genotipo(); //  Hijo 2

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

    public int getNumPoblacion(){
        return mPoblacion.size();
    }

    public Genotipo getGenotipoOfIndividuo(int index){
        return mPoblacion.get(index);
    }

    // Funcion para seleccionar a los individuos de la poblacion que vamos a cruzar
    // Hacemos una seleccion por Torneo (Pag 35 de los apuntes)
    // Usamos solo la mitad de la poblacion para obtener una generacion nueva
    public ArrayList<Genotipo> seleccionarIndividuos(){

        Random rand = new Random();
        ArrayList<Genotipo> listaPadres = new ArrayList<Genotipo>(); // Lista con los elementos seleccionados para reproducirse
        ArrayList<Genotipo> copiaPobacion = (ArrayList<Genotipo>) mPoblacion.clone(); // Lista copia de la poblacion de donde vamos tomando los individuos

        // Vamos a cruzar a la pobacion cogiendo grupos de 2 individuos y sleccionando al de mayor fitness
        // por lo tanto la mitad de la poblacion seran padres
        int numIndividuosSelec = mPoblacion.size() / 2;

        // Comprobamos que la mitad de la poblacion son padres, ya que solo podemos reproducir los individuos de dos en dos
        if(numIndividuosSelec % 2 != 0){
            numIndividuosSelec--;
        }

        // Hacemos el torneo en grupos de 2 y nos quedamos con el mayor fitness
        // Los individuos que participan en el torneo se seleccionan de forma aleatoria en la poblacion
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

    // Funcion con la que obtenemos el Fitness mas alto de la poblacion
    public float getMaxFitnessPoblacion(){
        // Ordenamos los individuos de la poblacion segun su fitness de menor a mayor
        AlgoritmoGenetico.ordenarPoblacionByFitness(mPoblacion);

        // Devolvemos el fitness del ultimo individuo de la pobacion (n) que sera el de mejor fitness
        return (mPoblacion.get(mPoblacion.size() - 1).getFitness());
    }

    // Funcion para guardar en un archivo de texto un individuo que le pasamos por prametro
    // El individuo se guarda en el archivo con el nombre ARCHIVO (varibale cte de la clase)
    public static void  guardarIndividuo(Genotipo individuo) {

        String infoIndividuo = individuo.getGenotipoFormateado();

        try {
            File file = new File(FICHERO);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(infoIndividuo);
            bw.write(individuo.getFitness()+"");
            bw.close();

            System.out.println("Se ha guardado el mejor individuo de la poblacion en '" + FICHERO + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Funcion para cargar un individuo del archivo guardado en el archivo con nombre ARCHIVO (varibale cte de la clase)
    public static Genotipo cargarIndividuo() throws IOException {

        Genotipo individuoCargado = new Genotipo();
        String[] cromosoma = null;

        String genotipoLeido = null;
        File file = new File(FICHERO);
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            char[] chars = new char[(int) file.length()];
            reader.read(chars);
            genotipoLeido = new String(chars);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader !=null){reader.close();}
        }

        try {
            cromosoma = genotipoLeido.split("\\s+");
        } catch (PatternSyntaxException ex) {
            System.out.println("ERROR al parsear el cromosoma cargado.");
        }

        for(int i = 0; i < individuoCargado.mCromosoma.length; i++){
            individuoCargado.setGenCromosoma(i, Float.parseFloat(cromosoma[i]));
        }

        float fitness = Float.parseFloat(cromosoma[cromosoma.length - 1]);
        individuoCargado.setFitness(fitness);

        return individuoCargado;
    }

    // Funcion para ejecutar el juego pasandole al controlador un individuo en particular
    // El juego se ejecuta de forma visual
    public static void playGameIndividuo(Genotipo individuo){
        Executor exec = new Executor();
        ControladorFuzzyGen controlador = new ControladorFuzzyGen(individuo); // Controlador Fuzzy al que le pasamos un individuo para establecer la reglas borrosas segun su Genotipo
        int delay = 10; // Delay o velocidad con la que se ejecutara el juego
        exec.runGame(controlador, new StarterGhosts(), true, delay); // Llamada al juego
    }

    public static void main(String[] args) throws IOException {

        int numPoblacion = NUM_POBLACION;
        AlgoritmoGenetico poblacion = new AlgoritmoGenetico(numPoblacion);
        int numGeneraciones = 0;
        int numMaxGeneraciones = 30;
        float fitnessObjetivo = 1200f;
        float currentFitness = 0f;

        // Menu de acciones para el usuario
        System.out.println("Que quieres hacer: (Selecciona una opcion 0/1)");
        System.out.println("0 - Ejecutar el algoritmo genetico - Pobacion: " + numPoblacion);
        System.out.println("1 - Cargar el mejor individuo de la poblacion y lanzar el juego");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int opcionSeleccionada = 0;


        try {
            opcionSeleccionada = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(opcionSeleccionada == 0) {

            System.out.println("Iniciando el algoritmo genetico... (esto puede tardar mucho tiempo)");

            long start_time = System.nanoTime(); // Empezamoa contar el tiempo que tarda

            // Evlauamos la poblacion por primera vez antes de empezar a iterar
            poblacion.evaluarGeneracion();
            currentFitness = poblacion.getMaxFitnessPoblacion();

            //Condicion de parada, mientras que el fitness del mejor elemento de la poblacion no sea 600 o mayor, o si llegamos a 10 generaciones
            while ((numGeneraciones < numMaxGeneraciones) && (currentFitness < fitnessObjetivo)) {

                // Generamos la nueva generacion de genotipos (los hijos resultantes de los elemtnos crien)
                //System.out.println("Generamos nueva generacion...");
                ArrayList<Genotipo> nuevaGeneracion = poblacion.generarSiguienteGeneracion();

                // Ordenamos nuestra poblacion segun su Fitness
                //System.out.println("Ordenamos la generacion segun su fitness...");
                ordenarPoblacionByFitness(poblacion.getPoblacion());

                // Reemplazamos la generacionCreada (hijos) en la poblacion (quitamos los individuos con menos fitness)
                //System.out.println("Reemplazamos generacion...");
                poblacion.reemplazarGeneracion(nuevaGeneracion);

                //System.out.println("Evaluamos la generacion...");
                // Volvemos a evaluar a la generacion para obtener los fitness de los hijos creados
                poblacion.evaluarGeneracion();

                // Aumentamos la cantidad de generaciones creadas
                numGeneraciones++;

                // Cambiamos el currentFitness al del mejor de la poblacion actual
                currentFitness = poblacion.getMaxFitnessPoblacion();

                // Informacion de la ejecucion del algoritmo genetico
                System.out.println("Generacion: " + numGeneraciones + " Mejor Fitness: " + currentFitness);
            }

            // Tiempo que hemos tardado en ejecutar el algoritmo genetico
            long end_time = System.nanoTime();
            double tiempo = (((end_time - start_time)/1e6)/1000);
            System.out.printf("Tiempo: %.2f segundos%n", tiempo);

            // El mejor individuo de la poblacion (el de mayor fitness)
            Genotipo mejorIndividuo = poblacion.getGenotipoOfIndividuo(numPoblacion - 1);
            System.out.println("El mejor individuo de la poblacion es: ");
            mejorIndividuo.printCromosoma();
            mejorIndividuo.printFenotipo();

            // Guardamos el individuo en un fichero para recuperarlo posteriormente
            guardarIndividuo(mejorIndividuo);

            // Le preguntamos al usuario si quiere lanzar el juego con el mejor individuo de la poblacion
            System.out.println("Quieres lanzar el juego con el mejor individuo de la poblacion? s/n");
            String respuesta = reader.readLine();

            if(respuesta.equals("s")){
                playGameIndividuo(mejorIndividuo);
            }

        }else{

            // Cargamos el individuo del fichero y lanzamos el juego en modo visual
            Genotipo mejorIndividuo = cargarIndividuo();
            System.out.println("El mejor individuo de la poblacion es: ");
            mejorIndividuo.printCromosoma();
            playGameIndividuo(mejorIndividuo);

        }
    }
}
