package de.unistuttgart.ipvs.pmp.gui.tab;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.placeholder.App;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;


public class AppDetailsTab extends Activity {
    
    private App app;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.pmp_app_details);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        
      //app = handleIntent(getIntent());
        app = new App(
                "Barcode Scanner",
                "With the Barcode Scanner you can scan your products and get more informations about them. "
                        + "Especially you can find the best price for products. If you enable the Location Feature you "
                        + "can also get the direction to the next store where the item is available. Facebook Feature "
                        + "allows you to share the product with your friends.", BitmapFactory.decodeResource(
                        getResources(), R.drawable.test_icon1));
        
        TextView description = (TextView) findViewById(R.id.TextView_Description);
        description.setText(app.getDescription());
    }
}
