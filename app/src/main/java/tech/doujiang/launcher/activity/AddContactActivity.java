package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import tech.doujiang.launcher.R;
import tech.doujiang.launcher.database.WorkspaceDBHelper;

public class AddContactActivity extends AppCompatActivity {
    private WorkspaceDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        dbHelper = WorkspaceDBHelper.getDBHelper(this.getApplicationContext());
    }


}
