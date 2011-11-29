package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.regex.Pattern;
import de.unistuttgart.ipvs.pmp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * LoginActivity the startup activity for vHike
 * 
 * @author Andre Nguyen
 * 
 */
public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		registerLink();

		Button btnLogin = (Button) findViewById(R.id.button_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				LoginActivity.this.startActivity(intent);
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
		Linkify.addLinks(view_register, pattern, "http://www.google.com");
	}

}
