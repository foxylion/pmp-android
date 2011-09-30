package de.unistuttgart.ipvs.pmp.apps.calendarapp.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import dalvik.annotation.TestTarget;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemListActionType;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.ImportActivity;
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
    ImportActivity iActivity;
    Model m;
    FileSystemConnector fsconn;
    
    int year;
    int month;
    int day;
    int id;
    
    String desc;
    String fileName;
    
    Activity setSlActivity;
    TouchUtils tu;
    
    
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
        id = 0;
        desc = "Test 1,2,3";
        fileName = "NotEmpty_File";
        date = new Date(year, month, day);
        
        Log.setTagSufix("ExportTest");
    }
    
    
    public void testExportEmptyAppo() {
        fsconn = FileSystemConnector.getInstance();
        m = Model.getInstance();
        
        m.getFileList().clear();
        fileName = "empty_File";
        
        appo = new Appointment(id, desc, date);
        appointmentList.add(appo);
        fsconn.exportAppointments(appointmentList, fileName);
        
        //check directory instead, assertTrue(m.getFileList().isEmpty()) is senseless
        assertTrue(m.getFileList().isEmpty());
        
    }
    
    
    public void testExport() throws Exception {
        a = this.getActivity();
        assertNotNull(a);
        m = Model.getInstance();
        assertNotNull(m);
        fsconn = FileSystemConnector.getInstance();
        assertNotNull(fsconn);
        
        appo = new Appointment(id, desc, date);
        appointmentList.add(appo);
        
        fsconn.exportAppointments(appointmentList, fileName);
        fsconn.listStoredFiles(FileSystemListActionType.NONE);
        
        // Wait (at most 10 seconds) for the connection to be established
        final CountDownLatch signal = new CountDownLatch(1);
        int i = 0;
        while (m.getFileList().isEmpty() && i++ < 40) {
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
        
        m.getFileList().clear();
        assertTrue(m.getFileList().isEmpty());
        
        // Test here
        fsconn.importAppointments(fileName);
        
        iActivity = new ImportActivity();
        //look for right method to check if imported

        
        a.finish();
    }
    
    
    public void testImport_WrongFormat() {
        
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.getActivity().finish();
    }
}
