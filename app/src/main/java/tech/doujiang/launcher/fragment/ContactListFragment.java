package tech.doujiang.launcher.fragment;

import android.Manifest;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.activity.ContactListActivity;
import tech.doujiang.launcher.adapter.ContactListAdapter;
import tech.doujiang.launcher.database.WorkspaceDBHelper;
import tech.doujiang.launcher.model.ContactBean;
import tech.doujiang.launcher.view.QuickAlphabeticBar;

public class ContactListFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_READ_CONTACT = 100;

    private ContactListAdapter adapter;
    private ListView contactListView;
    private List<ContactBean> contactList;
    private WorkspaceDBHelper dbHelper;
    private QuickAlphabeticBar alphabeticBar;

    private Map<Integer, ContactBean> contactIdMap = null;

//    private OnFragmentInteractionListener mListener;

    public ContactListFragment() {
    }

    public static ContactListFragment newInstance(String param1, String param2) {
        ContactListFragment fragment = new ContactListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        contactListView = (ListView) view.findViewById(R.id.contact_list);
        alphabeticBar = (QuickAlphabeticBar) view.findViewById(R.id.fast_scroller);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACT);
        }
        init();
        return view;
    }

    private void init() {
        dbHelper = WorkspaceDBHelper.getDBHelper(getActivity());
        contactList = dbHelper.getContact(dbHelper);
        dbHelper.close();
        if (contactList.size() > 0) {
            setAdapter(contactList);
        }
    }

    private void setAdapter(List<ContactBean> contactList) {
        adapter = new ContactListAdapter(getActivity(), contactList, alphabeticBar);
        contactListView.setAdapter(adapter);
        alphabeticBar.init(getActivity());
        alphabeticBar.setListView(contactListView);
        alphabeticBar.setHeight(alphabeticBar.getHeight());
        alphabeticBar.setVisibility(View.VISIBLE);
    }

    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }
    // No need to inetract between fragments.
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
