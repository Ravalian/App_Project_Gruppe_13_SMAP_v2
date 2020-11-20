package com.example.smap_app_project_grp_13_carlog.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smap_app_project_grp_13_carlog.R;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class YourLogsAdapter extends RecyclerView.Adapter<YourLogsAdapter.YourLogsViewHolder> {

    public interface IYourLogsItemClickedListener {
        void IYourLogsClicked(int YLID);
    }

    private IYourLogsItemClickedListener listener;

    /*
    private List<DBPlaceHolder> YLList = new List<DBPlaceHolder>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<DBPlaceHolder> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] ts) {
            return null;
        }

        @Override
        public boolean add(DBPlaceHolder dbPlaceHolder) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends DBPlaceHolder> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, @NonNull Collection<? extends DBPlaceHolder> collection) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public DBPlaceHolder get(int i) {
            return null;
        }

        @Override
        public DBPlaceHolder set(int i, DBPlaceHolder dbPlaceHolder) {
            return null;
        }

        @Override
        public void add(int i, DBPlaceHolder dbPlaceHolder) {

        }

        @Override
        public DBPlaceHolder remove(int i) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<DBPlaceHolder> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<DBPlaceHolder> listIterator(int i) {
            return null;
        }

        @NonNull
        @Override
        public List<DBPlaceHolder> subList(int i, int i1) {
            return null;
        }
    };
    */

    public YourLogsAdapter(IYourLogsItemClickedListener listener) { this.listener = listener; }

    @NonNull
    @Override
    public YourLogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_your_logs_list_item, parent, false);
        YourLogsViewHolder vh = new YourLogsViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull YourLogsAdapter.YourLogsViewHolder holder, int position) {

    }

    public class YourLogsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //ViewHolder ui widget references

        //constructor
        public YourLogsViewHolder(@NonNull View itemView, IYourLogsItemClickedListener YourLogsItemClickedListener) {
            super(itemView);

            //get references from the layout file
            listener = YourLogsItemClickedListener;

            //set click listener for whole list item
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //listener.onYourLogsAdapterClicked(YLList.get(getAdapterPosition()).getYLID());
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
