package com.pabloogc.bequ.app.screens.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dropbox.client2.DropboxAPI;
import com.google.gson.Gson;
import com.pabloogc.bequ.app.R;
import com.pabloogc.bequ.app.screens.meta.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Pablo Orgaz - 4/12/14 - pabloogc@gmail.com - https://github.com/pabloogc
 *
 * Dummy fragment that only has and image of the book, always the same since dropbox does not
 * provide thumbs to show (that information is contained inside the epub metadata if any).
 *
 */
public class BookDetailFragment extends BaseFragment {

    protected @InjectView(R.id.bookImage) ImageView image;
    private DropboxAPI.Entry entry;

    public static BookDetailFragment newInstance(DropboxAPI.Entry entry) {
        Bundle args = new Bundle();
        args.putString("entry", new Gson().toJson(entry));
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entry = new Gson().fromJson(getArguments().getString("entry"), DropboxAPI.Entry.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.book_detail_fragment, null, false);
        ButterKnife.inject(this, root);
        //TODO: Actually use the entry data to show de image, that information does not exists in dropbox
        image.setImageResource(R.drawable.ic_book);
        return root;
    }
}
