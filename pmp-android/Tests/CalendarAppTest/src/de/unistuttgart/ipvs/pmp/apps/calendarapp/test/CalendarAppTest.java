package de.unistuttgart.ipvs.pmp.apps.calendarapp.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.resourcegroups.database.IDatabaseConnection;
import android.os.RemoteException;
import android.test.InstrumentationTestCase;

public class CalendarAppTest extends InstrumentationTestCase {
    
    Date date;
    Calendar cal;
    CalendarApp calApp;
    Appointment appo;
    private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
    
    int year;
    int month;
    int day;
    
    String desc;
    String fileName;
    
    IDatabaseConnection idc;
    Map<String, String> values;
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        this.calApp = new CalendarApp() {
            
        };
        
        year = 2011;
        month = 1;
        day = 1;
        cal = new GregorianCalendar(year, month, day);
        
        desc = "Test 1,2,3";
        fileName = "Test_File";
        date = new Date(year, month, day);
    }
    
    
    public void testExport() throws RemoteException {
        
        int id = 0;
        appo = new Appointment(id, desc, date);
        appointmentList.add(appo);
        
        FileSystemConnector.getInstance().exportAppointments(appointmentList, fileName);
    }
    
}
