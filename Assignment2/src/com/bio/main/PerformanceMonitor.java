package com.bio.main;

/**
 * The class can be used to measure the performance of a process.
 * 
 * @author Mohamad Mahdi Ziaee
 *
 */
public class PerformanceMonitor {

	private final long startTimestamp;
	private long endTimestamp;
	private String processName;

	/**
	 * Use the default constructor just before the process starts.
	 */
	public PerformanceMonitor(String processName) {
		this.processName = processName;
		System.out.println("Starting [" + processName + "] process...");
		startTimestamp = System.currentTimeMillis();
	}

	/**
	 * Invoke this method at the end of the process which also prints the time
	 * taken to complete the process.
	 */
	public void end() {
		endTimestamp = System.currentTimeMillis();
		System.out.println(
				String.format("Process %s ended in %d milliseconds", processName, endTimestamp - startTimestamp));
	}

}
