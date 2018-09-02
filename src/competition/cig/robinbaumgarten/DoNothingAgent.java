package competition.cig.robinbaumgarten;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;

public class DoNothingAgent implements Agent{
    private boolean action[] = new boolean[Environment.numberOfButtons];
    private String name = "DoNothing";
    
    @Override
    public void reset() {
	
    }

    @Override
    public boolean[] getAction(Environment observation) {
	return this.action;
    }

    @Override
    public AGENT_TYPE getType() {
	return Agent.AGENT_TYPE.AI;
    }

    @Override
    public String getName() {
	return this.name;
    }

    @Override
    public void setName(String name) {
	this.name = name;
    }

}
