package de.unistuttgart.ipvs.pmp.apps.calendarapp.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.SlidingDrawer;
import dalvik.annotation.TestTarget;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemListActionType;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;

/**
 * @author Dang Huynh, André Bach Nguyen
 *
 */
public class CalenderAppFileSystemTest extends ActivityInstrumentationTestCase2<CalendarAppActivity> {
    
    Date date;
    Calendar cal;
    CalendarApp calApp;
    Appointment appo;
    private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
    
    CalendarAppActivity a;
    Model m;
    FileSystemConnector fsconn;
    
    int year;
    int month;
    int day;
    
    String desc;
    String fileName;
    
    
    public CalenderAppFileSystemTest() {
        super("de.unistuttgart.ipvs.pmp.apps.calendarapp", CalendarAppActivity.class);
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        year = 2011;
        month = 1;
        day = 1;
        cal = new GregorianCalendar(year, month, day);
        
        desc = "Test 1,2,3";
        fileName = "Test_File";
        date = new Date(year, month, day);
        
        Log.setTagSufix("ExportTest");
    }
    
    
    public void testExport() throws Exception {
        a = this.getActivity();
        assertNotNull(a);
        m = Model.getInstance();
        assertNotNull(m);
        fsconn = FileSystemConnector.getInstance();
        assertNotNull(fsconn);
        
        int id = 0;
        appo = new Appointment(id, desc, date);
        appointmentList.add(appo);
        
        fsconn.exportAppointments(appointmentList, fileName);
        fsconn.listStoredFiles(FileSystemListActionType.NONE);
        
        // Wait (at most 10 seconds) for the connection to be established
        final CountDownLatch signal = new CountDownLatch(1);
        int i = 0;
        while (m.getFileList().isEmpty() && i++<20) {
            signal.await(500, TimeUnit.MILLISECONDS);
        }
        assertFalse(m.getFileList().isEmpty());
        FileDetails fd = m.getFileForName(fileName);
        assertNotNull(fd);
        assertEquals(fd.getName(), fileName);
        a.finish();
    }
    
    
    public void testImport() throws Exception {
        a = this.getActivity();
        assertNotNull(a);
        m = Model.getInstance();
        assertNotNull(m);
        fsconn = FileSystemConnector.getInstance();
        assertNotNull(fsconn);
        
        // Test here
        
        a.finish();
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.getActivity().finish();
    }
}
