package tw.com.lixin.wmessenger;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
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

import tw.com.lixin.wmessenger.interfaces.CmdConnect;
import tw.com.lixin.wmessenger.models.User;

public class SmackService extends Service implements SubscribeListener, ConnectionListener, RosterListener, PresenceEventListener, RosterLoadedListener {

    private AbstractXMPPConnection xMPPconnection;
    private Roster roster;
    private static String user, pass;
    private Boolean creationError = false;
    private List<User> users;

    public SmackService() {
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
        if(creationError) return START_NOT_STICKY;

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
            Log.e("onFail", mss);
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

    private void sendMessage() {
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "me gayyy");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void presenceAvailable(FullJid address, Presence availablePresence) {
        Log.e("presenceAvailable", address.toString());
        sendMessage();
    }

    @Override
    public void presenceUnavailable(FullJid address, Presence presence) {
        Log.e("presenceUnavailable", address.toString());
    }

    @Override
    public void presenceError(Jid address, Presence errorPresence) {
        Log.e("presenceError", address.toString());
    }

    @Override
    public void presenceSubscribed(BareJid address, Presence subscribedPresence) {
        Log.e("presenceSubscribed", address.toString());
    }

    @Override
    public void presenceUnsubscribed(BareJid address, Presence unsubscribedPresence) {
        Log.e("presenceUnsubscribed", address.toString());
    }

    @Override
    public void entriesAdded(Collection<Jid> addresses) {
        Log.e("entriesAdded", addresses.toString());
    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {
        Log.e("entriesUpdated", addresses.toString());
    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {
        Log.e("entriesDeleted", addresses.toString());
    }

    @Override
    public void presenceChanged(Presence presence) {
        Log.e("presenceChanged", presence.toString());
    }

    @Override
    public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
        return null;
    }

    @Override
    public void onRosterLoaded(Roster roster) {

        Set<RosterEntry> entries = roster.getEntries();
        Log.e("onRosterLoaded", "sdssa");
        for (RosterEntry entry : entries) {

            Log.e("entry", "Name: "+entry.toString());

        }
    }

    @Override
    public void onRosterLoadingFailed(Exception exception) {
        Log.e("onRosterLoadingFailed", exception.getMessage());
    }

    @Override
    public void connected(XMPPConnection connection) {
        Log.e("XMPPConnection", "connected");
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        roster = Roster.getInstanceFor(xMPPconnection);
        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        roster.addRosterListener(this);
        roster.addPresenceEventListener(this);
        roster.addSubscribeListener(this);
        roster.addRosterLoadedListener(this);
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
