package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;

/**
 * LoginActivity: the startup activity for vHike
 * 
 * @author Andre Nguyen
 * 
 */
public class LoginActivity extends Activity {

	private String username;
	private String pw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		registerLink();

		Button btnLogin = (Button) findViewById(R.id.button_login);

		final Controller ctrl = new Controller();
		final EditText et_username = (EditText) findViewById(R.id.edit_login);
		final EditText et_pw = (EditText) findViewById(R.id.edit_password);

		username = "";
		pw = "";

		// ------------Provisorisch-----------
		et_username.setText("demo");
		et_pw.setText("test");
		// ------------Provisorisch-----------

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				username = et_username.getText().toString();
				pw = et_pw.getText().toString();

				if (username.equals("") || pw.equals("")) {
					Toast.makeText(LoginActivity.this,
							"Username or password field empty",
							Toast.LENGTH_LONG).show();
				} else {

					if (ctrl.login(username, pw)) {

						Toast.makeText(LoginActivity.this, "Login successful",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent(v.getContext(),
								MainActivity.class);
						v.getContext().startActivity(intent);

					} else {
						Toast.makeText(
								LoginActivity.this,
								"Login failed. Username or password not valid.",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}

	/**
	 * Set up Button for registration, starts RegisterActivity
	 */
	private void registerLink() {
		Button button_register = (Button) findViewById(R.id.button_register);
		button_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});
	}

	public boolean isConnected() {
		@SuppressWarnings("static-access")
		ConnectivityManager cm = (ConnectivityManager) getSystemService(LoginActivity.this.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnected();
	}

}
