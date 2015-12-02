package pacman.controllers.Genetico;

import pacman.controllers.Controller;
import pacman.game.Constants;
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


public class ControladorFuzzyGen extends Controller<Constants.MOVE> {

    RuleBlock reglas;
    Genotipo individuo;

    public ControladorFuzzyGen(Genotipo individuo) {
        this.individuo = individuo;
    }

    public void configure(Engine engine, int[] genotipo ){

        engine.setName("Estrategia");

        // Variable del Controlador correspondiente con la distancia
        InputVariable inputVariable = new InputVariable();
        inputVariable.setEnabled(true);
        inputVariable.setName("Distancia");
        inputVariable.setRange(-1, 200);
        inputVariable.addTerm(new Trapezoid("CERCA", genotipo[0], genotipo[1], genotipo[2], genotipo[3]));
        inputVariable.addTerm(new Trapezoid("NORMAL", genotipo[4], genotipo[5], genotipo[6], genotipo[7]));
        inputVariable.addTerm(new Trapezoid("LEJOS", genotipo[8], genotipo[9], genotipo[10], genotipo[11]));
        engine.addInputVariable(inputVariable);

        // Variable del Controlador correspondiente con el tiempo
        inputVariable = new InputVariable();
        inputVariable.setEnabled(true);
        inputVariable.setName("Tiempo");
        inputVariable.setRange(0, 200);
        inputVariable.addTerm(new Trapezoid("POCO", genotipo[12], genotipo[13], genotipo[14], genotipo[15]));
        inputVariable.addTerm(new Trapezoid("NORMAL", genotipo[16], genotipo[17], genotipo[18], genotipo[19]));
        inputVariable.addTerm(new Trapezoid("MUCHO", genotipo[20], genotipo[21], genotipo[22], genotipo[23]));
        engine.addInputVariable(inputVariable);



        // Variable del Controlador correspondente con distancia al powerpills
        inputVariable = new InputVariable();
        inputVariable.setEnabled(true);
        inputVariable.setName("DistanciaPowerPills");
        inputVariable.setRange(-1, 200);
        inputVariable.addTerm(new Trapezoid("CERCA", genotipo[24], genotipo[25], genotipo[26], genotipo[27]));
        inputVariable.addTerm(new Trapezoid("NORMAL", genotipo[28], genotipo[29], genotipo[30], genotipo[31]));
        inputVariable.addTerm(new Trapezoid("LEJOS", genotipo[32], genotipo[33], genotipo[34], genotipo[35]));
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
        System.out.println(reglas.toString());
    }


    public Genotipo getIndividuo() {
        return individuo;
    }

    public void setIndividuo(Genotipo individuo) {
        this.individuo = individuo;
    }

    @Override
    public Constants.MOVE getMove(Game game, long timeDue) {
        return null;
    }
}
