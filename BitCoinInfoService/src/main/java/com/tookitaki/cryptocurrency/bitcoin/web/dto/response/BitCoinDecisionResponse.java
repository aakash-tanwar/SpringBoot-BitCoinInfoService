package com.tookitaki.cryptocurrency.bitcoin.web.dto.response;

import java.util.List;

import com.tookitaki.cryptocurrency.bitcoin.domain.RecommendedDecision;

public class BitCoinDecisionResponse {

	private List<RecommendedDecision> recommendedDecisions;

	public static BitCoinDecisionResponse toDto(List<RecommendedDecision> recommendedDecisions) {
		BitCoinDecisionResponse bitCoinDecisionResponse = new BitCoinDecisionResponse();
		bitCoinDecisionResponse.setRecommendedDecisions(recommendedDecisions);
		return bitCoinDecisionResponse;
	}

	public List<RecommendedDecision> getRecommendedDecisions() {
		return recommendedDecisions;
	}

	public void setRecommendedDecisions(List<RecommendedDecision> recommendedDecisions) {
		this.recommendedDecisions = recommendedDecisions;
	}

	@Override
	public String toString() {
		return "BitCoinDecisionResponse [recommendedDecisions=" + recommendedDecisions + "]";
	}

}
