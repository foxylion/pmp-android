package de.unistuttgart.ipvs.pmp.service.resource;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.test.ServiceTestCase;

public class ResourceGroupServiceTest extends ServiceTestCase<ResourceGroupService> {

    public Intent intent;
    public PMPSignee pmpSignee = new PMPSignee(PMPComponentType.PMP, "test.pmp", getSystemContext());
    public ResourceGroupSingleApp<TestResourceGroup> rgApp = new ResourceGroupSingleApp<TestResourceGroup>() {
        
        @Override
        protected TestResourceGroup createResourceGroup() {
            return new TestResourceGroup(getSystemContext());
        }
    };
    
    
    public ResourceGroupServiceTest() {
        super(ResourceGroupService.class);
    }
    
    @Override
    protected void setupService() {
        super.setupService();  
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        setApplication(rgApp);
        
        rgApp.onCreate();
        
        /* first clear all public keys, then inject the pmp public key */
        rgApp.getResourceGroup().getSignee().clearRemotePublicKeys();
        rgApp.getResourceGroup().getSignee()
                .setRemotePublicKey(PMPComponentType.PMP, "test.pmp", pmpSignee.getLocalPublicKey());
        

        intent = new Intent();
        intent.setClass(getSystemContext(), ResourceGroupService.class);
        intent.putExtra(Constants.INTENT_IDENTIFIER, "test.pmp");
        intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.PMP);
        intent.putExtra(Constants.INTENT_SIGNATURE, pmpSignee.signContent("test.rg".getBytes()));
    }
    
    public void testSimpleBind() {
        IBinder binder = bindService(intent);
        try {
            assertEquals(IResourceGroupServicePMP.class.getName(), binder.getInterfaceDescriptor());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("got an remote exception " + e.toString());
        }
    }
}

class TestResourceGroup extends ResourceGroup {
    
    public TestResourceGroup(Context serviceContext) {
        super(serviceContext);
    }
    
    
    @Override
    public String getName(String locale) {
        return "myName:" + locale;
    }
    
    
    @Override
    public String getDescription(String locale) {
        return "myDescription:" + locale;
    }
    
    
    @Override
    protected String getServiceAndroidName() {
        return "test.rg";
    }
    
    
    @Override
    public void onRegistrationSuccess() {
        
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        
    }
    
}
