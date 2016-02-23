package com.bio.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.bio.main.pojo.MedianStr;
import com.bio.main.pojo.Motif;
import com.bio.main.pojo.Sequence;
import com.bio.main.pojo.TCGA;

public class BranchAndBound {

	private static final int PRIORITY_QUEUE_SIZE = 5;
	public static final int L_MER = 6;
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

	public List<MedianStr> findBestMedianStrs(List<Sequence> sequences) {
		return findBestMedianStrs(sequences, L_MER);
	}

	public List<MedianStr> findBestMedianStrs(List<Sequence> sequences, int lmer) {

		List<String> leafNodes = constructLeafNodes(lmer);
		PriorityQueue<MedianStr> priorityQueue = new PriorityQueue<>(PRIORITY_QUEUE_SIZE, new Comparator<MedianStr>() {

			@Override
			public int compare(MedianStr o1, MedianStr o2) {
				return Integer.compare(o1.getTotalDistance(), o2.getTotalDistance());
			}

		});

		for (String leaf : leafNodes) {

			List<Motif> motifs = new ArrayList<>();
			for (Sequence seq : sequences) {
				Motif motif = createMotif(seq.getStr().toCharArray(), leaf.toCharArray(), lmer);
				motifs.add(motif);
			}

			priorityQueue.add(new MedianStr(leaf, motifs));

		}
		return getBestMedianStrs(priorityQueue);
	}

	private List<MedianStr> getBestMedianStrs(PriorityQueue<MedianStr> priorityQueue) {

		List<MedianStr> result = new ArrayList<>();
		for (int i = 0; i < PRIORITY_QUEUE_SIZE; i++) {
			result.add(priorityQueue.poll());
		}

		return result;
	}

	public Motif createMotif(char[] seqChars, char[] leafChars, int lmer) {

		int bestDistance = lmer + 1;
		String motifStr = null;
		int location = 0;

		for (int seqCharIndex = 0; seqCharIndex < seqChars.length - leafChars.length + 1; seqCharIndex++) {

			char[] sequenceChars = Arrays.copyOfRange(seqChars, seqCharIndex, seqCharIndex + leafChars.length);
			int distance = getDistance(sequenceChars, leafChars);

			if (distance < bestDistance) {
				bestDistance = distance;
				motifStr = String.copyValueOf(sequenceChars);
				location = seqCharIndex;
			}
		}

		return new Motif(motifStr, location, bestDistance);
	}

	private int getDistance(char[] seqChars, char[] leafChars) {
		int distance = 0;
		for (int i = 0; i < seqChars.length; i++) {
			if (Character.toLowerCase(seqChars[i]) != Character.toLowerCase(leafChars[i])) {
				distance++;
			}
		}
		return distance;
	}

}
