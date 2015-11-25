package pacman.controllers.QLearning;

import static pacman.game.Constants.*;
import pacman.game.Game;

public class Accion {

    public static final Accion ATACAR = new Accion("ATACAR", 0);
    public static final Accion NEUTRO = new Accion("NEUTRO", 1);
    public static final Accion HUIR = new Accion("HUIR", 2);

    private static final Accion[] Acciones = {ATACAR, NEUTRO, HUIR};

    private String nombreAccion;
    private int idAccion;

    private Accion(String nombre, int id)
    {
        nombreAccion = nombre;
        idAccion = id;
    }

    public int getIdAccion(){
        return idAccion;
    }

    public String getNombreAccion(){
        return nombreAccion;
    }

    public static Accion getAccionById(int idAccion){
        return Acciones[idAccion];
    }

    public String toString(){
        return "Accion: " + nombreAccion + " ";
    }

    public MOVE getMovimientoFromAccion(Game game)
    {
        // Movimiento que vamos a devolver obtenido de la estrategia, la salida del motor borroso
        MOVE movimientoBlinky = MOVE.NEUTRAL;
        MOVE ultimoMovimientoPacMan = game.getPacmanLastMoveMade();

        // Si la estrategia obtenida es ATACAR, la direccion de Blinky es la misma que la de Pac-Man
        if (this.nombreAccion.equals("ATACAR"))
        {
            movimientoBlinky = ultimoMovimientoPacMan;
        }

        // Si la estrategia obtenida es QUIETO, Pac-Man se queda quieto
        if(this.nombreAccion.equals("NEUTRO"))
        {
            movimientoBlinky = MOVE.NEUTRAL;
        }

        // Si la estrategia obtenida es HUIR lo que hacemos es ir en sentido contrario a Pac-Man
        if(this.nombreAccion.equals("HUIR"))
        {
            switch (ultimoMovimientoPacMan) {
                case UP:
                    movimientoBlinky = MOVE.DOWN;
                    break;
                case DOWN:
                    movimientoBlinky = MOVE.UP;
                    break;
                case LEFT:
                    movimientoBlinky = MOVE.RIGHT;
                    break;
                case RIGHT:
                    movimientoBlinky = MOVE.LEFT;
                    break;
                default:
                    movimientoBlinky = MOVE.NEUTRAL;
                    break;
            }
        }

        return movimientoBlinky;
    }

}
