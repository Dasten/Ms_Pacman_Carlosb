package pacman.controllers.QLearning;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import pacman.game.Constants;
import pacman.game.Game;
import java.util.Arrays;


public class Estado {

    private String nombreEstado;
    private int idEstado;

    private int distancia;
    private int tiempo;
    private int powerPills;

    private String [] listaEstados = {"CPH", "CPN", "CNH", "CNN", "CMH", "CMN",
                                      "NPH", "NPN", "NNH", "NNN", "NMH", "NMN",
                                      "LPH", "LPN", "LNH", "LNN", "LMH", "LMN",
                                      "XXX"};


    public Estado(Game game) {
        this.distancia = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE));
        this.tiempo = game.getGhostEdibleTime(Constants.GHOST.SUE);
        this.powerPills = game.getNumberOfActivePowerPills();
        this.nombreEstado = getNombreEstadoByGame(game);
        this.idEstado = getIdEstadoByNombre(this.nombreEstado);
    }


    public float getRecompensa(){

        // Obtenemos la recompensa segun el nombre del Estado
        // Si el estado es XXX (Pacman ha muerto, obtenemos 100 puntos)
        if(nombreEstado == "XXX"){
            return 100f;
        }else{
            return 0;
        }

    }

    public boolean equals(Object e){
        if(e instanceof Estado){
            return ((Estado)e).idEstado == this.idEstado;
        }
        return false;
    }

    public int getIdEstadoByNombre(String nombre){
        return Arrays.asList(listaEstados).indexOf(nombre);
    }

    public String getNombreEstadoByGame(Game game){

        String resultado = "";
        int d = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE));
        int t = game.getGhostEdibleTime(Constants.GHOST.SUE);
        int pp = game.getNumberOfActivePowerPills();

        // Si Pacman ha muerto (vidas = 0) devolvemos el estado XXX, sino creamos el String con el nombre del estado.
        if(game.gameOver())
        {
            resultado = "XXX";
        }else{

            if (d <= 50) {
                resultado += "C";
            } else if (d > 50 && d < 100) {
                resultado += "N";
            } else {
                resultado += "L";
            }

            if(t <= 50){
                resultado += "P";
            }else if (t > 50 && t < 100){
                resultado += "N";
            }else{
                resultado += "M";
            }

            if(pp > 0){
                resultado += "H";
            }else{
                resultado += "N";
            }

        }

        return resultado;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getPowerPills() {
        return powerPills;
    }

    public void setPowerPills(int powerPills) {
        this.powerPills = powerPills;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
}
