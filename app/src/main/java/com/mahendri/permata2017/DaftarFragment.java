package com.mahendri.permata2017;


import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mahendri.permata2017.data.TonasContract;
import com.mahendri.permata2017.data.TonasCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DaftarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int PENDAFTAR_LOADER = 0;
    TonasCursorAdapter mCursorAdapater;


    public DaftarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflateView = inflater.inflate(R.layout.fragment_daftar, container, false);

        ListView daftarTonas = (ListView) inflateView.findViewById(R.id.list_view);
        mCursorAdapater = new TonasCursorAdapter(getActivity(),null);
        daftarTonas.setAdapter(mCursorAdapater);

        getActivity().getLoaderManager().initLoader(PENDAFTAR_LOADER, null, this);

        daftarTonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),EditActivity.class);
                intent.setData(ContentUris.withAppendedId(TonasContract.PendaftarEntry.CONTENT_URI, id));
                startActivity(intent);
            }
        });

        return inflateView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TonasContract.PendaftarEntry._ID,
                TonasContract.PendaftarEntry.COLUMN_DAFTAR_NAMA,
                TonasContract.PendaftarEntry.COLUMN_DAFTAR_SEKOLAH};

        return new CursorLoader(getActivity(),
                TonasContract.PendaftarEntry.CONTENT_URI,
                projection,null,null, TonasContract.PendaftarEntry._ID);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapater.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapater.swapCursor(null);
    }
}
