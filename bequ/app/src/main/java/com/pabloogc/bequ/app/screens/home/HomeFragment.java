package com.pabloogc.bequ.app.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.RESTUtility;
import com.pabloogc.bequ.app.R;
import com.pabloogc.bequ.app.screens.meta.BaseFragment;
import com.pabloogc.bequ.app.util.Time;
import com.pabloogc.bequ.app.widget.WLoadingLayout;
import com.pabloogc.playa.handlers.SuccessHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 */
public class HomeFragment extends BaseFragment {

    protected @InjectView(R.id.booksListView) ListView listView;
    protected @InjectView(R.id.loadingLayout) WLoadingLayout loadingLayout;
    protected List<DropboxAPI.Entry> data = new ArrayList<DropboxAPI.Entry>(0);
    protected BookAdapter adapter;

    private static List<DropboxAPI.Entry> filterBooks(List<DropboxAPI.Entry> items) {
        List<DropboxAPI.Entry> filter = new ArrayList<DropboxAPI.Entry>();
        for (DropboxAPI.Entry book : items) {
            if (book.fileName().endsWith(".pdf") && !book.fileName().startsWith("._"))
                filter.add(book);
        }
        return filter;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doSearch(false);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.books_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_by_name:
                sortByName();
                return true;
            case R.id.action_sort_by_date:
                sortByDate();
                return true;
            case R.id.action_refresh:
                doSearch(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.inject(this, root);
        adapter = new BookAdapter();
        listView.setAdapter(adapter);
        loadingLayout.setOnRetryClickListener(new WLoadingLayout.OnRetryClickListener() {
            @Override public void onRetry(View view) {
                doSearch(true);
            }
        });
        return root;
    }

    public void doSearch(boolean force) {
        getApiHelper().search("/", ".pdf", 1000, false)
                .success(new SuccessHandler<List<DropboxAPI.Entry>>() {
                    @Override public void onSuccess(List<DropboxAPI.Entry> result) {
                        data = filterBooks(result);
                        adapter.notifyDataSetChanged();
                    }

                    @Override public String getSuccessMessage(List<DropboxAPI.Entry> response) {
                        return String.format("Found %d", response.size());
                    }
                })
                .cacheSkip(force)
                .loading(loadingLayout)
                .cacheTime(Time.minutes(10))
                .cacheKey("search/.pdf")
                .execute();

    }

    public void sortByName() {
        if (data != null) {
            Collections.sort(data, new Comparator<DropboxAPI.Entry>() {
                @Override public int compare(DropboxAPI.Entry lhs, DropboxAPI.Entry rhs) {
                    return lhs.fileName().toLowerCase().compareTo(rhs.fileName().toLowerCase());
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

    public void sortByDate() {
        if (data != null) {
            Collections.sort(data, new Comparator<DropboxAPI.Entry>() {
                @Override public int compare(DropboxAPI.Entry lhs, DropboxAPI.Entry rhs) {
                    //This is very slow, the parse should be cached
                    return RESTUtility.parseDate(lhs.modified).compareTo(RESTUtility.parseDate(rhs.modified));
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

    protected static class Holder {
        @InjectView(R.id.bookTitle) TextView title;
        @InjectView(R.id.bookImage) ImageView image;
    }

    private class BookAdapter extends BaseAdapter {

        @Override public int getCount() {
            return data.size();
        }

        @Override public Object getItem(int position) {
            return data.get(position);
        }

        @Override public long getItemId(int position) {
            return data.get(position).fileName().hashCode();
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.book_list_item, null);
                holder = new Holder();
                convertView.setTag(holder);
                ButterKnife.inject(holder, convertView);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.title.setText(data.get(position).fileName());
            return convertView;
        }
    }
}
