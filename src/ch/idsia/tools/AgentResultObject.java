package ch.idsia.tools;

public class AgentResultObject {

    public int perftectAgentWin;
    public int limitedAgentWin;
    public double perfectAgentDistance;
    public double limitedAgentDistance;
    public double totalDistance;

    public AgentResultObject(int perWin, int limWin, double perDist, double limDist, double totDist) {
	perftectAgentWin = perWin;
	limitedAgentWin = limWin;
        this.perfectAgentDistance = perDist;
        this.limitedAgentDistance = limDist;
        this.totalDistance = totDist;
    }
}
