package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageSwitcher;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.database.WorkspaceDBHelper;

public class AddContactActivity extends AppCompatActivity implements OnClickListener{
    private WorkspaceDBHelper dbHelper;

    private ImageSwitcher imageSwitcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        dbHelper = WorkspaceDBHelper.getDBHelper(this.getApplicationContext());
    }

    @Override
    public void onClick(View view) {

    }

    public boolean verify() {
        return false;
    }

}
