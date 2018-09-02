package ch.idsia.tools;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import ch.idsia.mario.simulation.BasicSimulator;
import ch.idsia.mario.simulation.Simulation;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.ToolsConfigurator;
import competition.cig.robinbaumgarten.AStarAgent;
import competition.cig.robinbaumgarten.DoNothingAgent;
import competition.cig.robinbaumgarten.RepeatingAStarAgent;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.level.Level;

import java.util.HashMap;
import java.util.Random;


public class RunMapEliteLevel {
    private String level;
    private int appendingSize;
    private Random rnd;
    private HashMap<String, String> parameters;

    public RunMapEliteLevel(Random rnd, HashMap<String, String> parameters) {
	this.rnd = rnd;
	this.parameters = parameters;
    }

    public void setLevel(String chromosome, int appendingSize) {
        this.level = chromosome;
        this.appendingSize = appendingSize;
    }
    
    public EvaluationInfo runLevel(boolean ignorePipes) {
        Level lvl = Level.initializeLevel(level, appendingSize, ignorePipes);
        CmdLineOptions options = optionSetup(true);
        Agent ai = new DoNothingAgent();
        
        options.setAgent(ai);
        Simulation simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        int deadByFalling = ((BasicSimulator)simulator).simulateOneLevel(lvl).totalKills;
        
        lvl = Level.initializeLevel(level, appendingSize, ignorePipes);
        options = optionSetup(false);
        if(this.parameters == null || this.parameters.get("agentType") == "AStarAgent") {
            ai = new AStarAgent();
        }
        else {
            ai = new RepeatingAStarAgent(rnd, Float.parseFloat(parameters.get("agentSTD")));
        }
        
        options.setAgent(ai);
        simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        EvaluationInfo evalInfo = ((BasicSimulator)simulator).simulateOneLevel(lvl);
        int newFalling = evalInfo.totalKills - evalInfo.stompKills - evalInfo.fireKills - evalInfo.shellKills;
        evalInfo.totalKills = evalInfo.totalKills - newFalling + deadByFalling;
        
        return evalInfo;
        
    }

    private CmdLineOptions optionSetup(boolean invulnerable) {
        CmdLineOptions options = new CmdLineOptions(new String[0]);
        // basic options stuff
        options.setVisualization(GlobalOptions.VisualizationOn);
        options.setNumberOfTrials(1);
        options.setMaxFPS(true);
        options.setMarioInvulnerable(invulnerable);
        ToolsConfigurator.CreateMarioComponentFrame(options);
        options.setMarioMode(0);
        options.setTimeLimit(20);
        return options;
    }
}
