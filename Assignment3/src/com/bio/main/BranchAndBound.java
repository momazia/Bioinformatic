package com.bio.main;

import java.util.ArrayList;
import java.util.List;

import com.bio.main.pojo.TCGA;

public class BranchAndBound {

	private static BranchAndBound instance = null;

	private BranchAndBound() {
		super();
	}

	public static BranchAndBound getInstance() {
		if (instance == null) {
			instance = new BranchAndBound();
		}
		return instance;
	}

	public List<String> constructLeafNodes(int lmer) {
		List<String> result = new ArrayList<>();
		createLeafNode(result, "", lmer);
		return result;
	}

	private void createLeafNode(List<String> result, String prefix, int lmer) {

		if (lmer == 0) {
			result.add(prefix);
			return;
		}

		for (TCGA tcga : TCGA.values()) {
			createLeafNode(result, prefix + tcga, lmer - 1);
		}
	}
}
