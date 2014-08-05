package com.homesystemsconsulting.core;

public abstract class Task implements Runnable {
	
	private final String name;

	public Task(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().setName(name);
			execute();
		} finally {
			Thread.currentThread().setName("TERMINATED-" + Thread.currentThread().getName());
		}
	}

	/**
	 * 
	 */
	public abstract void execute();
}
