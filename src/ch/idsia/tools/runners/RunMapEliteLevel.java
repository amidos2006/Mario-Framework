package ch.idsia.tools.runners;

import ch.idsia.ai.agents.Agent;
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
import ch.idsia.mario.engine.sprites.Mario;

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
        int numTrials = 1;
        
        lvl = Level.initializeLevel(level, appendingSize, ignorePipes);
        options = optionSetup(false);
        if(this.parameters == null || this.parameters.get("agentType") == "AStarAgent") {
            ai = new AStarAgent();
        }
        else {
            ai = new RepeatingAStarAgent(rnd, Float.parseFloat(parameters.get("agentSTD")));
            if(parameters.containsKey("numTrials")) {
        	numTrials = Integer.parseInt(parameters.get("numTrials"));
            }
        }
        
        EvaluationInfo bestEvalInfo = null;
        for(int i=0; i<numTrials; i++) {
            options.setAgent(ai);
            simulator = new BasicSimulator(options.getSimulationOptionsCopy());
            EvaluationInfo evalInfo = ((BasicSimulator)simulator).simulateOneLevel(lvl);
            if(bestEvalInfo == null || evalInfo.lengthOfLevelPassedCells > bestEvalInfo.lengthOfLevelPassedCells) {
        	bestEvalInfo = evalInfo;
            }
            if(bestEvalInfo.marioStatus == Mario.STATUS_WIN) {
        	break;
            }
        }
        
        int newFalling = bestEvalInfo.totalKills - bestEvalInfo.stompKills - bestEvalInfo.fireKills - bestEvalInfo.shellKills;
        bestEvalInfo.totalKills = bestEvalInfo.totalKills - newFalling + deadByFalling;
        return bestEvalInfo;
        
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
