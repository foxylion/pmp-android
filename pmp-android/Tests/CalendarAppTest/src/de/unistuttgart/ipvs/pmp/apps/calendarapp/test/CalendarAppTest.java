package de.unistuttgart.ipvs.pmp.apps.calendarapp.test;

import java.io.File;
import java.io.FileWriter;

import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

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
        if (!verified) {
            assertTrue(getActivity().getApplication() instanceof CalendarApp);
            
            CalendarApp ca = (CalendarApp) getActivity().getApplication();
            
            for (String sf : REQUIRED_SERVICE_FEATURES) {
                if (!ca.isServiceFeatureEnabled(sf)) {
                    Log.e("Calendar-Test", "Service feature " + sf + " not enabled. Behavior undefined.");
                }
            }
            verified = true;
        }
        
        solo = new Solo(getInstrumentation(), getActivity());
        
        // prepare
        solo.clickOnMenuItem("Delete");
        assertTrue(solo.searchText("really want to delete"));
        solo.clickOnText("Confirm");
        assertEquals(0, Model.getInstance().getAppointmentList().size());
        
    }
    
    
    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
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
        solo.clickOnMenuItem("New");
        // new appointment dialog
        assertTrue(solo.waitForText("Add new"));
        assertEquals(1, solo.getCurrentDatePickers().size());
        solo.setDatePicker(solo.getCurrentDatePickers().get(0), APPOINTMENT_YEAR, APPOINTMENT_MONTH, APPOINTMENT_DAY);
        assertEquals(5, solo.getCurrentEditTexts().size());
        solo.enterText(solo.getCurrentEditTexts().get(3), APPOINTMENT_NAME);
        solo.enterText(solo.getCurrentEditTexts().get(4), APPOINTMENT_DESC);
        assertEquals(3, solo.getCurrentRadioButtons().size());
        solo.clickOnText("high");
        solo.clickOnText("Confirm");
        
        // verify it's there
        assertTrue(solo.searchText(String.valueOf(APPOINTMENT_YEAR)));
        assertTrue(solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertTrue(solo.searchText(APPOINTMENT_NAME));
        assertTrue(solo.searchText(APPOINTMENT_DESC));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment shows the right values in the change dialog.
     * 
     * @throws Exception
     */
    public void testNewAppointmentRightChange() throws Exception {
        testNewAppointment();
        solo.clickOnText(APPOINTMENT_NAME);
        
        // change appointment dialog
        assertTrue(solo.waitForText("Change"));
        assertEquals(1, solo.getCurrentDatePickers().size());
        assertEquals(APPOINTMENT_YEAR, solo.getCurrentDatePickers().get(0).getYear());
        assertEquals(APPOINTMENT_MONTH, solo.getCurrentDatePickers().get(0).getMonth());
        assertEquals(APPOINTMENT_DAY, solo.getCurrentDatePickers().get(0).getDayOfMonth());
        assertEquals(5, solo.getCurrentEditTexts().size());
        assertEquals(APPOINTMENT_NAME, solo.getCurrentEditTexts().get(3).getText().toString());
        assertEquals(APPOINTMENT_DESC, solo.getCurrentEditTexts().get(4).getText().toString());
        assertEquals(3, solo.getCurrentRadioButtons().size());
        assertTrue(solo.getButton("high").isSelected());
        solo.clickOnText("Confirm");
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment can be changed correctly.
     * 
     * @throws Exception
     */
    public void testChangeAppointment() throws Exception {
        testNewAppointment();
        solo.clickOnText(APPOINTMENT_NAME);
        
        // change appointment dialog
        assertTrue(solo.waitForText("Change"));
        assertEquals(1, solo.getCurrentDatePickers().size());
        solo.setDatePicker(solo.getCurrentDatePickers().get(0), APPOINTMENT2_YEAR, APPOINTMENT2_MONTH, APPOINTMENT2_DAY);
        assertEquals(5, solo.getCurrentEditTexts().size());
        solo.clearEditText(solo.getCurrentEditTexts().get(3));
        solo.enterText(solo.getCurrentEditTexts().get(3), APPOINTMENT2_NAME);
        solo.clearEditText(solo.getCurrentEditTexts().get(4));
        solo.enterText(solo.getCurrentEditTexts().get(4), APPOINTMENT2_DESC);
        assertEquals(3, solo.getCurrentRadioButtons().size());
        solo.clickOnText("low");
        solo.clickOnText("Confirm");
        
        // verify it's there
        assertFalse(solo.searchText(String.valueOf(APPOINTMENT_YEAR)));
        assertFalse(solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertFalse(solo.searchText(APPOINTMENT_NAME));
        assertFalse(solo.searchText(APPOINTMENT_DESC));
        assertTrue(solo.searchText(String.valueOf(APPOINTMENT2_YEAR)));
        assertTrue(solo.searchText(String.valueOf(APPOINTMENT2_DAY)));
        assertTrue(solo.searchText(APPOINTMENT2_NAME));
        assertTrue(solo.searchText(APPOINTMENT2_DESC));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment can be deleted.
     * 
     * @throws Exception
     */
    public void testDeleteAppointment() throws Exception {
        testNewAppointment();
        solo.clickLongOnText(APPOINTMENT_NAME);
        solo.clickOnText("Delete");
        
        assertFalse(solo.searchText(String.valueOf(APPOINTMENT_YEAR)));
        assertFalse(solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertFalse(solo.searchText(APPOINTMENT_NAME));
        assertFalse(solo.searchText(APPOINTMENT_DESC));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether a new appointment can be mailed.
     * 
     * @throws Exception
     */
    public void testMailAppointment() throws Exception {
        testNewAppointment();
        solo.clickLongOnText(APPOINTMENT_NAME);
        solo.clickOnText("Send");
        
        assertTrue(solo.searchText(String.valueOf(APPOINTMENT_YEAR)));
        assertTrue(solo.searchText(String.valueOf(APPOINTMENT_DAY)));
        assertTrue(solo.searchText(APPOINTMENT_NAME));
        assertTrue(solo.searchText(APPOINTMENT_DESC));
        assertTrue(solo.searchText("Send"));
        
        solo.goBack();
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether an all empty calendar shows it is empty.
     * 
     * @throws Exception
     */
    public void testEmptyNotice() throws Exception {
        assertTrue(solo.searchText("No appointments"));
    }
    
    
    /**
     * Tests whether export is possible. Overrides, if necessary.
     * 
     * @throws Exception
     */
    public void testExport() throws Exception {
        testNewAppointment();
        solo.clickOnMenuItem("Export");
        
        // export dialog
        assertTrue(solo.waitForText("Export"));
        assertEquals(1, solo.getCurrentEditTexts().size());
        solo.enterText(0, EXPORT_NAME);
        solo.clickOnText("Confirm");
        if (solo.waitForText("Override")) {
            solo.clickOnButton("Override");
        }
        solo.searchText("succeeded");
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether import is possible.
     * 
     * @throws Exception
     */
    public void testImport() throws Exception {
        testExport();
        solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(solo.waitForText("Import"));
        solo.clickOnText(EXPORT_NAME);
        assertTrue(solo.searchText("succeed"));
        
        assertTrue(solo.searchText(APPOINTMENT_NAME, 2));
        assertTrue(solo.searchText(APPOINTMENT_DESC, 2));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
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
        solo.clickOnMenuItem("Export");
        assertTrue(solo.searchText("no appointments"));
        solo.clickOnText("OK");
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether you can export an empty filename
     * 
     * @throws Exception
     */
    public void testEmptyExportName() throws Exception {
        testNewAppointment();
        solo.clickOnMenuItem("Export");
        
        // export dialog
        assertTrue(solo.waitForText("Export"));
        assertEquals(1, solo.getCurrentEditTexts().size());
        solo.clickOnText("Confirm");
        
        assertTrue(solo.searchText("not"));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether appointments without a name can be added.
     * 
     * @throws Exception
     */
    public void testAddEmpty() throws Exception {
        solo.clickOnMenuItem("New");
        
        // new appointment dialog
        assertTrue(solo.waitForText("Add new"));
        assertEquals(1, solo.getCurrentDatePickers().size());
        solo.setDatePicker(solo.getCurrentDatePickers().get(0), APPOINTMENT_YEAR, APPOINTMENT_MONTH, APPOINTMENT_DAY);
        assertEquals(5, solo.getCurrentEditTexts().size());
        assertEquals(3, solo.getCurrentRadioButtons().size());
        solo.clickOnText("middle");
        solo.clickOnText("Confirm");
        
        assertTrue(solo.searchText("add a name"));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
    
    /**
     * Tests whether you can import an invalid date.
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
            fw.append("VERSION:2.0");
            fw.append("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP");
            fw.append("BEGIN:VTODO");
            fw.append("SUMMARY:" + IL_APPOINTMENT_NAME);
            fw.append("DESCRIPTION:" + IL_APPOINTMENT_DESC);
            fw.append("PRIORITY:1");
            fw.append("DTSTAMP:20130231T000000Z");
            fw.append("END:VTODO");
            fw.append("END:VCALENDAR");
        } finally {
            fw.close();
        }
        
        solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(solo.waitForText("Import"));
        solo.clickOnText(EXPORT_NAME);
        assertFalse(solo.searchText("succeeded"));
        
        assertFalse(solo.searchText(IL_APPOINTMENT_NAME));
        assertFalse(solo.searchText(IL_APPOINTMENT_DESC));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
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
            fw.append("VERSION:2.0");
            fw.append("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP");
            fw.append("BEGIN:VTODO");
            fw.append("SUMMARY:" + IL_APPOINTMENT_NAME);
            fw.append("DESCRIPTION:" + IL_APPOINTMENT_DESC);
            fw.append("PRIORITY:1025");
            fw.append("DTSTAMP:20130627T000000Z");
            fw.append("END:VTODO");
            fw.append("END:VCALENDAR");
        } finally {
            fw.close();
        }
        
        solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(solo.waitForText("Import"));
        solo.clickOnText(EXPORT_NAME);
        assertFalse(solo.searchText("succeeded"));
        
        assertFalse(solo.searchText(IL_APPOINTMENT_NAME));
        assertFalse(solo.searchText(IL_APPOINTMENT_DESC));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
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
            fw.append("VERSION:20.0");
            fw.append("PRODID:BETTER_THAN_PMP_CALENDAR");
            fw.append("BEGIN:VTODO");
            fw.append("SUMMARY:" + IL_APPOINTMENT_NAME);
            fw.append("DESCRIPTION:" + IL_APPOINTMENT_DESC);
            fw.append("PRIORITY:1");
            fw.append("DTSTAMP:20130627T000000Z");
            fw.append("END:VTODO");
            fw.append("END:VCALENDAR");
        } finally {
            fw.close();
        }
        
        solo.clickOnMenuItem("Import");
        
        // import dialog
        assertTrue(solo.waitForText("Import"));
        solo.clickOnText(EXPORT_NAME);
        assertFalse(solo.searchText("succeeded"));
        
        assertFalse(solo.searchText(IL_APPOINTMENT_NAME));
        assertFalse(solo.searchText(IL_APPOINTMENT_DESC));
        
        solo.assertCurrentActivity("Not CalendarAppActivity", CalendarAppActivity.class);
    }
    
}
