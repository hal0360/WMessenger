package tw.com.lixin.wmessenger;

import android.os.Bundle;
import android.widget.TextView;

import tw.com.atromoby.utils.LocalIntent;
import tw.com.atromoby.utils.LocalReceiver;
import tw.com.atromoby.widgets.Animate;
import tw.com.atromoby.widgets.CustomInput;
import tw.com.atromoby.widgets.RootActivity;
import tw.com.lixin.wmessenger.global.LocalFilter;


public class MainActivity extends RootActivity{

    private CustomInput userInput, passInput;
    private TextView errorTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.userInput);
        passInput = findViewById(R.id.passInput);
        errorTxt = findViewById(R.id.errorTxt);
        localReceiver.registerReceiver(this,LocalFilter.LOGIN);

        clicked(R.id.loginButton, view -> {
            errorTxt.setText("Wait for login...");
            SmackService.login(this, userInput.getRawText(),passInput.getRawText());
        });
    }

    private LocalReceiver localReceiver = new LocalReceiver() {
        @Override
        public void onReceive(LocalIntent localIntent) {
            String noo = localIntent.getObject(String.class);
            errorTxt.setText(noo);
            if(noo.equals("okay")){
                toActivity(FriendActivity.class, Animate.FADE);
            }
        }
    };
}
