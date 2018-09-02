package ch.idsia.tools;

import ch.idsia.ai.agents.Agent;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.mario.simulation.BasicSimulator;
import ch.idsia.mario.simulation.Simulation;
import ch.idsia.tools.EvaluationInfo;
import ch.idsia.tools.ToolsConfigurator;
import competition.cig.robinbaumgarten.AStarAgent;
import competition.cig.robinbaumgarten.LimitedJumpAgent;
import competition.cig.robinbaumgarten.EnemyBlindAgent;
import competition.cig.robinbaumgarten.NoBButtonAgent;
import ch.idsia.tools.CmdLineOptions;
import ch.idsia.mario.engine.GlobalOptions;
import ch.idsia.mario.engine.level.Level;
import java.util.Random;
import ch.idsia.ai.tasks.ProgressTask;
import ch.idsia.ai.tasks.Task;


public class RunGivenLevel {
    public Agent perfect;
    public Agent disabled;

    public String level;
    public int appendingSize;

    public RunGivenLevel() {
    }
//    public static void main(String[] args) {
//
//        CmdLineOptions options = new CmdLineOptions(args);
//
//        // construct and set up agents
//        RunGivenLevel eL = new RunGivenLevel(new Random());
//
//    }

    public void agentSetup(Agent perfect, Agent disabled) {
        this.perfect = perfect;
        this.disabled = disabled;
    }

    public void setLevel(String chromosome, int appendingSize) {
        this.level = chromosome;
        this.appendingSize = appendingSize;
    }
    public AgentResultObject runLevel(CmdLineOptions options) {
//        System.out.println(level);
        Level lvl = Level.initializeLevel(level, appendingSize, false);


        options = optionSetup(options);
        agentSetup(new AStarAgent(), new LimitedJumpAgent());
//        agentSetup(new AStarAgent(), new EnemyBlindAgent());
//        agentSetup(new AStarAgent(), new NoBButtonAgent());
//        this.runLevel(options);
//        Task task = new ProgressTask(options);

//        double[] perf = task.evaluate(perfect);
//        double[] disa = task.evaluate(disabled);

        // perfect first
        options.setAgent(perfect);


        Simulation simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        EvaluationInfo perf;
        perf = ((BasicSimulator)simulator).simulateOneLevel(lvl);

        options.setAgent(disabled);
        simulator = new BasicSimulator(options.getSimulationOptionsCopy());
        EvaluationInfo disa;
        disa = ((BasicSimulator)simulator).simulateOneLevel(lvl);

       double perfectPerformance =  perf.computeDistancePassed();
       double disabledPerformance = disa.computeDistancePassed();

       double result = (perfectPerformance - disabledPerformance) / options.getLevelLength();
       System.out.println(result);

       int perfStatus = perf.marioStatus;
       int disaStatus = disa.marioStatus;
       AgentResultObject aro = new AgentResultObject(perfStatus, disaStatus, result);

       return aro;
//        System.out.println(perf);
//        System.out.println(disa);

    }

    public CmdLineOptions optionSetup(CmdLineOptions options) {

        if(options == null) {


            options = new CmdLineOptions(new String[0]);
            // basic options stuff
//            options.setMaxFPS(false);
            options.setVisualization(GlobalOptions.VisualizationOn);
            options.setNumberOfTrials(1);
            options.setMaxFPS(true);
            ToolsConfigurator.CreateMarioComponentFrame(
                    options);
            options.setMarioMode(0);
            options.setTimeLimit(10);
        }
        // flag that this is going to work differently. We are going to insert our own level here
//        options.setCustomLevel(true);
        return options;
    }
}
