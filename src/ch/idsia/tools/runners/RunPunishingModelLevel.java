package ch.idsia.tools.runners;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.simulation.BasicSimulator;
import ch.idsia.mario.simulation.Simulation;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.ToolsConfigurator;
import competition.cig.robinbaumgarten.AStarAgent;
import ch.idsia.tools.AgentResultObject;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.level.Level;

import java.util.HashMap;
import java.util.Random;


public class RunPunishingModelLevel {
    private String _level;
    private int _appendingSize;
    private Random _rnd;
    private HashMap<String, String> _parameters;

    public RunPunishingModelLevel(Random rnd, HashMap<String, String> parameters) {
    	this._rnd = rnd;
	this._parameters = parameters;
    }

    public void setLevel(String chromosome, int appendingSize) {
        this._level = chromosome;
        this._appendingSize = appendingSize;
    }
    public AgentResultObject runLevel(boolean ignorePipes) {
	GlobalOptions.limitedForwardModel_killCoin = false;
        GlobalOptions.limitedForwardModel_killHighJump = false;
        GlobalOptions.limitedForwardModel_killMushroom = false;
        GlobalOptions.limitedForwardModel_killRun = false;
        GlobalOptions.limitedForwardModel_killShell = false;
        GlobalOptions.limitedForwardModel_killStomp = false;
        
	Agent perfectAgent = new AStarAgent();
        Level lvl = Level.initializeLevel(_level, _appendingSize, ignorePipes);
        CmdLineOptions options = optionSetup(false);
        options.setAgent(perfectAgent);
        Simulation simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        EvaluationInfo perfectEvalInfo = ((BasicSimulator)simulator).simulateOneLevel(lvl);
        
        if(this._parameters != null) {
            switch(this._parameters.get("agentType").trim().toLowerCase()) {
            case "highjump":
        	GlobalOptions.limitedForwardModel_killHighJump = true;
        	break;
            case "coin":
        	GlobalOptions.limitedForwardModel_killCoin = true;
        	break;
            case "mushroom":
        	GlobalOptions.limitedForwardModel_killMushroom = true;
        	break;
            case "run":
                GlobalOptions.limitedForwardModel_killRun = true;
                break;
            case "shell":
        	GlobalOptions.limitedForwardModel_killShell = true;
        	break;
            case "stomp":
        	GlobalOptions.limitedForwardModel_killStomp = true;
        	break;
            }
        }
        
        perfectAgent = new AStarAgent();
        lvl = Level.initializeLevel(_level, _appendingSize, ignorePipes);
        options = optionSetup(false);
        options.setAgent(perfectAgent);
        simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        EvaluationInfo limitedEvalInfo = ((BasicSimulator)simulator).simulateOneLevel(lvl);
        
        GlobalOptions.limitedForwardModel_killCoin = false;
        GlobalOptions.limitedForwardModel_killHighJump = false;
        GlobalOptions.limitedForwardModel_killMushroom = false;
        GlobalOptions.limitedForwardModel_killRun = false;
        GlobalOptions.limitedForwardModel_killShell = false;
        GlobalOptions.limitedForwardModel_killStomp = false;
        
       return new AgentResultObject(perfectEvalInfo.marioStatus, limitedEvalInfo.marioStatus, 
	       perfectEvalInfo.lengthOfLevelPassedCells, limitedEvalInfo.lengthOfLevelPassedCells, 
	       perfectEvalInfo.totalLengthOfLevelCells - this._appendingSize);
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
