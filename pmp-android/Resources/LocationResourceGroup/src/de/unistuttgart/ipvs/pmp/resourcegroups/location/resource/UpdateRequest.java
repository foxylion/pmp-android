package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

class UpdateRequest {
	
	public static final long MAX_TIME_BETWEEN_REQUEST = 30 * 1000;
	
	private long minTime;
	private float minDistance;
	private long lastRequest;

	public UpdateRequest(long minTime, float minDistance) {
		this.minTime = minTime;
		this.minDistance = minDistance;
		this.lastRequest = System.currentTimeMillis();
	}
	
	public long getMinTime() {
		return minTime;
	}

	public void setMinTime(long minTime) {
		this.minTime = minTime;
	}

	public float getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(float minDistance) {
		this.minDistance = minDistance;
	}

	public long getLastRequest() {
		return lastRequest;
	}
	
	public boolean isOutdated() {
		return (System.currentTimeMillis() > (this.lastRequest + MAX_TIME_BETWEEN_REQUEST));
	}

	public void setLastRequest(long lastRequest) {
		this.lastRequest = lastRequest;
	}
}
