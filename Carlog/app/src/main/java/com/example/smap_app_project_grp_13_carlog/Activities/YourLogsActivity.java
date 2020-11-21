package com.example.smap_app_project_grp_13_carlog.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.smap_app_project_grp_13_carlog.Fragments.YourLogsDetailsFragment;
import com.example.smap_app_project_grp_13_carlog.Fragments.YourLogsListFragment;
import com.example.smap_app_project_grp_13_carlog.Models.Logs;
import com.example.smap_app_project_grp_13_carlog.R;
import com.example.smap_app_project_grp_13_carlog.Interface.YourLogsSelectorInterface;

import java.util.ArrayList;

public class YourLogsActivity extends AppCompatActivity implements YourLogsSelectorInterface {

    //keeping track of phone mode (portrait or landscape) and user mode (which view the user has selected)
    public enum PhoneMode {PORTRAIT, LANDSCAPE}
    public enum UserMode {LIST_VIEW, DETAIL_VIEW}
    private PhoneMode phoneMode;
    private UserMode userMode;

    //tags -TOBE moved to Constants.java
    private static final String LIST_FRAG = "list_fragment";
    private static final String DETAILS_FRAG = "details_fragment";

    //the actual fragments we use
    private YourLogsListFragment yourLogsList;
    private YourLogsDetailsFragment yourLogsDetails;

    //containers we use to put out fragments in
    private LinearLayout listContainer;
    private LinearLayout detailsContainer;

    //list of logs
    private ArrayList<Logs> yourlogs;
    private int selectedLogIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_logs_mulitpane);

        //get container views
        listContainer = (LinearLayout)findViewById(R.id.YL_list_container);
        detailsContainer = (LinearLayout)findViewById(R.id.YL_details_container);

        //determine orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            phoneMode = PhoneMode.PORTRAIT;
        } else {
            phoneMode = PhoneMode.LANDSCAPE;
        }

        if (savedInstanceState == null) {
            //no persisted state, start the app in list view mode and selected index = 0
            selectedLogIndex = 0;
            userMode = UserMode.LIST_VIEW;

            //initialize fragments
            yourLogsList = new YourLogsListFragment();
            yourLogsDetails = new YourLogsDetailsFragment();

            yourLogsList.setLogs(yourlogs);
            yourLogsDetails.setLogs(yourlogs.get(selectedLogIndex));

            //add the first two fragments to container (one will be invisible if in portrait mode)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.YL_list_container, yourLogsList, LIST_FRAG)
                    .add(R.id.YL_details_container, yourLogsDetails, DETAILS_FRAG)
                    .commit();
        } else {
            //got restarted with persisted state, probably due to orientation change
            selectedLogIndex = savedInstanceState.getInt("Your_log_position");
            userMode = (UserMode) savedInstanceState.getSerializable("User_mode");

            if (userMode == null) {
                userMode = UserMode.LIST_VIEW; //default value if none saved
            }

            //check if FragmentManager already holds instance of Fragments, else create them
            yourLogsList = (YourLogsListFragment)getSupportFragmentManager().findFragmentByTag(LIST_FRAG);
            if (yourLogsList == null) {
                yourLogsList = new YourLogsListFragment();
            }
            yourLogsDetails = (YourLogsDetailsFragment)getSupportFragmentManager().findFragmentByTag(DETAILS_FRAG);
            if (yourLogsDetails == null) {
                yourLogsDetails = new YourLogsDetailsFragment();
            }
        }

        updateFragmentViewState(userMode);
    }

    @Override
    public void onBackPressed() {
        if (phoneMode == PhoneMode.LANDSCAPE) {
            finish();
        } else {
            if (userMode == UserMode.LIST_VIEW) {
                //return to prives activity
                finish();
            } else if (userMode == UserMode.DETAIL_VIEW) {
                //go back to list mode if in detail mode
                updateFragmentViewState(UserMode.LIST_VIEW);
            }
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("log_position", selectedLogIndex);
        outState.putSerializable("user_mode", userMode);
        super.onSaveInstanceState(outState);
    }

    private void updateFragmentViewState(UserMode targetMode) {
        //update view
        if (targetMode == UserMode.DETAIL_VIEW) {
            userMode = UserMode.DETAIL_VIEW;
            switchFragment(targetMode);
        } if (targetMode == UserMode.LIST_VIEW) {
            userMode = UserMode.LIST_VIEW;
            switchFragment(targetMode);
        }
    }

    private boolean switchFragment(UserMode targetMode) {
        if (phoneMode == PhoneMode.LANDSCAPE.PORTRAIT) {
            if (targetMode == UserMode.LIST_VIEW) {
                listContainer.setVisibility(View.VISIBLE);
                detailsContainer.setVisibility(View.GONE);
            } else if (targetMode == UserMode.DETAIL_VIEW) {
                listContainer.setVisibility(View.GONE);
                detailsContainer.setVisibility(View.VISIBLE);
            }
        }
        return true;
    }

    public void onYourLogSelected(int position) {
        if (yourLogsDetails != null) {
            Logs selectedLog = yourlogs.get(position);
            if (selectedLog != null) {
                selectedLogIndex = position;
                yourLogsDetails.setLogs(selectedLog);
            }
        }
        updateFragmentViewState(UserMode.DETAIL_VIEW);
    }

    //ved ikke om den skal bruges sådan her eller det er firebase
    public ArrayList<Logs> getYourLogsList() { return yourlogs; }

    public Logs getCurrentSelection() {
        if (yourlogs != null) {
            return yourlogs.get(selectedLogIndex);
        } else {
            return null;
        }
    }
}