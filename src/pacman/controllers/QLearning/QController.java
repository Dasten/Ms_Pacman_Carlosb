package pacman.controllers.QLearning;

import pacman.controllers.Controller;
import static pacman.game.Constants.*;
import pacman.game.Constants;
import pacman.game.Game;

import java.util.EnumMap;
import java.util.Random;

public class QController extends Controller <EnumMap<GHOST, MOVE>> {

    private final static float CONSISTENCY = 1.0f;	//carry out intended move with this probability
    private Random rnd = new Random();
    private EnumMap <GHOST, MOVE> myMoves = new EnumMap<Constants.GHOST,Constants.MOVE>(GHOST.class);
    private Constants.MOVE[] moves = MOVE.values();

    private MOVE siguienteMovimiento;

    public MOVE getSiguienteMovimiento() {
        return siguienteMovimiento;
    }

    public void setSiguienteMovimiento(MOVE siguienteMovimiento) {
        this.siguienteMovimiento = siguienteMovimiento;
    }

    //private Estado lastEstado = null;
    //private Accion lastAccion = null;

    //private QLearner learner;
/*
    public void setQLearner(QLearner ql){
        this.learner = ql;
    }

    public QLearner getQLearner(){
        return this.learner;
    }
*/



    public EnumMap<Constants.GHOST,Constants.MOVE> getMove(Game game,long timeDue)
    {

        //System.out.println("Dentro del controller");
        myMoves.clear();

        //MOVE siguienteMovimiento = MOVE.NEUTRAL;


        //e.init(0, 0);

        //Estado estado = learner.getState(game);
        //Accion accion = learner.getAccion(estado);
        //float r = 0;


        //e.executeMovement(a.asInt());
        //siguienteMovimiento = accion.getMovimientoFromAccion(game);

        //r=e.getActualReward();
        //r = estado.getRecompensa();

        //State s_prime=learner.getState(e);
        //Estado ePrima = learner.getState(game);

        //learner.update(s, a, r, s_prime);
         /*
        if(lastEstado != null){
            learner.update(lastEstado, lastAccion, r, estado);
        }
        */


        //s=s_prime;
        //lastEstado = estado;
        //lastAccion = accion;




        for(Constants.GHOST ghost : Constants.GHOST.values())
            if(game.doesGhostRequireAction(ghost))
            {
                // Si es SUE modificamos su comportamiento con el movimiento obtenido, sino los demas fantasmas se comportan como siempre
                if(ghost == Constants.GHOST.SUE)
                {
                    // ################################################################
                    //Setemos el movimiento final que va a hacer SUE

                    myMoves.put(ghost, siguienteMovimiento);


                    // #################################################################
                }else{
                    if(rnd.nextFloat()<CONSISTENCY)	//approach/retreat from the current node that Ms Pac-Man is at
                        myMoves.put(ghost,game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
                                game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost), Constants.DM.PATH));
                    else									//else take a random action
                        myMoves.put(ghost,moves[rnd.nextInt(moves.length)]);
                }
            }

        return myMoves;
    }

}
