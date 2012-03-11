package de.unistuttgart.ipvs.pmp.apps.calendarapp.test;

import java.io.File;
import java.io.FileWriter;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;

/**
 * General activity based system-test for the {@link CalendarApp}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class CalendarAppTest extends ActivityInstrumentationTestCase2<CalendarAppActivity> {
    
    /*
     * test logic
     */
    
    private boolean verified = false;
    private static final String[] REQUIRED_SERVICE_FEATURES = new String[] { "read", "write", "send", "import",
            "export" };
    
    /*
     * values
     */
    
    private static final String APPOINTMENT_NAME = "Important Appointment";
    private static final String APPOINTMENT_DESC = "Important Description";
    private static final int APPOINTMENT_YEAR = 2013;
    private static final int APPOINTMENT_MONTH = 5;
    private static final int APPOINTMENT_DAY = 27;
    
    private static final String APPOINTMENT2_NAME = "PMP-Meeting";
    private static final String APPOINTMENT2_DESC = "Unimportant";
    private static final int APPOINTMENT2_YEAR = 2012;
    private static final int APPOINTMENT2_MONTH = 4;
    private static final int APPOINTMENT2_DAY = 26;
    
    private static final String IL_APPOINTMENT_NAME = "Illegal Appointment";
    private static final String IL_APPOINTMENT_DESC = "Illegal Description";
    
    private static final String EXPORT_NAME = "Meine kleine Terminsammlung";
    
    
    public CalendarAppTest() {
        super("de.unistuttgart.ipvs.pmp.apps.calendarapp", CalendarAppActivity.class);
    }
    
    private Solo solo;
    
    
    @Override
    protected void setUp() throws Exception {
        // if dummy user is not complying
        if (!this.verified) {
            assertTrue(getActivity().getApplication() instanceof CalendarApp);
            
            for (String sf : REQUIRED_SERVICE_FEATURES) {
                if (!PMP.get().isServiceFeatureEnabled(sf)) {
                    Log.e("Calendar-Test", "Service feature " + sf + " not enabled. Behavior undefined.");
                }
            }
            this.verified = true;
        }
        
        this.solo = new Solo(getInstrumentation(), getActivity());
        
        // prepare
        this.solo.clickOnMenuItem("Delete");
        assertTrue(this.solo.searchText("really want to delete"));
        this.solo.clickOnText("Confirm");
        assertEquals(0, Model.getInstance().getAppointmentList().size());
        
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        this.solo.finishOpenedActivities();
    }
    
    
    /*
     * "everything is fine" tests
     */
    
    /**
     * Tests whether a new appointment can be generated.
     * 
     * @throws Exception
     */
    public void testNewAppointment() throws Exception {
        this.solo.clickOnMenuItem("New");
        // new appointment dialog
        assertTrue(this.solo.waitForText("Add new"));
        assertEquals(1, this.solo.getCurrentDatePickers().size());
        this.solo.setDatePicker(this.solo.getCurrentDatePickers().get(0), APPOINTMENT_YEAR, APPOINTMENT_MONTH,
                APPOINTMENT_DAY);
        assertEquals(5, this.solo.getCurrentEditTexts().size());
        this.solo.enterText(this.solo.getCurrentEditTexts().get(3), APPOINTMENT_NAME);
        this.solo.enterText(this.solo.getCurrentEditTexts().get(4), APPOINTMENT_DESC);
        assertEquals(3, this.solo.getCurrentRadioButtons().size());
        this.solo.clickOnText("high");
        this.solo.clickOnText("Confirm");
        
        // verify it's there
        assertTrue(this.solo.searchText(String.valueOf(APPOINTMENT_YEAR)));
        assertTrue(this.solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertTrue(this.solo.searchText(APPOINTMENT_NAME));
        assertTrue(this.solo.searchText(APPOINTMENT_DESC));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment shows the right values in the change dialog.
     * 
     * @throws Exception
     */
    public void testNewAppointmentRightChange() throws Exception {
        testNewAppointment();
        this.solo.clickOnText(APPOINTMENT_NAME);
        
        // change appointment dialog
        assertTrue(this.solo.waitForText("Change"));
        assertEquals(1, this.solo.getCurrentDatePickers().size());
        assertEquals(APPOINTMENT_YEAR, this.solo.getCurrentDatePickers().get(0).getYear());
        assertEquals(APPOINTMENT_MONTH, this.solo.getCurrentDatePickers().get(0).getMonth());
        assertEquals(APPOINTMENT_DAY, this.solo.getCurrentDatePickers().get(0).getDayOfMonth());
        assertEquals(5, this.solo.getCurrentEditTexts().size());
        assertEquals(APPOINTMENT_NAME, this.solo.getCurrentEditTexts().get(3).getText().toString());
        assertEquals(APPOINTMENT_DESC, this.solo.getCurrentEditTexts().get(4).getText().toString());
        assertEquals(3, this.solo.getCurrentRadioButtons().size());
        assertTrue(this.solo.isRadioButtonChecked("high"));
        this.solo.clickOnText("Confirm");
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment can be changed correctly.
     * 
     * @throws Exception
     */
    public void testChangeAppointment() throws Exception {
        testNewAppointment();
        this.solo.clickOnText(APPOINTMENT_NAME);
        
        // change appointment dialog
        assertTrue(this.solo.waitForText("Change"));
        assertEquals(1, this.solo.getCurrentDatePickers().size());
        this.solo.setDatePicker(this.solo.getCurrentDatePickers().get(0), APPOINTMENT2_YEAR, APPOINTMENT2_MONTH,
                APPOINTMENT2_DAY);
        assertEquals(5, this.solo.getCurrentEditTexts().size());
        this.solo.clearEditText(this.solo.getCurrentEditTexts().get(3));
        this.solo.enterText(this.solo.getCurrentEditTexts().get(3), APPOINTMENT2_NAME);
        this.solo.clearEditText(this.solo.getCurrentEditTexts().get(4));
        this.solo.enterText(this.solo.getCurrentEditTexts().get(4), APPOINTMENT2_DESC);
        assertEquals(3, this.solo.getCurrentRadioButtons().size());
        this.solo.clickOnText("low");
        this.solo.clickOnText("Confirm");
        
        // verify it's there
        assertFalse(this.solo.searchText(String.valueOf(APPOINTMENT_YEAR)));
        assertFalse(this.solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertFalse(this.solo.searchText(APPOINTMENT_NAME));
        assertFalse(this.solo.searchText(APPOINTMENT_DESC));
        assertTrue(this.solo.searchText(String.valueOf(APPOINTMENT2_YEAR)));
        assertTrue(this.solo.searchText(String.valueOf(APPOINTMENT2_DAY)));
        assertTrue(this.solo.searchText(APPOINTMENT2_NAME));
        assertTrue(this.solo.searchText(APPOINTMENT2_DESC));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment can be deleted.
     * 
     * @throws Exception
     */
    public void testDeleteAppointment() throws Exception {
        testNewAppointment();
        this.solo.clickLongOnText(APPOINTMENT_NAME);
        this.solo.clickOnText("Delete");
        
        assertFalse(this.solo.searchText(String.valueOf(APPOINTMENT_YEAR)));
        assertFalse(this.solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertFalse(this.solo.searchText(APPOINTMENT_NAME));
        assertFalse(this.solo.searchText(APPOINTMENT_DESC));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment can be mailed.
     * 
     * @throws Exception
     */
    public void testMailAppointment() throws Exception {
        testNewAppointment();
        this.solo.clickLongOnText(APPOINTMENT_NAME);
        this.solo.clickOnText("Send");
        
        /*
        assertTrue(solo.waitForText(String.valueOf(APPOINTMENT_YEAR)));
        assertTrue(solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertTrue(solo.searchText(APPOINTMENT_NAME));
        assertTrue(solo.searchText(APPOINTMENT_DESC));
        assertTrue(solo.searchText("Send"));
        */
        this.solo.goBack();
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether an all empty calendar shows it is empty.
     * 
     * @throws Exception
     */
    public void testEmptyNotice() throws Exception {
        assertTrue(this.solo.searchText("No appointments"));
    }
    
    
    /**
     * Tests whether export is possible. Overrides, if necessary.
     * 
     * @throws Exception
     */
    public void testExport() throws Exception {
        testNewAppointment();
        this.solo.clickOnMenuItem("Export");
        
        // export dialog
        assertTrue(this.solo.waitForText("Export"));
        assertEquals(1, this.solo.getCurrentEditTexts().size());
        try {
            runTestOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    //Quick and dirty fix because enterText() doesn't work
                    CalendarAppTest.this.solo.getCurrentEditTexts().get(0).setText(EXPORT_NAME);
                    //        solo.enterText(solo.getCurrentEditTexts().get(0), EXPORT_NAME);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.solo.clickOnText("Confirm");
        if (this.solo.waitForText("Override")) {
            this.solo.clickOnButton("Override");
        }
        this.solo.searchText("succeeded");
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether import is possible.
     * 
     * @throws Exception
     */
    public void testImport() throws Exception {
        testExport();
        this.solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(this.solo.waitForText("Import"));
        this.solo.clickOnText(EXPORT_NAME);
        assertTrue(this.solo.searchText("succeed"));
        
        assertTrue(this.solo.searchText(APPOINTMENT_NAME, 2));
        assertTrue(this.solo.searchText(APPOINTMENT_DESC, 2));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /*
     * "you should not try this" tests
     */
    
    /**
     * Tests whether empty exports are possible.
     * 
     * @throws Exception
     */
    public void testEmptyExport() throws Exception {
        this.solo.clickOnMenuItem("Export");
        assertTrue(this.solo.searchText("no appointments"));
        this.solo.clickOnText("OK");
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether you can export an empty filename
     * 
     * @throws Exception
     */
    public void testEmptyExportName() throws Exception {
        testNewAppointment();
        this.solo.clickOnMenuItem("Export");
        
        // export dialog
        assertTrue(this.solo.waitForText("Export"));
        assertEquals(1, this.solo.getCurrentEditTexts().size());
        this.solo.clickOnText("Confirm");
        
        assertTrue(this.solo.searchText("Please enter a filename"));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether appointments without a name can be added.
     * 
     * @throws Exception
     */
    public void testAddEmpty() throws Exception {
        this.solo.clickOnMenuItem("New");
        
        // new appointment dialog
        assertTrue(this.solo.waitForText("Add new"));
        assertEquals(1, this.solo.getCurrentDatePickers().size());
        this.solo.setDatePicker(this.solo.getCurrentDatePickers().get(0), APPOINTMENT_YEAR, APPOINTMENT_MONTH,
                APPOINTMENT_DAY);
        assertEquals(5, this.solo.getCurrentEditTexts().size());
        assertEquals(3, this.solo.getCurrentRadioButtons().size());
        this.solo.clickOnText("middle");
        this.solo.clickOnText("Confirm");
        
        assertTrue(this.solo.searchText("add a name"));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether you can import an invalid date.
     * For the next tests you have to set the permission "android.permission.WRITE_EXTERNAL_STORAGE" in the
     * AndroidManifest of the CalendarApp
     * 
     * @throws Exception
     */
    public void testEvilImportDate() throws Exception {
        testExport();
        
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/calendarData/"
                + EXPORT_NAME);
        FileWriter fw = new FileWriter(f, false);
        try {
            fw.append("BEGIN:VCALENDAR");
            fw.append("\n");
            fw.append("VERSION:2.0");
            fw.append("\n");
            fw.append("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP");
            fw.append("\n");
            fw.append("BEGIN:VTODO");
            fw.append("\n");
            fw.append("SUMMARY:" + IL_APPOINTMENT_NAME);
            fw.append("\n");
            fw.append("DESCRIPTION:" + IL_APPOINTMENT_DESC);
            fw.append("\n");
            fw.append("PRIORITY:1");
            fw.append("\n");
            fw.append("DTSTAMP:20130231T000000Z");
            fw.append("\n");
            fw.append("END:VTODO");
            fw.append("\n");
            fw.append("END:VCALENDAR");
        } finally {
            fw.close();
        }
        
        this.solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(this.solo.waitForText("Import"));
        this.solo.clickOnText(EXPORT_NAME);
        assertFalse(this.solo.searchText("succeeded"));
        
        assertFalse(this.solo.searchText(IL_APPOINTMENT_NAME));
        assertFalse(this.solo.searchText(IL_APPOINTMENT_DESC));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether you can import an invalid priority.
     * 
     * @throws Exception
     */
    public void testEvilImportPriority() throws Exception {
        testExport();
        
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/calendarData/"
                + EXPORT_NAME);
        FileWriter fw = new FileWriter(f, false);
        try {
            fw.append("BEGIN:VCALENDAR");
            fw.append("\n");
            fw.append("VERSION:2.0");
            fw.append("\n");
            fw.append("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP");
            fw.append("\n");
            fw.append("BEGIN:VTODO");
            fw.append("\n");
            fw.append("SUMMARY:" + IL_APPOINTMENT_NAME);
            fw.append("\n");
            fw.append("DESCRIPTION:" + IL_APPOINTMENT_DESC);
            fw.append("\n");
            fw.append("PRIORITY:1025");
            fw.append("\n");
            fw.append("DTSTAMP:20130627T000000Z");
            fw.append("\n");
            fw.append("END:VTODO");
            fw.append("\n");
            fw.append("END:VCALENDAR");
        } finally {
            fw.close();
        }
        
        this.solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(this.solo.waitForText("Import"));
        this.solo.clickOnText(EXPORT_NAME);
        assertFalse(this.solo.searchText("succeeded"));
        
        assertFalse(this.solo.searchText(IL_APPOINTMENT_NAME));
        assertFalse(this.solo.searchText(IL_APPOINTMENT_DESC));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether you can import an invalid dataset.
     * 
     * @throws Exception
     */
    public void testEvilImport() throws Exception {
        testExport();
        
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/calendarData/"
                + EXPORT_NAME);
        FileWriter fw = new FileWriter(f, false);
        try {
            fw.append("BEGIN:VCALENDAR");
            fw.append("\n");
            fw.append("VERSION:20.0");
            fw.append("\n");
            fw.append("PRODID:BETTER_THAN_PMP_CALENDAR");
            fw.append("\n");
            fw.append("BEGIN:VTODO");
            fw.append("\n");
            fw.append("SUMMARY:" + IL_APPOINTMENT_NAME);
            fw.append("\n");
            fw.append("DESCRIPTION:" + IL_APPOINTMENT_DESC);
            fw.append("\n");
            fw.append("PRIORITY:1");
            fw.append("\n");
            fw.append("DTSTAMP:20130627T000000Z");
            fw.append("\n");
            fw.append("END:VTODO");
            fw.append("\n");
            fw.append("END:VCALENDAR");
        } finally {
            fw.close();
        }
        
        this.solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(this.solo.waitForText("Import"));
        this.solo.clickOnText(EXPORT_NAME);
        assertFalse(this.solo.searchText("succeeded"));
        
        assertFalse(this.solo.searchText(IL_APPOINTMENT_NAME));
        assertFalse(this.solo.searchText(IL_APPOINTMENT_DESC));
        
        this.solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
}
