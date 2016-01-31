package com.bio.main;

public class MainProcessor {

	public static void main(String[] args) {
		System.out.println("Starting the process...");
		Process.getInstance().run();
		System.out.println("Process ended.");
	}
}
