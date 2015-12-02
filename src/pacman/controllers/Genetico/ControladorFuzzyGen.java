package pacman.controllers.Genetico;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.*;
import com.fuzzylite.Engine;
import com.fuzzylite.defuzzifier.Centroid;
import com.fuzzylite.norm.s.Maximum;
import com.fuzzylite.norm.t.Minimum;
import com.fuzzylite.rule.Rule;
import com.fuzzylite.rule.RuleBlock;
import com.fuzzylite.term.Ramp;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.variable.InputVariable;
import com.fuzzylite.variable.OutputVariable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Random;

import static pacman.game.Constants.*;


public class ControladorFuzzyGen extends Controller<Constants.MOVE> {

    RuleBlock reglas;
    Genotipo individuo;

    public ControladorFuzzyGen(Genotipo individuo) {
        this.individuo = individuo;
    }

    public void configure(Engine engine){

        int[] genotivoIndividuo = individuo.getmCromosoma();

        engine.setName("Estrategia");

        // Variable del Controlador correspondiente con la distancia
        InputVariable inputVariable = new InputVariable();
        inputVariable.setEnabled(true);
        inputVariable.setName("Distancia");
        inputVariable.setRange(-1, 200);
        inputVariable.addTerm(new Trapezoid("CERCA", genotivoIndividuo[0], genotivoIndividuo[1], genotivoIndividuo[2], genotivoIndividuo[3]));
        inputVariable.addTerm(new Trapezoid("NORMAL", genotivoIndividuo[4], genotivoIndividuo[5], genotivoIndividuo[6], genotivoIndividuo[7]));
        inputVariable.addTerm(new Trapezoid("LEJOS", genotivoIndividuo[8], genotivoIndividuo[9], genotivoIndividuo[10], genotivoIndividuo[11]));
        engine.addInputVariable(inputVariable);

        // Variable del Controlador correspondiente con el tiempo
        inputVariable = new InputVariable();
        inputVariable.setEnabled(true);
        inputVariable.setName("Tiempo");
        inputVariable.setRange(0, 200);
        inputVariable.addTerm(new Trapezoid("POCO", genotivoIndividuo[12], genotivoIndividuo[13], genotivoIndividuo[14], genotivoIndividuo[15]));
        inputVariable.addTerm(new Trapezoid("NORMAL", genotivoIndividuo[16], genotivoIndividuo[17], genotivoIndividuo[18], genotivoIndividuo[19]));
        inputVariable.addTerm(new Trapezoid("MUCHO", genotivoIndividuo[20], genotivoIndividuo[21], genotivoIndividuo[22], genotivoIndividuo[23]));
        engine.addInputVariable(inputVariable);



        // Variable del Controlador correspondente con distancia al powerpills
        inputVariable = new InputVariable();
        inputVariable.setEnabled(true);
        inputVariable.setName("DistanciaPowerPills");
        inputVariable.setRange(-1, 200);
        inputVariable.addTerm(new Trapezoid("CERCA", genotivoIndividuo[24], genotivoIndividuo[25], genotivoIndividuo[26], genotivoIndividuo[27]));
        inputVariable.addTerm(new Trapezoid("NORMAL", genotivoIndividuo[28], genotivoIndividuo[29], genotivoIndividuo[30], genotivoIndividuo[31]));
        inputVariable.addTerm(new Trapezoid("LEJOS", genotivoIndividuo[32], genotivoIndividuo[33], genotivoIndividuo[34], genotivoIndividuo[35]));
        engine.addInputVariable(inputVariable);





        // Variable de Salida del controlador, representa la Estrategia a tomar por Pac-Man
        OutputVariable outputVariable = new OutputVariable();
        outputVariable.setEnabled(true);
        outputVariable.setName("Estrategia");
        outputVariable.setRange(0.000, 1.000);
        outputVariable.fuzzyOutput().setAccumulation(new Maximum());
        outputVariable.setDefuzzifier(new Centroid(200));//new MeanOfMaximum());//);
        outputVariable.setDefaultValue(Double.NaN);
        outputVariable.setLockValidOutput(false);
        outputVariable.setLockOutputRange(false);
        outputVariable.addTerm(new Ramp("ATACAR", 0.250, 0.450));
        outputVariable.addTerm(new Trapezoid("NEUTRO", 0.400, 0.450, 0.550, 0.600));
        outputVariable.addTerm(new Ramp("HUIR", 0.600, 0.800));
        engine.addOutputVariable(outputVariable);

        RuleBlock ruleBlock = new RuleBlock();
        ruleBlock.setEnabled(true);
        ruleBlock.setName("");
        ruleBlock.setConjunction(new Minimum());
        ruleBlock.setDisjunction(new Maximum());
        ruleBlock.setActivation(new Minimum());

        // Reglas del motor borroso
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is POCO and DistanciaPowerPills is CERCA then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is POCO and DistanciaPowerPills is NORMAL then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is POCO and DistanciaPowerPills is LEJOS then Estrategia is HUIR", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is NORMAL and DistanciaPowerPills is CERCA then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is NORMAL and DistanciaPowerPills is NORMAL then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is NORMAL and DistanciaPowerPills is LEJOS then Estrategia is NEUTRO", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is MUCHO and DistanciaPowerPills is CERCA then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is MUCHO and DistanciaPowerPills is NORMAL then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is MUCHO and DistanciaPowerPills is LEJOS then Estrategia is ATACAR", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is POCO and DistanciaPowerPills is CERCA then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is POCO and DistanciaPowerPills is NORMAL then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is POCO and DistanciaPowerPills is LEJOS then Estrategia is NEUTRO", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is NORMAL and DistanciaPowerPills is CERCA then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is NORMAL and DistanciaPowerPills is NORMAL then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is NORMAL and DistanciaPowerPills is LEJOS then Estrategia is NEUTRO", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is MUCHO and DistanciaPowerPills is CERCA then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is MUCHO and DistanciaPowerPills is NORMAL then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is MUCHO and DistanciaPowerPills is LEJOS then Estrategia is ATACAR", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is POCO and DistanciaPowerPills is CERCA then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is POCO and DistanciaPowerPills is NORMAL then Estrategia is HUIR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is POCO and DistanciaPowerPills is LEJOS then Estrategia is HUIR", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is NORMAL and DistanciaPowerPills is CERCA then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is NORMAL and DistanciaPowerPills is NORMAL then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is NORMAL and DistanciaPowerPills is LEJOS then Estrategia is NEUTRO", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is MUCHO and DistanciaPowerPills is CERCA then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is MUCHO and DistanciaPowerPills is NORMAL then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is MUCHO and DistanciaPowerPills is LEJOS then Estrategia is NEUTRO", engine));

        engine.addRuleBlock(ruleBlock);

        reglas = ruleBlock;

    }

