package de.unistuttgart.ipvs.pmp.api;

public interface IPMP {

	public void register(PMPRegistrationHandler handler, boolean includeUpdate, int timeout);

	public void updateServiceFeatures(PMPServiceFeatureUpdateHandler handler, int timeout);

	public void requestServiceFeatures(List<String> sfs, boolean showDialog, int timeout);

	public Map<String, Boolean> getServiceFeatures();

	public boolean isServiceFeatureEnabled(String sf);

	public boolean areServiceFeaturesEnabled(List<String> sfs);

	public List<String> listAvailableServiceFeatures(List<String> sfs);

	public List<String> listUnavailableServiceFeatures(List<String> sfs);

	public List<String> listAllServiceFeatures();

	public void getResource(PMPResourceIdentifier res, PMPResourceHandler handler, int timeout);

	public boolean isResourceCached(PMPResourceIdentifier res);

	public IBinder getResourceFromCache(PMPResourceIdentifier res);
}