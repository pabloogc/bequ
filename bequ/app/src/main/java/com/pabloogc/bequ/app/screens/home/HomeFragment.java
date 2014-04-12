package com.pabloogc.bequ.app.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.pabloogc.bequ.app.R;
import com.pabloogc.bequ.app.screens.meta.BaseFragment;
import com.pabloogc.bequ.app.util.Time;
import com.pabloogc.bequ.app.widget.WLoadingLayout;
import com.pabloogc.playa.handlers.SuccessHandler;

import java.util.ArrayList;
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

    @Override public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doSearch();
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.books_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.inject(this, root);
        adapter = new BookAdapter();
        listView.setAdapter(adapter);
        root.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                doSearch();
            }
        });
        return root;
    }

    public void doSearch() {
        getApiHelper().search("/", ".pdf", 100, false)
                .success(new SuccessHandler<List<DropboxAPI.Entry>>() {
                    @Override public void onSuccess(List<DropboxAPI.Entry> result) {
                        data = filterBooks(result);
                        adapter.notifyDataSetChanged();
                    }

                    @Override public String getSuccessMessage(List<DropboxAPI.Entry> response) {
                        return String.format("Found %d", response.size());
                    }
                })
                .loading(loadingLayout)
                .cacheTime(Time.seconds(30), Time.seconds(120))
                .execute();

    }

    private List<DropboxAPI.Entry> filterBooks(List<DropboxAPI.Entry> items) {
        List<DropboxAPI.Entry> filter = new ArrayList<DropboxAPI.Entry>();
        for (DropboxAPI.Entry book : items) {
            if (book.fileName().endsWith(".pdf") && !book.fileName().startsWith("._"))
                filter.add(book);
        }
        return filter;
    }

    protected static class Holder {
        @InjectView(R.id.bookTitle) TextView title;
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
