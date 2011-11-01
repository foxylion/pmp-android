package de.unistuttgart.ipvs.pmp.model2.element;

import de.unistuttgart.ipvs.pmp.model2.PersistenceProvider;

/**
 * The basic model element functions to provide access to the persistence layer by dynamic data fetching.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class ModelElement {
    
    /**
     * If true, the actual content has been successfully loaded. If false, the element is just a carcass until a request
     * requires it to initialize.
     */
    private boolean cached;
    
    /**
     * The persistence provider for this model element.
     */
    protected ElementPersistenceProvider<? extends ModelElement> persistenceProvider;
    
    
    public ModelElement() {
        this.cached = false;
    }
    
    
    /**
     * Sets the {@link ElementPersistenceProvider} for this {@link ModelElement}. Should only invoked by another
     * {@link PersistenceProvider}.
     * 
     * @param persistenceProvider
     */
    public void setPersistenceProvider(ElementPersistenceProvider<? extends ModelElement> persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }
    
    
    /**
     * <p>
     * Checks whether this element is already cached. If not, loads the data from the persistence and thus caches the
     * element.
     * </p>
     * 
     * <p>
     * <b>You should NEVER need to call this method outside of the model.</b> In lazy-initialization style it will be
     * automatically called from within the {@link ModelElement}. In eager-initialization style it will be automatically
     * called for you by the {@link PersistenceProvider}.
     * </p>
     * 
     * @return whether this object is cached after this call. false should only be possible, if no
     *         {@link ElementPersistenceProvider} was assigned.
     */
    public boolean checkCached() {
        if (!isCached()) {
            if (this.persistenceProvider != null) {
                this.persistenceProvider.loadElementData();
                this.cached = true;
            }
        }
        
        return this.cached;
    }
    
    
    /**
     * Persists this element, i.e. writes its contents to the persistence layer.
     * 
     * @throws IllegalStateException
     *             if the element is not yet cached (it would make no sense to persist an element whose values were not
     *             edited before - if they were, it would be cached)
     * @return whether this object is persisted after this call. false should only be possible, if no
     *         {@link ElementPersistenceProvider} was assigned.
     */
    protected boolean persist() {
        if (!isCached()) {
            throw new IllegalStateException("Cannot persist an uncached element.");
        }
        
        if (this.persistenceProvider == null) {
            return false;
        }
        
        this.persistenceProvider.storeElementData();
        return true;
    }
    
    
    /**
     * 
     * @return true, if this element is cached i.e. it represents the current persistence state, false otherwise
     */
    public boolean isCached() {
        return this.cached;
    }
    
}
