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

	/**
	 * Use the default constructor just before the process starts.
	 */
	public PerformanceMonitor() {
		startTimestamp = System.currentTimeMillis();
	}

	/**
	 * Invoke this method at the end of the process.
	 */
	public void end() {
		endTimestamp = System.currentTimeMillis();
	}

	/**
	 * The method prints the time elapsed in milliseconds
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%d milliseconds", endTimestamp - startTimestamp);
	}
}
