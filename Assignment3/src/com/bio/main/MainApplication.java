package com.bio.main;

import java.util.List;

public class MainApplication {

	public static void main(String[] args) {

		List<String> leafNodes = BranchAndBound.getInstance().constructLeafNodes(6);
		for (String str : leafNodes) {
			System.out.println(str);
		}
		
		
	}

}
