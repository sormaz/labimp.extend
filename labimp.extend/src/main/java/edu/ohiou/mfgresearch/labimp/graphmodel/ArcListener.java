package edu.ohiou.mfgresearch.labimp.graphmodel;

import java.util.EventListener;

public interface ArcListener extends EventListener {
	
	public void arcChanged();
	
	public void userObjectUpdated();
	
}
