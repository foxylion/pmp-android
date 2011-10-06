/*
 * Copyright 2011 pmp-android development team
 * Project: PMPTest
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.service.resource;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.test.ServiceTestCase;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroupSingleApp;
import de.unistuttgart.ipvs.pmp.service.INullService;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

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
        
        setApplication(this.rgApp);
        
        this.rgApp.onCreate();
        
        /* first clear all public keys, then inject the public keys */
        this.rgApp.getResourceGroup().getSignee().clearRemotePublicKeys();
        this.rgApp.getResourceGroup().getSignee()
                .setRemotePublicKey(PMPComponentType.PMP, "test.pmp", this.testSignee.getLocalPublicKey());
        this.rgApp.getResourceGroup().getSignee()
                .setRemotePublicKey(PMPComponentType.APP, "test.app", this.testSignee.getLocalPublicKey());
        this.rgApp.getResourceGroup().getSignee()
                .setRemotePublicKey(PMPComponentType.RESOURCE_GROUP, "test.rg", this.testSignee.getLocalPublicKey());
        
        this.intent = new Intent();
        this.intent.setClass(getSystemContext(), ResourceGroupService.class);
    }
    
    
    /**
     * Bind as a known PMP-Client.
     */
    public void testSimplePMPBind() {
        this.intent.putExtra(Constants.INTENT_IDENTIFIER, "test.pmp");
        this.intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.PMP);
        this.intent.putExtra(Constants.INTENT_SIGNATURE, this.testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(this.intent);
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
        this.intent.putExtra(Constants.INTENT_IDENTIFIER, "test.app");
        this.intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.APP);
        this.intent.putExtra(Constants.INTENT_SIGNATURE, this.testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(this.intent);
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
        this.intent.putExtra(Constants.INTENT_IDENTIFIER, "test.rg");
        this.intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.RESOURCE_GROUP);
        this.intent.putExtra(Constants.INTENT_SIGNATURE, this.testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(this.intent);
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
        this.intent.putExtra(Constants.INTENT_IDENTIFIER, "");
        this.intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.NONE);
        this.intent.putExtra(Constants.INTENT_SIGNATURE, new byte[0]);
        
        IBinder binder = bindService(this.intent);
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
        }
        ;
        
        /* prepare the intent */
        this.intent.putExtra(Constants.INTENT_IDENTIFIER, "test.pmp");
        this.intent.putExtra(Constants.INTENT_TYPE, PMPComponentType.PMP);
        this.intent.putExtra(Constants.INTENT_SIGNATURE, this.testSignee.signContent("test.rg".getBytes()));
        
        IBinder binder = bindService(this.intent);
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
