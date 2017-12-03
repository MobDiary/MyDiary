package com.kiminonawa.mydiary.entries.entries;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiminonawa.mydiary.R;
import com.kiminonawa.mydiary.entries.BaseDiaryFragment;
import com.kiminonawa.mydiary.entries.DiaryActivity;
import com.kiminonawa.mydiary.entries.photo.PhotoOverviewActivity;
import com.kiminonawa.mydiary.setting.SettingActivity;
import com.kiminonawa.mydiary.shared.ThemeManager;
import com.kiminonawa.mydiary.shared.ViewTools;


public class EntriesFragment extends BaseDiaryFragment implements
        DiaryViewerDialogFragment.DiaryViewerCallback, View.OnClickListener {

    /**
     * UI
     */
    private RelativeLayout RL_main_bottom_bar;
    private TextView TV_entries_edit_msg;
    private ImageView IV_main_setting;
    /**
     * RecyclerView
     */
    private RecyclerView RecyclerView_entries;
    private EntriesAdapter entriesAdapter;

    public EntriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entries, container, false);

        TV_entries_edit_msg = (TextView) rootView.findViewById(R.id.TV_entries_edit_msg);
        TV_entries_edit_msg.setTextColor(ThemeManager.getInstance().getThemeMainColor(getActivity()));

        RecyclerView_entries = (RecyclerView) rootView.findViewById(R.id.RecyclerView_entries);
        RL_main_bottom_bar = (RelativeLayout) rootView.findViewById(R.id.RL_main_bottom_bar);
        RL_main_bottom_bar.setBackgroundColor(ThemeManager.getInstance().getThemeMainColor(getActivity()));

        IV_main_setting = (ImageView)rootView.findViewById(R.id.IV_main_setting);
        IV_main_setting.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void initRecyclerView() {
        LinearLayoutManager lmr = new LinearLayoutManager(getActivity());
        RecyclerView_entries.setLayoutManager(lmr);
        entriesAdapter = new EntriesAdapter(EntriesFragment.this, getEntriesList());
        RecyclerView_entries.setAdapter(entriesAdapter);
        //true for close all view
        setBottomBarUI();
    }


    public void setBottomBarUI() {

            entriesAdapter.setEditMode(false);
            TV_entries_edit_msg.setVisibility(View.GONE);
            IV_main_setting.setImageDrawable(ViewTools.getDrawable(getActivity(), R.drawable.ic_mode_edit_white_24dp));

    }

    public void gotoDiaryPosition(int position) {
        RecyclerView_entries.scrollToPosition(position);
    }

    public void updateEntriesData() {
        updateEntriesList();
        entriesAdapter.notifyDataSetChanged();
        ((DiaryActivity) getActivity()).callCalendarRefresh();
    }

    @Override
    public void deleteDiary() {
        updateEntriesData();
    }

    @Override
    public void updateDiary() {
        updateEntriesData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.EDT_main_hashtag_search:
                break;
            case R.id.IV_main_setting:
                Intent gotoSettingIntent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(gotoSettingIntent);
                break;

        }
    }
}
