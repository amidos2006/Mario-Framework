package ch.idsia.tools.runners;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.mario.simulation.BasicSimulator;
import ch.idsia.mario.simulation.Simulation;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.ToolsConfigurator;
import competition.cig.robinbaumgarten.AStarAgent;
import competition.cig.robinbaumgarten.DoNothingAgent;
import competition.cig.robinbaumgarten.LimitedJumpAgent;
import competition.cig.robinbaumgarten.EnemyBlindAgent;
import competition.cig.robinbaumgarten.NoBButtonAgent;
import ch.idsia.tools.AgentResultObject;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.level.Level;

import java.util.HashMap;
import java.util.Random;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;


public class RunLimitedAgentsLevel {
    private String _level;
    private int _appendingSize;
    private Random _rnd;
    private HashMap<String, String> _parameters;

    public RunLimitedAgentsLevel(Random rnd, HashMap<String, String> parameters) {
    	this._rnd = rnd;
	this._parameters = parameters;
    }

    public void setLevel(String chromosome, int appendingSize) {
        this._level = chromosome;
        this._appendingSize = appendingSize;
    }
    public AgentResultObject runLevel(boolean ignorePipes) {
	Agent perfectAgent = new AStarAgent();
        Agent limitedAgent = new DoNothingAgent();
        if(this._parameters != null) {
            if(this._parameters.get("agentType") == "LimitedJump") {
        	limitedAgent = new LimitedJumpAgent();
            }
            if(this._parameters.get("agentType") == "EnemyBlind") {
        	limitedAgent = new EnemyBlindAgent();
            }
            if(this._parameters.get("agentType") == "NoRun") {
        	limitedAgent = new NoBButtonAgent();
            }
        }
        
        Level lvl = Level.initializeLevel(_level, _appendingSize, ignorePipes);
        CmdLineOptions options = optionSetup(false);
        options.setAgent(perfectAgent);
        Simulation simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        EvaluationInfo perfectEvalInfo = ((BasicSimulator)simulator).simulateOneLevel(lvl);
        
        lvl = Level.initializeLevel(_level, _appendingSize, ignorePipes);
        options = optionSetup(false);
        options.setAgent(limitedAgent);
        simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        EvaluationInfo limitedEvalInfo = ((BasicSimulator)simulator).simulateOneLevel(lvl);
        
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