    void printReglas(){
        //
        System.out.println(reglas.getRule());
    }


    public Genotipo getIndividuo() {
        return individuo;
    }

    public void setIndividuo(Genotipo individuo) {
        this.individuo = individuo;
    }


    // Funcion para obtener la estrategia a partir de la salida del motor borroso, obtenemos la estrategia con mas valor de pertenencia
    private String getEstrategiaFromFuzzyEngine(Engine engine){

        // Obetenemos el valor de pertenencia de cada grupo
        double pertenenciaAtacar = engine.getOutputVariable("Estrategia").getTerm(0).membership(engine.getOutputVariable("Estrategia").defuzzify());
        double pertenenciaNEUTRO = engine.getOutputVariable("Estrategia").getTerm(1).membership(engine.getOutputVariable("Estrategia").defuzzify());
        double pertenenciaHUIR = engine.getOutputVariable("Estrategia").getTerm(2).membership(engine.getOutputVariable("Estrategia").defuzzify());
        //System.out.println("ATACAR: " + pertenenciaAtacar + " - NEUTRO: " + pertenenciaNEUTRO + " - HUIR: " + pertenenciaHUIR);

        if (pertenenciaAtacar > pertenenciaNEUTRO){
            if(pertenenciaAtacar > pertenenciaHUIR){
                return "ATACAR";
            }else{
                return "HUIR";
            }
        }else{
            if(pertenenciaNEUTRO > pertenenciaHUIR){
                return "NEUTRO";
            }else{
                return "HUIR";
            }
        }
    }



    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {

        // Primero obtenemos los datos necesarios para nuestro controlador borroso
        int distancia = getDistanciaToFantasma(game);
        int tiempo = getTiempoToFantasma(game);
        int distanciaPowerPills = getDistanciaToPP(game);


        String estrategiaResultado = "";
        MOVE direccionPacMan = MOVE.NEUTRAL;

        //ControladorFuzzyGen controlador = new ControladorFuzzyGen();
        Engine engine = new Engine();
        //controlador.configure(engine);
        configure(engine);



        StringBuilder status = new StringBuilder();
        if (!engine.isReady(status)){
            throw new RuntimeException("Engine not ready. " + "The following errors were encountered:\n" + status.toString());
        }

        // Seteamos las variables del motor con los datos obtenidos del juego
        engine.setInputValue("Distancia", distancia);
        engine.setInputValue("Tiempo", tiempo);
        engine.setInputValue("DistanciaPowerPills", distanciaPowerPills);

        // Ejecutamos el motor
        engine.process();

        // Obtenemos la estategia a partir del output del motor borroso
        estrategiaResultado = getEstrategiaFromFuzzyEngine(engine);

        // Obtenemos la direccion a tomar con la estrategia obtenida del motor
        direccionPacMan = getMovimientoFromEstrategia(estrategiaResultado, game);

        return direccionPacMan;
    }


