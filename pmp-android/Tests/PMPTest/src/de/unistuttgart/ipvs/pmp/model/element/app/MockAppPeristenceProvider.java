package de.unistuttgart.ipvs.pmp.model.element.app;

import de.unistuttgart.ipvs.pmp.model.element.MockElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;

public class MockAppPeristenceProvider extends MockElementPersistenceProvider<App> {
    
    public IAIS ais;
    
    
    public MockAppPeristenceProvider(App element) {
        super(element);
        
        this.ais = XMLUtilityProxy.getAppUtil().createBlankAIS();
    }
    
    
    @Override
    public void loadElementData() {
        this.element.ais = this.ais;
    }
    
}
