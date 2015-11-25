package pacman.controllers;

import com.fuzzylite.term.Discrete;
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


public class ControladorBorroso extends Controller<EnumMap<GHOST,MOVE>>{

    private final static float CONSISTENCY=1.0f;	//carry out intended move with this probability
    private Random rnd=new Random();
    private EnumMap<GHOST,MOVE> myMoves=new EnumMap<GHOST,MOVE>(GHOST.class);
    private MOVE[] moves=MOVE.values();


    public void configure(Engine engine){
        engine.setName("Estrategia");

		// Variable del Controlador correspondiente con BlinkyDist
		InputVariable inputVariable = new InputVariable();
		inputVariable.setEnabled(true);
		inputVariable.setName("Distancia");
		inputVariable.setRange(-1, 200);
		inputVariable.addTerm(new Ramp("CERCA", 75, 50));
		inputVariable.addTerm(new Trapezoid("NORMAL", 25, 50, 100, 125));
		inputVariable.addTerm(new Ramp("LEJOS", 75, 100));
		engine.addInputVariable(inputVariable);

		// Variable del Controlador correspondiente con EdibleTime
		inputVariable = new InputVariable();
		inputVariable.setEnabled(true);
		inputVariable.setName("Tiempo");
		inputVariable.setRange(0, 200);
		inputVariable.addTerm(new Ramp("POCO", 100, 50));
		inputVariable.addTerm(new Trapezoid("NORMAL", 25, 50, 100, 125));
		inputVariable.addTerm(new Ramp("MUCHO", 50, 100));
		engine.addInputVariable(inputVariable);

		// Variable del Controlador correspondente con NumOfPillsLeft
        inputVariable = new InputVariable();
        inputVariable.setEnabled(true);
        inputVariable.setName("PowerPills");
        inputVariable.setRange(0,4);
        inputVariable.addTerm(new Discrete("HAY", Arrays.asList(0.0), Arrays.asList(1.0)));
        inputVariable.addTerm(new Discrete("NOHAY", Arrays.asList(1.0, 2.0, 3.0, 4.0), Arrays.asList(1.0, 1.0, 1.0, 1.0)));
        engine.addInputVariable(inputVariable);

		// Variable de Salida del controlador, representa la Estrategia a tomar por Blinky
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
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is POCO and PowerPills is HAY then Estrategia is HUIR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is POCO and PowerPills is NOHAY then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is NORMAL and PowerPills is HAY then Estrategia is HUIR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is NORMAL and PowerPills is NOHAY then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is MUCHO and PowerPills is HAY then Estrategia is HUIR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is CERCA and Tiempo is MUCHO and PowerPills is NOHAY then Estrategia is HUIR", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is POCO and PowerPills is HAY then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is POCO and PowerPills is NOHAY then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is NORMAL and PowerPills is HAY then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is NORMAL and PowerPills is NOHAY then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is MUCHO and PowerPills is HAY then Estrategia is HUIR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is NORMAL and Tiempo is MUCHO and PowerPills is NOHAY then Estrategia is HUIR", engine));

        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is POCO and PowerPills is HAY then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is POCO and PowerPills is NOHAY then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is NORMAL and PowerPills is HAY then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is NORMAL and PowerPills is NOHAY then Estrategia is ATACAR", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is MUCHO and PowerPills is HAY then Estrategia is NEUTRO", engine));
        ruleBlock.addRule(Rule.parse("if Distancia is LEJOS and Tiempo is MUCHO and PowerPills is NOHAY then Estrategia is NEUTRO", engine));

		engine.addRuleBlock(ruleBlock);

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


    // Funcion para obtener la direccion que va a seguir Blinky dependiendo de la estrategia obtenida con el Motor Borroso
    private MOVE getMovimientoFromEstrategia(String estrategia, Game game)
    {
        // Movimiento que vamos a devolver obtenido de la estrategia, la salida del motor borroso
        MOVE movimientoBlinky = MOVE.NEUTRAL;
        MOVE ultimoMovimientoPacMan = game.getPacmanLastMoveMade();

        // Si la estrategia obtenida es ATACAR, la direccion de Blinky es la misma que la de Pac-Man
        if (estrategia.equals("ATACAR"))
        {
            movimientoBlinky = ultimoMovimientoPacMan;
        }

        // Si la estrategia obtenida es QUIETO, Pac-Man se queda quieto
        if(estrategia.equals("NEUTRO"))
        {
            movimientoBlinky = MOVE.NEUTRAL;
        }

        // Si la estrategia obtenida es HUIR lo que hacemos es ir en sentido contrario a Pac-Man
        if(estrategia.equals("HUIR"))
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


    public EnumMap<GHOST,MOVE> getMove(Game game,long timeDue)
    {

        myMoves.clear();

        // Primero obtenemos los datos necesarios para nuestro controlador borroso
        int distancia = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY));
        int tiempo = game.getGhostEdibleTime(GHOST.BLINKY);
        int powerPills = game.getNumberOfActivePowerPills();
        String estrategiaResultado = "";
        MOVE direccionBlinky = MOVE.NEUTRAL;

        // Variables necesarias para ejecutar el motor de logica borrosa
        ControladorBorroso controlador = new ControladorBorroso();
        Engine engine = new Engine();
        controlador.configure(engine);

        StringBuilder status = new StringBuilder();
        if (!engine.isReady(status)){
            throw new RuntimeException("Engine not ready. " + "The following errors were encountered:\n" + status.toString());
        }

        // Seteamos las variables del motor con los datos obtenidos del juego
        engine.setInputValue("Distancia", distancia);
        engine.setInputValue("Tiempo", tiempo);
        engine.setInputValue("PowerPills", powerPills);

        // Ejecutamos el motor
        engine.process();

        // Obtenemos la estategia a partir del output del motor borroso
        estrategiaResultado = getEstrategiaFromFuzzyEngine(engine);

        // Obtenemos la direccion a tomar con la estrategia obtenida del motor
        direccionBlinky = getMovimientoFromEstrategia(estrategiaResultado, game);

        for(GHOST ghost : GHOST.values())
            if(game.doesGhostRequireAction(ghost))
            {
                // Si es Blinky modificamos su comportamiento con el movimiento obtenido, sino los demas fantasmas se comportan como siempre
                if(ghost == GHOST.BLINKY)
                {
                    //System.out.println("Estrategia tomada: " + estrategiaResultado);
                    myMoves.put(ghost, direccionBlinky);
                }else{
                    if(rnd.nextFloat()<CONSISTENCY)	//approach/retreat from the current node that Ms Pac-Man is at
                        myMoves.put(ghost,game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
                                game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(ghost),DM.PATH));
                    else									//else take a random action
                        myMoves.put(ghost,moves[rnd.nextInt(moves.length)]);
                }
            }

        return myMoves;
    }
}