    private int getDistanciaToFantasma(Game game){
        GHOST fantasmaCercano = getFatantasmaCercano(game);
        int distancia = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(fantasmaCercano));
        return distancia;
    }

    private int getTiempoToFantasma(Game game){
        GHOST fantasmaCercano = getFatantasmaCercano(game);
        int tiempo =  game.getGhostEdibleTime(fantasmaCercano);
        return tiempo;
    }

    private int getDistanciaToPP(Game game){
        int distancia;
        int [] indexPP = game.getActivePowerPillsIndices();

        // Obtenemos la distancia mas cercana, sino quedan PPs devolvemos 200 (distancia maxima)
        if(indexPP.length > 0){
            Arrays.sort(indexPP);
            distancia = indexPP[0];
        }else{
            distancia = 200;
        }

        return distancia;
    }

    private GHOST getFatantasmaCercano(Game game){

        int[] distancias = new int[4];
        GHOST fantasma = GHOST.BLINKY;
        distancias[0] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY));
        distancias[1] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY));
        distancias[2] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE));
        distancias[3] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY));

        int max = distancias[0];
        int indexFantasma = 0;
        for(int i = 0; i < distancias.length; i++){
            if(distancias[i] >= max){
                max = distancias[i];
                indexFantasma = i;
            }
        }

        switch (indexFantasma){
            case 0:
                fantasma = GHOST.BLINKY;
                break;
            case 1:
                fantasma = GHOST.PINKY;
                break;
            case 2:
                fantasma = GHOST.SUE;
                break;
            case 3:
                fantasma = GHOST.INKY;
                break;
        }

        return fantasma;
    }





    // Funcion para obtener la direccion que va a seguir PacMan dependiendo de la estrategia obtenida con el Motor Borroso
    private MOVE getMovimientoFromEstrategia(String estrategia, Game game)
    {
        // Movimiento que vamos a devolver obtenido de la estrategia, la salida del motor borroso
        MOVE movimientoPacMan = MOVE.NEUTRAL;
        GHOST fantasmaCercano = getFatantasmaCercano(game);
        MOVE ultimoMovimientoFantasma = game.getGhostLastMoveMade(fantasmaCercano);

        // Si la estrategia obtenida es ATACAR, la direccion de PacMan es la misma que la del Fantasma
        if (estrategia.equals("ATACAR"))
        {
            movimientoPacMan = ultimoMovimientoFantasma;
        }

        // Si la estrategia obtenida es NEUTRO, Pac-Man se sigue con su movimiento
        if(estrategia.equals("NEUTRO"))
        {
            movimientoPacMan = MOVE.NEUTRAL;
        }

        // Si la estrategia obtenida es HUIR lo que hacemos es ir en sentido contrario al Fantasma
        if(estrategia.equals("HUIR"))
        {
            switch (ultimoMovimientoFantasma) {
                case UP:
                    movimientoPacMan = MOVE.DOWN;
                    break;
                case DOWN:
                    movimientoPacMan = MOVE.UP;
                    break;
                case LEFT:
                    movimientoPacMan = MOVE.RIGHT;
                    break;
                case RIGHT:
                    movimientoPacMan = MOVE.LEFT;
                    break;
                default:
                    movimientoPacMan = MOVE.NEUTRAL;
                    break;
            }
        }

        return movimientoPacMan;
    }


}
