package tw.com.lixin.wmessenger;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;

import tw.com.lixin.wmessenger.interfaces.CmdStr;

public class SmackTask extends AsyncTask<String,String,String>
{
    private AbstractXMPPConnection connection;
    private CmdStr cmdStr;
    private String pass, user;

    public SmackTask(AbstractXMPPConnection con) {
        connection = con;
    }

    public void login(String user, String pass){
        this.user = user;
        this.pass = pass;
        execute((String) null);
    }

    public void onFail(CmdStr cmdStr){
        this.cmdStr = cmdStr;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            connection.connect();
            connection.login(user, pass);
            return "ok";
        } catch (Exception e) {
            Log.e("SmackException",e.getMessage());
            return e.getMessage();
        }

        /*
        AccountManager accountManager = AccountManager.getInstance(connection);
        accountManager.sensitiveOperationOverInsecureConnection(true);
        try {
            accountManager.createAccount(Localpart.from("warboss"), "warhammer");   // Skipping optional fields like email, first name, last name, etc..
        } catch (SmackException.NoResponseException e) {
            Log.e("5Exception",e.getMessage());
            hoty = "5";
        } catch (XMPPException.XMPPErrorException e) {
            Log.e("6Exception",e.getMessage());
            hoty = "6";
        } catch (SmackException.NotConnectedException e) {
            Log.e("7Exception",e.getMessage());
            hoty = "7";
        } catch (InterruptedException e) {
            Log.e("8Exception",e.getMessage());
            hoty = "8";
        } catch (XmppStringprepException e) {
            Log.e("9Exception",e.getMessage());
            hoty = "9";
        }*/

    }

    @Override
    protected void onPostExecute(String mss) {
        if(!mss.equals("ok")){
            cmdStr.exec(mss);
        }
    }
}

