package ch.idsia.scenarios;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.agents.AgentsPool;
import ch.idsia.ai.agents.human.HumanKeyboardAgent;
import competition.cig.robinbaumgarten.AStarAgent;
//import competition.cig.mechanicextractor.AStarAgent;
//import competition.cig.robinbaumgarten.AStarAgent;
import competition.cig.robinbaumgarten.EnemyBlindAgent;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.tools.EvaluationOptions;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: May 5, 2009
 * Time: 12:46:43 PM
 */
public class Play {

    public static void main(String[] args) {
	GlobalOptions.VisualizationOn = true;
	GlobalOptions.MarioCeiling = true;
	
        Agent controller = new AStarAgent();
        if (args.length > 0) {
            controller = AgentsPool.load (args[0]);
            AgentsPool.addAgent(controller);
        }
        EvaluationOptions options = new CmdLineOptions(new String[0]);
        options.setAgent(controller);
        Task task = new ProgressTask(options);
        options.setMaxFPS(false);
        options.setVisualization(true);
        options.setNumberOfTrials(1);
        options.setMatlabFileName("");
        options.setMarioMode(0);

        options.setLevelRandSeed((int) (Math.random () * Integer.MAX_VALUE));
        options.setLevelDifficulty(5);
        task.setOptions(options);
        
        System.out.println ("Score: " + task.evaluate (controller)[0]);
    }
}
