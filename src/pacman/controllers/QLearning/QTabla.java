package pacman.controllers.QLearning;

public class QTabla {

    private static final float alpha = 0.3f;
    private static final float gamma = 0.9f;

    private float[][] qvalores;
    private int numEstados;
    private int numAcciones;

    private String [] listaEstados = {"CPH", "CPN", "CNH", "CNN", "CMH", "CMN",
            "NPH", "NPN", "NNH", "NNN", "NMH", "NMN",
            "LPH", "LPN", "LNH", "LNN", "LMH", "LMN",
            "XXX"};

    public QTabla(int numEstados, int numAcciones){
        this.numEstados = numEstados;
        this.numAcciones = numAcciones;
        qvalores = new float[numEstados][numAcciones];
    }

    public void init(){
        for (int i=0; i<numEstados; i++){
            for (int j=0; j<numAcciones; j++){
                qvalores[i][j]=0;
            }
        }
    }

    public float update(Estado e, Accion a, Estado ePrima, float recompensa){
        int estado = e.getIdEstado();
        int accion = a.getIdAccion();

        int mejorAccion = getMejorAccion(ePrima).getIdAccion();
        qvalores[estado][accion] = (1 - alpha) * qvalores[estado][accion] + alpha * (recompensa + gamma * qvalores[ePrima.getIdEstado()][mejorAccion]);

        return qvalores[estado][accion];
    }

    public Accion getMejorAccion(Estado e) {

        Accion mejor = null;
        float maximo = 0.0f;

        for(int i = 0; i < numAcciones; i++){
            if(mejor == null) {
                mejor = Accion.getAccionById(i);
                maximo = qvalores[e.getIdEstado()][i];
            }else if (qvalores[e.getIdEstado()][i] > maximo){
                mejor = Accion.getAccionById(i);
                maximo = qvalores[e.getIdEstado()][i];
            }
        }
        return mejor;
    }

    public float QValor(Estado e, Accion a){
        return qvalores[e.getIdEstado()][a.getIdAccion()];
    }

    // To string de la tabla para imprimirla por pantalla
    public String toString(){

        String resultado = "################  - QVALORES -  ################\n";
        for(int estado = 0; estado < qvalores.length; estado++){
            //resultado += String.format("%-3d :", estado);
            resultado += listaEstados[estado];
            for(int accion = 0; accion < qvalores[0].length; accion++){
                resultado += String.format("% 5.2f ", qvalores[estado][accion]);
            }
            resultado += "\n";
        }
        return resultado;
    }

}