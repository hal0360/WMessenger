package tw.com.lixin.wmessenger.viewHolders;

import android.widget.ImageView;
import android.widget.TextView;


import tw.com.atromoby.widgets.ItemHolder;
import tw.com.lixin.wmessenger.R;

public class FriendHolder extends ItemHolder {

    private TextView name;
    private ImageView avatar;
    private String userID;

    public FriendHolder(String uid) {
        super(R.layout.friend_card);
        userID = uid;
    }

    @Override
    public void onBind() {
        name = findView(R.id.userID);
        name.setText(userID);
        avatar = findView(R.id.avatar);
        clicked(avatar, v->alert("nigga"));
    }

    @Override
    public void onClean() {

    }
}
