package tw.com.lixin.wmessenger;

import android.widget.TextView;

import tw.com.atromoby.collectionview.CollectionView;
import tw.com.atromoby.collectionview.Item;
import tw.com.atromoby.collectionview.ItemHolder;

public class RecordView extends ItemHolder {

    TextView carr;

    public RecordView(CollectionView cv) {
        super(cv, R.layout.carpark_card);
        carr = findView(R.id.serveTime);
    }

    @Override
    public void init(Item item) {
        carr.setText(item.title);
    }

    @Override
    public void cleanUp() {

    }
}
