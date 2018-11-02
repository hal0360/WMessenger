package tw.com.lixin.wmessenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import tw.com.atromoby.collectionview.CollectionView;
import tw.com.atromoby.collectionview.Item;

import tw.com.atromoby.rootactivity.RootActivity;

public class MainActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CollectionView.holders.put(1, RecordView::new);

        List<Item> items = new ArrayList<>();
        items.add(new Item(1,"blue"));
        items.add(new Item(1,"red"));
        items.add(new Item(1,"yellow"));

        CollectionView collectionView = findViewById(R.id.recordCollView);
        collectionView.init(items);

        SmackService.login(this,"warboss","warhammer");
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("custom-event-name"));
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            alert(message);
        }
    };
}
