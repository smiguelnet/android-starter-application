package net.smiguel.app.view.adapter;

public interface EntityListAdapter<T> {

    public T getItemSelected(int position);

    public void removeAt(int position);
}
