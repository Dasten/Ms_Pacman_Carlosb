package pacman.controllers.QLearning;

import pacman.game.Game;

public class QLearner {

    private QTabla qvalores;

    private long updates = 0;

    public QLearner(){

        int numEstado = 19;
        int numAcciones = 3;

        qvalores = new QTabla(numEstado, numAcciones);
        updates =0;
    }

    public void initialize(){
        qvalores.init();
        updates=0;
    }

    public Estado getState(Game game){
        return new Estado(game);
    }

    public Accion getAccion(Estado e){
        return qvalores.getAccion(e);
    }

    public Accion getMejorAccion(Estado e){
        return qvalores.getMejorAccion(e);
    }

    public void update(Estado e, Accion a, float recompensa, Estado ePrima){
        qvalores.update(e, a, ePrima, recompensa);
        updates++;
    }

    public String toString(){
        String resultado = String.format("Actualizaciones: %d\n%s", updates,qvalores.toString());
        return resultado;
    }




}
