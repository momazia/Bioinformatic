package com.bio.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.bio.main.pojo.Median;
import com.bio.main.pojo.Sequence;

public class BnBMainProcess {

	private int i;
	private List<Sequence> dnaSeq;
	private int l;
	private int k;
	private char[] s;
	private static final int PRIORITY_QUEUE_SIZE = 5;
	PriorityQueue<Median> priorityQueue = new PriorityQueue<>(PRIORITY_QUEUE_SIZE, new Comparator<Median>() {

		@Override
		public int compare(Median o1, Median o2) {
			return Integer.compare(o1.getTotalDistance(), o2.getTotalDistance());
		}

	});

	public BnBMainProcess(List<Sequence> dnaSeq, int l, int k) {
		super();
		this.dnaSeq = dnaSeq;
		this.l = l;
		this.k = k;
	}

	public BnBMainProcess() {

	}

	public List<Median> medianSearch() {
		s = createInitialLmer(l);
		int bestDistance = Integer.MAX_VALUE;
		i = l;
		while (i > 0) {
			if (i < l) {
				String prefix = getActualText(s, i);
				int optimisticDistance = getTotalDistance(prefix);
				if (optimisticDistance > bestDistance) {
					byPass();
				} else {
					nextVertex();
				}

			} else {
				String word = getActualText(s);
				priorityQueue.add(new Median(word, getTotalDistance(word)));
				nextVertex();
			}
		}
		return getTopMedians();
	}

	private List<Median> getTopMedians() {

		List<Median> result = new ArrayList<>();
		for (int i = 0; i < PRIORITY_QUEUE_SIZE; i++) {
			result.add(priorityQueue.poll());
		}

		return result;
	}

	private String getActualText(char[] chr, int to) {
		return getActualText(Arrays.copyOfRange(chr, 0, to + 1));
	}

	private void byPass() {
		for (int j = i; j >= 1; j--) {
			if (Character.getNumericValue(s[j]) < k) {
				s[j] = nextCharacter(s[j]);
				i = j;
				return;
			}
		}
		i = 0;
	}

	private int getTotalDistance(String word) {

		int totalDistance = 0;
		char[] wordChars = word.toCharArray();

		for (Sequence sequence : dnaSeq) {

			char[] seqChars = sequence.getStr().toCharArray();
			int bestLocalDistance = Integer.MAX_VALUE;

			for (int seqCharIndex = 0; seqCharIndex < seqChars.length - wordChars.length + 1; seqCharIndex++) {

				char[] sequenceChars = Arrays.copyOfRange(seqChars, seqCharIndex, seqCharIndex + wordChars.length);
				int distance = getDistance(sequenceChars, wordChars);
				if (bestLocalDistance > distance) {
					bestLocalDistance = distance;
				}
			}
			totalDistance += bestLocalDistance;

		}

		return totalDistance;
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

	public String getActualText(char[] s) {
		String result = "";
		for (int i = 1; i < s.length; i++) {
			switch (s[i]) {
			case '1':
				result += "A";
				break;
			case '2':
				result += "C";
				break;
			case '3':
				result += "G";
				break;
			case '4':
				result += "T";
				break;
			}
		}
		return result;
	}

	private void nextVertex() {
		if (i < l) {
			i++;
			s[i] = '1';
			return;
		} else {
			for (int j = l; j > 0; j--) {
				if (Character.getNumericValue(s[j]) < k) {
					s[j] = nextCharacter(s[j]);
					i = j;
					return;
				}
			}
		}
		i = 0;
	}

	public char nextCharacter(char c) {
		return (char) (c + 1);
	}

	public char[] createInitialLmer(int l) {
		char[] chr = new char[l + 1];
		chr[0] = ' ';// Putting an empty char in the beginning of the char seq
						// since the indexes start from 0 in JAVA. And in the
						// pseudo code indexes start at 1. This is for
						// readability purposes.
		for (int i = 1; i <= l; i++) {
			chr[i] = '1';
		}
		return chr;
	}
}
