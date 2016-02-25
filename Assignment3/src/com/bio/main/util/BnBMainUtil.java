package com.bio.main.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.bio.main.pojo.Median;
import com.bio.main.pojo.Sequence;

/**
 * Main utility program used to find median using Branch and Bound (BnB) algorithm.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class BnBMainUtil {

	/**
	 * Moving index within S
	 */
	private int i;
	/**
	 * DNA sequences
	 */
	private List<Sequence> dnaSeq;
	/**
	 * Lmer
	 */
	private int l;
	/**
	 * TGCA size = 4
	 */
	private int k;
	/**
	 * The leaf representation in an array of characters.
	 */
	private char[] s;
	/**
	 * Size of the priority queue
	 */
	private static final int PRIORITY_QUEUE_SIZE = 5;
	/**
	 * Counter for checking to see how many leafs were visited and skipped.
	 */
	private int numOfLeafsVisited = 0;
	/**
	 * Priority queue defined by comparing the total distances of each to {@link Median} objects.
	 */
	PriorityQueue<Median> priorityQueue = new PriorityQueue<>(PRIORITY_QUEUE_SIZE, new Comparator<Median>() {

		@Override
		public int compare(Median o1, Median o2) {
			return Integer.compare(o1.getTotalDistance(), o2.getTotalDistance());
		}

	});

	public BnBMainUtil(List<Sequence> dnaSeq, int l, int k) {
		super();
		this.dnaSeq = dnaSeq;
		this.l = l;
		this.k = k;
	}

	public BnBMainUtil() {

	}

	/**
	 * The main method which triggers the median search and finds the {@link BnBMainUtil#PRIORITY_QUEUE_SIZE} top medians and return them.
	 * 
	 * @return
	 */
	public List<Median> medianSearch() {
		// Creating the initial lmer array of strings in '111111' format.
		s = createInitialLmer(l);

		int bestDistance = Integer.MAX_VALUE;

		i = l;
		while (i > 0) {
			if (i < l) {// In case of parent nodes.
				String prefix = getActualText(s, i);
				int optimisticDistance = getTotalDistance(prefix);
				if (optimisticDistance > bestDistance) {// If the prefix (parent node)'s total distance is worse, then we byPass.
					byPass();
				} else {// Otherwise we go to the next vertex.
					nextVertex();
				}

			} else {// In case of leaf nodes.
				String word = getActualText(s);
				// Counting the number of leafs visited.
				numOfLeafsVisited++;
				// Finding the best distance
				int totalDistance = getTotalDistance(word);
				if (totalDistance < bestDistance) {
					bestDistance = totalDistance;
				}
				// Priority queue takes care of throwing out those which do not have a high score
				priorityQueue.add(new Median(word, totalDistance));
				nextVertex();
			}
		}
		// Returning the final result.
		return getTopMedians();
	}

	/**
	 * Converts the priority queue's top {@link BnBMainUtil#PRIORITY_QUEUE_SIZE} elements into a list.
	 * 
	 * @return
	 */
	private List<Median> getTopMedians() {

		List<Median> result = new ArrayList<>();
		for (int i = 0; i < PRIORITY_QUEUE_SIZE; i++) {
			result.add(priorityQueue.poll());
		}

		return result;
	}

	/**
	 * Gets an array with digits format and convert it into its proper presentation format ('AACCA'). <br>
	 * ('11221') to ('AACCA')
	 * 
	 * @param chr
	 * @param to
	 * @return
	 */
	private String getActualText(char[] chr, int to) {
		return getActualText(Arrays.copyOfRange(chr, 0, to + 1));
	}

	/**
	 * By passes a part of the tree by updating s[j] into the next possible character given if it is possible. Otherwise it will stop (at the end of
	 * the tree).
	 */
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

	/**
	 * Finds the total distance of a given word from all the DNA sequences.
	 * 
	 * @param word
	 * @return
	 */
	private int getTotalDistance(String word) {

		int totalDistance = 0;
		char[] wordChars = word.toCharArray();

		for (Sequence sequence : dnaSeq) {

			char[] seqChars = sequence.getStr().toCharArray();
			int bestLocalDistance = Integer.MAX_VALUE;

			// Going through the DNA sequence using a window of lmer.
			for (int seqCharIndex = 0; seqCharIndex < seqChars.length - wordChars.length + 1; seqCharIndex++) {

				char[] sequenceChars = Arrays.copyOfRange(seqChars, seqCharIndex, seqCharIndex + wordChars.length);
				// Finding the distance between the window and the word given.
				int distance = getDistance(sequenceChars, wordChars);
				if (bestLocalDistance > distance) {
					bestLocalDistance = distance;
				}
			}
			totalDistance += bestLocalDistance;

		}

		return totalDistance;
	}

	/**
	 * Finds the total distance between the two given arrays of characters. If the same characters in the array, of the same index, are not the same,
	 * that means its distance is added by 1.
	 * 
	 * @param seqChars
	 * @param leafChars
	 * @return
	 */
	private int getDistance(char[] seqChars, char[] leafChars) {
		int distance = 0;
		for (int i = 0; i < seqChars.length; i++) {
			if (Character.toLowerCase(seqChars[i]) != Character.toLowerCase(leafChars[i])) {
				distance++;
			}
		}
		return distance;
	}

	/**
	 * Converts digit formatted arrays into DNA format.
	 * 
	 * @param s
	 * @return
	 */
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

	/**
	 * Moves to the next vertex in the tree.
	 */
	private void nextVertex() {
		if (i < l) {// Parent node
			i++;
			s[i] = '1';
			return;
		} else {// Leaf node
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

	/**
	 * Gives back the next digit in character data format. Example: '1' becomes '2'
	 * 
	 * @param c
	 * @return
	 */
	public char nextCharacter(char c) {
		return (char) (c + 1);
	}

	/**
	 * Creates the initial array of characters in which the index 'i' will move around and the algorithm is based on.
	 * 
	 * @param l
	 * @return
	 */
	public char[] createInitialLmer(int l) {
		char[] chr = new char[l + 1];
		chr[0] = ' ';// Putting an empty char in the beginning of the char seq since the indexes start from 0 in JAVA. And in the pseudo code indexes
						// start at 1. This is for readability purposes.
		for (int i = 1; i <= l; i++) {
			chr[i] = '1';
		}
		return chr;
	}

	public int getNumOfLeafsVisited() {
		return numOfLeafsVisited;
	}

	public void setNumOfLeafsVisited(int numOfLeafsVisited) {
		this.numOfLeafsVisited = numOfLeafsVisited;
	}
}
