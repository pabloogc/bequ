package com.pabloogc.bequ.app.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.RESTUtility;
import com.pabloogc.bequ.app.R;
import com.pabloogc.bequ.app.screens.meta.BaseFragment;
import com.pabloogc.playa.toolbox.Time;
import com.pabloogc.playa.toolbox.WLoadingLayout;
import com.pabloogc.playa.handlers.SuccessHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 * <p/>
 * The fragment displaying a list of books stored in dropbox.
 */
public class HomeFragment extends BaseFragment {

    protected @InjectView(R.id.booksListView) ListView listView;
    protected @InjectView(R.id.loadingLayout) WLoadingLayout loadingLayout;
    protected List<DropboxAPI.Entry> data = new ArrayList<DropboxAPI.Entry>(0);
    protected BookAdapter adapter;

    /**
     * Remove false search results created by the OS like ._ files since dropbox search matches
     * using {@link java.lang.String#contains(CharSequence)} and we want files with a specified
     * extension
     *
     * @param items the items to filter
     * @param ext   the extension required
     * @return filtered books
     */
    private static List<DropboxAPI.Entry> filterBooks(List<DropboxAPI.Entry> items, String ext) {
        List<DropboxAPI.Entry> filter = new ArrayList<DropboxAPI.Entry>();
        for (DropboxAPI.Entry book : items) {
            if (book.fileName().endsWith(ext) && !book.fileName().startsWith("._"))
                filter.add(book);
        }
        return filter;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        loadingLayout.setOnRetryClickListener(new WLoadingLayout.OnRetryClickListener() {
            @Override public void onRetry(View view) {
                doSearch(true);
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookDetailFragment fragment = BookDetailFragment.newInstance(data.get(position));
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(
                                R.animator.slide_in_right, R.animator.slide_out_left,
                                R.animator.slide_in_left, R.animator.slide_out_right)
                        .replace(R.id.fragmentContainer, fragment)
                        .addToBackStack("/")
                        .commit();
            }
        });
        return root;
    }


    /**
     * Perform the dropbox query to find epub files
     *
     * @param force if it should skip the cached data and go to network. Data is cached for 1 hour.
     */
    private void doSearch(boolean force) {
        if (force || data.isEmpty())
            //Searching pdf instead of epub for testing purposes
            getApiHelper().search("/", ".pdf", 0, false)
                    .success(new SuccessHandler<List<DropboxAPI.Entry>>() {
                        @Override public void onSuccess(List<DropboxAPI.Entry> result) {
                            data = filterBooks(result, ".pdf");
                            adapter.notifyDataSetChanged();
                        }

                        @Override public String getSuccessMessage(List<DropboxAPI.Entry> result) {
                            return String.format("Found %d", result.size());
                        }
                    })
                    .loading(loadingLayout)
                    .cacheSkip(force)
                    .cacheTime(Time.hours(1))
                    .cacheKey("search/.pdf")
                    .execute();
    }

    ///////////////////////////////
    //Data/List related
    ///////////////////////////////

    /**
     * Sort the books by modification date provided by dropbox
     */
    public void sortByDate() {
        if (data != null) {
            Collections.sort(data, new Comparator<DropboxAPI.Entry>() {
                @Override public int compare(DropboxAPI.Entry lhs, DropboxAPI.Entry rhs) {
                    //FIXME: This is very slow, the parse Date should be cached
                    return RESTUtility.parseDate(lhs.modified).compareTo(RESTUtility.parseDate(rhs.modified));
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Sort the books by filename. Sorting the books by book name requires downloading the whole file
     * to read the metadata so it's not a good idea unless explicitly requested by the user.
     */
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

    static class BookHolder {
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
            BookHolder holder;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.book_list_item, null);
                convertView.setDrawingCacheEnabled(true);
                holder = new BookHolder();
                convertView.setTag(holder);
                ButterKnife.inject(holder, convertView);
            } else {
                holder = (BookHolder) convertView.getTag();
            }
            holder.title.setText(data.get(position).fileName());
            //no image, all books have the same since dropbox does not give thumbs
            return convertView;
        }
    }
}
