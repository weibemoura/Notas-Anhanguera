package br.com.ficux.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.ficux.entidade.Unidade;
import java.util.List;

public class TextViewAdapter<T> extends ArrayAdapter<T> {
    private int textViewResourceId;

    public TextViewAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
        this.textViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        LayoutInflater mInflator = (LayoutInflater) getContext().getSystemService("layout_inflater");
        if (convertView == null) {
            row = mInflator.inflate(this.textViewResourceId, null);
        } else {
            row = convertView;
        }
        ((TextView) row.findViewById(16908308)).setText(getText(getItem(position)));
        return row;
    }

    public String getText(T item) {
        return ((Unidade) item).getNome();
    }
}
