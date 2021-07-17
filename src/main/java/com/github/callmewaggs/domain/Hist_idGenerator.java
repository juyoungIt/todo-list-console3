package com.github.callmewaggs.domain;

public class Hist_idGenerator {
	
	private long id;
	
	public Hist_idGenerator() {
		this.id = 1;
	}
	
	public long generate() {
		return id++;
	}
}
