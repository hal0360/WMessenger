package tw.com.lixin.wmessenger.viewHolders;

import android.widget.ImageView;
import android.widget.TextView;

import tw.com.atromoby.widgets.CollectionView;
import tw.com.atromoby.widgets.Item;
import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmessenger.R;

public class FriendHolder extends ItemHolder {

    TextView name;
    ImageView avatar;

    public FriendHolder(CollectionView cv) {
        super(cv, R.layout.friend_card);
        name = findView(R.id.userID);
        avatar = findView(R.id.avatar);
    }

    @Override
    public void init(Item item) {

    }

    @Override
    public void cleanUp() {

    }
}
