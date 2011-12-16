package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

/**
 * Users can register an account when register form is filled in correctly
 * 
 * @author andres
 * 
 */
public class RegisterActivity extends Activity {

	EditText et_username;
	EditText et_email;
	EditText et_password;
	EditText et_pw_confirm;
	EditText et_firstname;
	EditText et_lastname;
	EditText et_mobile;
	EditText et_desc;

	boolean correctForm = false;
	boolean registrationSuccessfull = false;

	private final Pattern email_pattern = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
	private final Pattern mobile_pattern = Pattern
			.compile("^[+]?[0-9]{10,13}$");

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		et_username = (EditText) findViewById(R.id.et_username);
		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_pw);
		et_pw_confirm = (EditText) findViewById(R.id.et_pw_confirm);
		et_firstname = (EditText) findViewById(R.id.et_firstname);
		et_lastname = (EditText) findViewById(R.id.et_lastname);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_desc = (EditText) findViewById(R.id.et_description);

		validator();
		register();

	}

	private void validator() {
		et_pw_confirm.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				if (et_pw_confirm.getText().toString()
						.equals(et_password.getText().toString())) {
					correctForm = true;
				} else {
					correctForm = false;
					et_pw_confirm.setError("Passwords do not match");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

		});
		et_email.addTextChangedListener(new InputValidator(et_email, ""));
		et_mobile.addTextChangedListener(new InputValidator(et_mobile, ""));
	}

	private void register() {
		Button register = (Button) findViewById(R.id.Button_Register);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Controller ctrl = new Controller();
				Map<String, String> map = new HashMap<String, String>();

				EditText et_username = (EditText) findViewById(R.id.et_username);

				et_email = (EditText) findViewById(R.id.et_email);
				EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
				EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
				EditText et_mobile = (EditText) findViewById(R.id.et_mobile);
				EditText et_desc = (EditText) findViewById(R.id.et_description);

				map.put("username", et_username.getText().toString());
				map.put("password", et_password.getText().toString());
				map.put("email", et_email.getText().toString());
				map.put("firstname", et_firstname.getText().toString());
				map.put("lastname", et_lastname.getText().toString());
				map.put("tel", et_mobile.getText().toString());
				map.put("description", et_desc.getText().toString());

				if (correctForm) {
					ctrl.register(map);

					Toast.makeText(RegisterActivity.this,
							"Registration successfull", Toast.LENGTH_LONG)
							.show();
					Intent intent = new Intent(RegisterActivity.this,
							MainActivity.class);
					RegisterActivity.this.startActivity(intent);
				} else {
					Toast.makeText(RegisterActivity.this,
							"Registration failed. Please check input",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	/**
	 * validate user input in register form
	 * 
	 * @author andres
	 * 
	 */
	private class InputValidator implements TextWatcher {

		private EditText editText;
		private String t;

		public InputValidator(EditText editText, String toMatch) {
			this.editText = editText;
			t = toMatch;
		}

		@Override
		public void afterTextChanged(Editable s) {
			switch (editText.getId()) {
			case R.id.et_email: {
				if (!email_pattern.matcher(editText.getText().toString())
						.matches()) {
					editText.setError("Invalid email");
					correctForm = false;
				} else {
					correctForm = true;
				}
			}
				break;

			case R.id.et_mobile: {
				if (!mobile_pattern.matcher(editText.getText().toString())
						.matches()) {
					editText.setError("Invalid phone number");
					correctForm = false;
				} else {
					correctForm = true;
				}
			}
				break;

			case R.id.et_pw_confirm: {
				if (editText.getText().toString().equals(t)) {
					correctForm = true;
				} else {
					editText.setError("Passwords do not match");
					correctForm = false;
				}
			}
				break;
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

	}

}
