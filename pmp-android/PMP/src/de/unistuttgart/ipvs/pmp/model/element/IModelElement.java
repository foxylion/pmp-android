package de.unistuttgart.ipvs.pmp.model.element;

/**
 * Most generic interface for any interface for a {@link ModelElement}. 
 * 
 * @author Tobias Kuhn
 *
 */
public interface IModelElement {
    
    /**
     * @return Returns the <b>unique</b> identifier of the {@link IModelElement}.
     */
    public String getIdentifier();
}
