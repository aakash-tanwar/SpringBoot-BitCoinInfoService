package com.tookitaki.cryptocurrency.bitcoin.domain;

import com.tookitaki.cryptocurrency.bitcoin.common.DecisionStatus;
import com.tookitaki.cryptocurrency.bitcoin.common.DecisionStrategy;

public class RecommendedDecision {

	private DecisionStrategy decisionStrategy;
	private DecisionStatus decisionStatus;

	public DecisionStrategy getDecisionStrategy() {
		return decisionStrategy;
	}

	public void setDecisionStrategy(DecisionStrategy decisionStrategy) {
		this.decisionStrategy = decisionStrategy;
	}

	public DecisionStatus getDecisionStatus() {
		return decisionStatus;
	}

	public void setDecisionStatus(DecisionStatus decisionStatus) {
		this.decisionStatus = decisionStatus;
	}

	@Override
	public String toString() {
		return "RecommendedDecision [decisionStrategy=" + decisionStrategy + ", decisionStatus=" + decisionStatus + "]";
	}

}
