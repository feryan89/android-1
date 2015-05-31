package com.i906.mpt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.i906.mpt.MptApplication;
import com.i906.mpt.R;
import com.i906.mpt.extension.ExtensionInfo;
import com.i906.mpt.extension.ExtensionInfo.Screen;
import com.i906.mpt.util.preference.GeneralPrefs;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ExtensionsAdapter extends RecyclerView.Adapter<ExtensionsAdapter.ViewHolder> {

    @Inject
    protected GeneralPrefs mPrefs;

    protected List<ExtensionInfo> mList;
    protected List<ScreenHolder> mScreenList;

    protected String mSelectedScreen;

    public ExtensionsAdapter(Context context) {
        MptApplication.component(context).inject(this);
        mScreenList = new ArrayList<>();
        mSelectedScreen = mPrefs.getSelectedPrayerView();
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_extension, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScreenHolder sh = getItem(position);
        holder.setScreen(sh.screen);
        holder.setExtension(sh.extension);
        holder.selected.setChecked(mSelectedScreen.equals(sh.screen.getView()));
    }

    public void setExtensionList(List<ExtensionInfo> list) {
        mList = list;

        for (ExtensionInfo ei : mList) {
            for (Screen s : ei.getScreens()) {

                if (s == null) continue;
                if (s.getView() == null) continue;

                ScreenHolder sh = new ScreenHolder();
                sh.extension = ei;
                sh.screen = s;
                mScreenList.add(sh);
            }
        }

        notifyDataSetChanged();
    }

    public ScreenHolder getItem(int position) {
        return mScreenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).screen.getView().hashCode();
    }

    @Override
    public int getItemCount() {
        return mScreenList.size();
    }

    public boolean isEmpty() {
        return mScreenList.isEmpty();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected ExtensionsAdapter adapter;
        protected ExtensionInfo extension;
        protected Screen screen;

        @InjectView(R.id.rb_selected)
        protected RadioButton selected;

        @InjectView(R.id.tv_name)
        protected TextView name;

        @InjectView(R.id.tv_author)
        protected TextView author;

        public ViewHolder(View itemView, ExtensionsAdapter adapter) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            this.adapter = adapter;
        }

        public void setExtension(ExtensionInfo item) {
            extension = item;
            author.setText(item.getAuthor());
        }

        public void setScreen(Screen item) {
            screen = item;
            name.setText(item.getName());
        }

        @OnClick(R.id.list_item)
        protected void onScreenSelected() {
            adapter.mSelectedScreen = screen.getView();
            adapter.mPrefs.setSelectedPrayerView(screen.getView());
            adapter.notifyDataSetChanged();
        }
    }

    static class ScreenHolder {
        public ExtensionInfo extension;
        public Screen screen;
    }
}
