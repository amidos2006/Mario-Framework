package competition.cig.robinbaumgarten;

import java.util.Random;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;

public class RepeatingAStarAgent  implements Agent{
    private AStarAgent _agent;
    private boolean[] _prevAction;
    private int _repeatingCount;
    private float _repeatingSTD;
    private Random _rnd;
    private String _name = "RepeatingAStarAgent";

    public RepeatingAStarAgent(Random rnd, float std) {
	this._rnd = rnd;
	this._repeatingSTD = std;
    }
    
    @Override
    public void reset() {
	this._agent = new AStarAgent();
	this._agent.reset();
	this._prevAction = new boolean[Environment.numberOfButtons];
	this._repeatingCount = 0;
    }

    @Override
    public boolean[] getAction(Environment observation) {
	if(this._repeatingCount <= 0) {
	    this._prevAction = this._agent.getAction(observation);
	    this._repeatingCount = (int)Math.ceil(Math.abs(this._rnd.nextGaussian()) * this._repeatingSTD);
	    if(this._repeatingCount == 0) {
		this._repeatingCount = 1;
	    }
	}
	this._repeatingCount -= 1;
	return this._prevAction;
    }

    @Override
    public AGENT_TYPE getType() {
	return AGENT_TYPE.AI;
    }

    @Override
    public String getName() {
	return this._name;
    }

    @Override
    public void setName(String name) {
	this._name = name;
    }

}
