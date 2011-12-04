package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;

/**
 * LoginActivity the startup activity for vHike
 * 
 * @author Andre Nguyen
 * 
 */
public class LoginActivity extends Activity {

	private Controller ctrl;
	private EditText et_username;
	private EditText et_pw;
	private String username;
	private String pw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		registerLink();

		Button btnLogin = (Button) findViewById(R.id.button_login);

		ctrl = new Controller();
		et_username = (EditText) findViewById(R.id.edit_login);
		et_pw = (EditText) findViewById(R.id.edit_password);
		username = et_username.getText().toString();
		pw = et_pw.getText().toString();

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (username != null && pw != null) {
					if (ctrl.login(username, pw)) {
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						LoginActivity.this.startActivity(intent);
					} else {
						Toast.makeText(LoginActivity.this, "Login failed",
								Toast.LENGTH_LONG);
					}
				} else {
					Toast.makeText(LoginActivity.this,
							"Username or password field empty",
							Toast.LENGTH_LONG);
				}
			}
		});
	}

	private void registerLink() {
		TextView view_register = (TextView) findViewById(R.id.view_register);
		String text = "Register";
		view_register.setText(text);
		// Turn pattern "Register" into clickable
		Pattern pattern = Pattern.compile("Register");
		// prefix our pattern with http://
		// Linkify.addLinks(view_register, pattern, "http://www.google.com");
	}

}
