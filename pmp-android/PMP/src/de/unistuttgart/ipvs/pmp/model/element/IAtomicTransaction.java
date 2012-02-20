package de.unistuttgart.ipvs.pmp.model.element;

/**
 * Interface to describe an atomic transaction processor.
 * 
 * @author Tobias Kuhn
 * 
 */
public interface IAtomicTransaction {
    
    /**
     * Starts a new transaction.
     */
    public void start();
    
    
    /**
     * Commits the transaction.
     */
    public void commit();
    
    
    /**
     * Aborts the transaction.
     */
    public void abort();
    
}
