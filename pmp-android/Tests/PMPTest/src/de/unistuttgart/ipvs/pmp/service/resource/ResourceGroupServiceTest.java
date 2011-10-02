package de.unistuttgart.ipvs.pmp.service.resource;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;
import de.unistuttgart.ipvs.pmp.service.INullService;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.test.ServiceTestCase;

public class ResourceGroupServiceTest extends ServiceTestCase<ResourceGroupService> {
    
    public static volatile boolean result = false;
    public static volatile Semaphore semaphore = new Semaphore(0);
    
    public Intent intent;
    public PMPSignee testSignee = new PMPSignee(PMPComponentType.PMP, "testSignee", getSystemContext());
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
        
        /* first clear all public keys, then inject the public keys */
        rgApp.getResourceGroup().getSignee().clearRemotePublicKeys();
        rgApp.getResourceGroup().getSignee()
                .setRemotePublicKey(PMPComponentType.PMP, "test.pmp", testSignee.getLocalPublicKey());
        rgApp.getResourceGroup().getSignee()
                .setRemotePublicKey(PMPComponentType.APP, "test.app", testSignee.getLocalPublicKey());
        rgApp.getResourceGroup().getSignee()
                .setRemotePublicKey(PMPComponentType.RESOURCE_GROUP, "test.rg", testSignee.getLocalPublicKey());
        
        intent = new Intent();
        intent.setClass(getSystemContext(), ResourceGroupService.class);
    }
    
    
    /**
     * Bind as a known PMP-Client.
     */
    public void testSimplePMPBind() {
        intent.putExtra(Constants.INTENT_IDENTIFIER, "test.pmp");
        intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.PMP);
        intent.putExtra(Constants.INTENT_SIGNATURE, testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(intent);
        try {
            assertEquals(IResourceGroupServicePMP.class.getName(), binder.getInterfaceDescriptor());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("got an remote exception " + e.toString());
        }
    }
    
    
    /**
     * Bind as a known App-Client.
     */
    public void testSimpleAppBind() {
        intent.putExtra(Constants.INTENT_IDENTIFIER, "test.app");
        intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.APP);
        intent.putExtra(Constants.INTENT_SIGNATURE, testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(intent);
        try {
            assertEquals(IResourceGroupServiceApp.class.getName(), binder.getInterfaceDescriptor());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("got an remote exception " + e.toString());
        }
    }
    
    
    /**
     * Bind as a known but not really possible ResourceGroup-Client.
     */
    public void testSimpleUnknownBind() {
        intent.putExtra(Constants.INTENT_IDENTIFIER, "test.rg");
        intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.RESOURCE_GROUP);
        intent.putExtra(Constants.INTENT_SIGNATURE, testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(intent);
        try {
            assertEquals(INullService.class.getName(), binder.getInterfaceDescriptor());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("got an remote exception " + e.toString());
        }
    }
    
    
    /**
     * Test some uncommon bindings.
     */
    public void testSimpleUncommonBind() {
        intent.putExtra(Constants.INTENT_IDENTIFIER, "");
        intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.NONE);
        intent.putExtra(Constants.INTENT_SIGNATURE, new byte[0]);
        
        IBinder binder = bindService(intent);
        try {
            assertEquals(INullService.class.getName(), binder.getInterfaceDescriptor());
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("got an remote exception " + e.toString());
        }
    }
    
    
    /**
     * Test the registration callback.
     */
    public void testRegistrationCallback() {
        class SimpleTimerTask extends TimerTask {
            
            @Override
            public void run() {
                ResourceGroupServiceTest.semaphore.release();
            }
        };
        
        /* prepare the intent */
        intent.putExtra(Constants.INTENT_IDENTIFIER, "test.pmp");
        intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.PMP);
        intent.putExtra(Constants.INTENT_SIGNATURE, testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(intent);
        try {
            assertEquals(IResourceGroupServicePMP.class.getName(), binder.getInterfaceDescriptor());
            
            /* create the interface for communication */
            IResourceGroupServicePMP irsp = IResourceGroupServicePMP.Stub.asInterface(binder);
            
            /* test the registration succeed */
            irsp.setRegistrationState(new RegistrationState(true));
            
            Timer t = new Timer();
            t.schedule(new SimpleTimerTask(), 5000);
            
            ResourceGroupServiceTest.semaphore.acquire();
            assertTrue(result);
            
            /* prepare */
            ResourceGroupServiceTest.semaphore.drainPermits();
            ResourceGroupServiceTest.result = false;
            t.cancel();
            
            /* test the registration failed */
            irsp.setRegistrationState(new RegistrationState(false, "registrationFailed"));
            
            new Timer().schedule(new SimpleTimerTask(), 5000);
            
            ResourceGroupServiceTest.semaphore.acquire();
            assertTrue(result);
            
        } catch (RemoteException e) {
            e.printStackTrace();
            fail("got an remote exception " + e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("got an interrupted exception " + e.toString());
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
        ResourceGroupServiceTest.result = true;
        ResourceGroupServiceTest.semaphore.release();
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        if (message.equals("registrationFailed")) {
            ResourceGroupServiceTest.result = true;
            ResourceGroupServiceTest.semaphore.release();
        }
    }
}
