package de.unistuttgart.ipvs.pmp.test.api.app;

/*
 * public class AppTest extends ActivityInstrumentationTestCase2<ActivityMain> {
 * 
 * public AppTest() {
 * super("de.unistuttgart.ipvs.pmp", ActivityMain.class);
 * }
 * 
 * public static Bundle sf_ttt;
 * public static Bundle sf_tfx;
 * public static Bundle sf_fff;
 * 
 * public static final String SF_1 = "TEST_SF_1";
 * public static final String SF_2 = "TEST_SF_2";
 * public static final String SF_3 = "TEST_SF_3";
 * 
 * 
 * @Override
 * protected void setUp() throws Exception {
 * sf_ttt = new Bundle();
 * sf_ttt.putBoolean(SF_1, true);
 * sf_ttt.putBoolean(SF_2, true);
 * sf_ttt.putBoolean(SF_3, true);
 * 
 * sf_tfx = new Bundle();
 * sf_tfx.putBoolean(SF_1, true);
 * sf_tfx.putBoolean(SF_2, false);
 * 
 * sf_fff = new Bundle();
 * sf_fff.putBoolean(SF_1, false);
 * sf_fff.putBoolean(SF_2, false);
 * sf_fff.putBoolean(SF_3, false);
 * }
 * 
 * 
 * public void testServiceFeatureStorage() throws Exception {
 * App test = new App() {
 * 
 * @Override
 * public void onRegistrationSuccess() {
 * }
 * 
 * 
 * @Override
 * public void onRegistrationFailed(String message) {
 * }
 * 
 * 
 * @Override
 * public SharedPreferences getSharedPreferences(String name, int mode) {
 * return AppTest.this.getActivity().getSharedPreferences(name, mode);
 * }
 * 
 * };
 * test.updateServiceFeatures(sf_tfx);
 * 
 * // check results
 * assertTrue(test.isServiceFeatureEnabled(SF_1));
 * assertFalse(test.isServiceFeatureEnabled(SF_2));
 * assertFalse(test.isServiceFeatureEnabled(SF_3));
 * }
 * 
 * 
 * public void testServiceFeatureDeactivation() throws Exception {
 * App test = new App() {
 * 
 * @Override
 * public void onRegistrationSuccess() {
 * }
 * 
 * 
 * @Override
 * public void onRegistrationFailed(String message) {
 * }
 * 
 * 
 * @Override
 * public SharedPreferences getSharedPreferences(String name, int mode) {
 * return AppTest.this.getActivity().getSharedPreferences(name, mode);
 * }
 * 
 * };
 * test.updateServiceFeatures(sf_ttt);
 * test.updateServiceFeatures(sf_fff);
 * 
 * // check results
 * assertFalse(test.isServiceFeatureEnabled(SF_1));
 * assertFalse(test.isServiceFeatureEnabled(SF_2));
 * assertFalse(test.isServiceFeatureEnabled(SF_3));
 * }
 * 
 * 
 * public void testServiceFeatureActivation() throws Exception {
 * App test = new App() {
 * 
 * @Override
 * public void onRegistrationSuccess() {
 * }
 * 
 * 
 * @Override
 * public void onRegistrationFailed(String message) {
 * }
 * 
 * 
 * @Override
 * public SharedPreferences getSharedPreferences(String name, int mode) {
 * return AppTest.this.getActivity().getSharedPreferences(name, mode);
 * }
 * 
 * };
 * test.updateServiceFeatures(sf_fff);
 * test.updateServiceFeatures(sf_ttt);
 * 
 * // check results
 * assertTrue(test.isServiceFeatureEnabled(SF_1));
 * assertTrue(test.isServiceFeatureEnabled(SF_2));
 * assertTrue(test.isServiceFeatureEnabled(SF_3));
 * }
 * 
 * }
 */
