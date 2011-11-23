package de.unistuttgart.ipvs.pmp.apps.calendarapp.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
//import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
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
    Appointment appointment;
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
        date = cal.getTime();
        id = 0;
        desc = "Test 1,2,3";
        fileName = "NotEmpty_File";
        
        //Log.setTagSufix("ExportTest");
    }
    
    
//    public void testExportEmptyAppointment() {
//        fsconn = FileSystemConnector.getInstance();
//        m = Model.getInstance();
//        
//        m.getFileList().clear();
//        fileName = "empty_File";
//        
////        appointment = new Appointment(id, desc, date);
////        appointmentList.add(appointment);
//        //fsconn.exportAppointments(appointmentList, fileName);
//        fsconn.exportAppointments(null, "");
//        
//        //check directory instead, assertTrue(m.getFileList().isEmpty()) is senseless
//        assertTrue(m.getFileList().isEmpty());
//    }
    
    
    public void testExport() throws Exception {
        a = this.getActivity();
        assertNotNull(a);
        m = Model.getInstance();
        assertNotNull(m);
        fsconn = FileSystemConnector.getInstance();
        assertNotNull(fsconn);
        
        if (m.getServiceLevel()<6) {
            m.setServiceLevel(6);
        }
        
        // fsconn.exportAppointments(null, null); // Test not necessary
        // fsconn.exportAppointments(appointmentList, "a"); // Test not necessary
        
        // Test invalid file names
        appointment = new Appointment(id, desc, date);
        appointmentList.add(appointment);
        fsconn.exportAppointments(appointmentList, "");
        fsconn.exportAppointments(appointmentList, "\n");
        fsconn.exportAppointments(appointmentList, ".");
        fsconn.exportAppointments(appointmentList, "\\");
        fsconn.exportAppointments(appointmentList, "/");
        fsconn.exportAppointments(appointmentList, ">");
        fsconn.exportAppointments(appointmentList, "<");
        fsconn.exportAppointments(appointmentList, ":");
        fsconn.exportAppointments(appointmentList, "|");
        fsconn.exportAppointments(appointmentList, "&");
        fsconn.exportAppointments(appointmentList, "*");
        fsconn.exportAppointments(appointmentList, "?");
        // Filename 256-250 characters
        fsconn.exportAppointments(appointmentList, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa123456");
        fsconn.exportAppointments(appointmentList, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa12345");
        fsconn.exportAppointments(appointmentList, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1234");
        fsconn.exportAppointments(appointmentList, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa123");
        fsconn.exportAppointments(appointmentList, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa12");
        fsconn.exportAppointments(appointmentList, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa1");
        fsconn.exportAppointments(appointmentList, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        
        fsconn.exportAppointments(appointmentList, fileName);
//        fsconn.listStoredFiles(FileSystemListActionType.NONE);
        
        // Wait (at most 10 seconds) for the connection to be established
        final CountDownLatch signal = new CountDownLatch(1);
        int i = 0;
        while (m.getFileList().isEmpty() && i++ < 20) {
            signal.await(500, TimeUnit.MILLISECONDS);
        }
        //Log.d("Waited " + (i-1)*500 + " milliseconds");
        assertFalse(m.getFileList().isEmpty());
        FileDetails fd = m.getFileForName(fileName);
        assertNotNull(fd);
        assertEquals(fd.getName(), fileName);
        
        // Test import
        appointmentList.clear();
        fsconn.importAppointments(fileName);
        appointmentList = Model.getInstance().getAppointmentList();
        assertEquals(1, appointmentList.size());
        appointment = appointmentList.get(0);
        assertEquals(date, appointment.getDate());
        assertEquals(desc, appointment.getDescrpition());
        
        a.finish();
    }
    
    
//    public void testImport() throws Exception {
//        a = this.getActivity();
//        assertNotNull(a);
//        m = Model.getInstance();
//        assertNotNull(m);
//        fsconn = FileSystemConnector.getInstance();
//        assertNotNull(fsconn);
//        
//        m.getFileList().clear();
//        assertTrue(m.getFileList().isEmpty());
//        
//        // Test here
//        fsconn.importAppointments(fileName);
//        
//        iActivity = new ImportActivity();
//        //look for right method to check if imported
//
//        
//        a.finish();
//    }
    
    
//    public void testImport_WrongFormat() {
//        
//    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (a != null) {
            a.finish();
        }
    }
}