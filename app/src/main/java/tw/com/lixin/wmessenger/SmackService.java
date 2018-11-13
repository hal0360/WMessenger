package tw.com.lixin.wmessenger;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.PresenceEventListener;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.RosterLoadedListener;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import tw.com.atromoby.utils.LocalIntent;
import tw.com.atromoby.utils.LocalReceiver;
import tw.com.lixin.wmessenger.global.LocalFilter;
import tw.com.lixin.wmessenger.models.User;

public class SmackService extends Service implements ConnectionListener{

    private static AbstractXMPPConnection xMPPconnection;
    private Roster roster;
    private static String user, pass;
    private Boolean creationError = false;
    private List<User> users;

    public SmackService() {
    }

    public static AbstractXMPPConnection getConnection(){
        return xMPPconnection;
    }

    public static void login(Context context, String username, String password){
        user = username;
        pass = password;
        context.startService(new Intent(context, SmackService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XMPPTCPConnectionConfiguration connConfig;
        try {
            connConfig = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain("211-75-195-57.hinet-ip.hinet.net")// Name of your Host
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .build();
            xMPPconnection = new XMPPTCPConnection(connConfig);
            xMPPconnection.addConnectionListener(this);
        } catch (XmppStringprepException e) {
            creationError = true;
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(creationError){
            LocalReceiver.send(this, new LocalIntent(LocalFilter.LOGIN,"creation error!!"));
            return START_NOT_STICKY;
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, "WM_CHANNEL_ID")
                .setContentTitle("WMessenging")
                .setContentText("Recieving incoming XMPP connection...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        if(xMPPconnection.isAuthenticated()) return Service.START_STICKY;

        xMPPconnection.disconnect();
        SmackTask smackTask = new SmackTask(xMPPconnection);
        smackTask.onFail(mss -> {
            LocalReceiver.send(this, new LocalIntent(LocalFilter.LOGIN,mss));
            Log.e("onFail", mss);
            creationError = true;
            stopSelf();
        });
        smackTask.login(user, pass);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(xMPPconnection != null) xMPPconnection.disconnect();
        super.onDestroy();
    }

    public static void logout(Context context) {
        context.stopService(new Intent(context,SmackService.class));
    }

    @Override
    public void connected(XMPPConnection connection) {
        Log.e("XMPPConnection", "connected");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        LocalReceiver.send(this, new LocalIntent(LocalFilter.LOGIN,"okay"));
    }

    @Override
    public void connectionClosed() {
        Log.e("XMPPConnection", "connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.e("connectionClosedOnError", e.getMessage());
    }
}
