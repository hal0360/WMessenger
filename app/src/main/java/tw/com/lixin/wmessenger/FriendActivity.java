package tw.com.lixin.wmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.PresenceEventListener;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.RosterLoadedListener;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.Jid;

import java.util.Collection;

import tw.com.atromoby.widgets.RootActivity;

public class FriendActivity extends RootActivity implements RosterLoadedListener, SubscribeListener, RosterListener, PresenceEventListener {

    private Roster roster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        roster = Roster.getInstanceFor(SmackService.getConnection());
        roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
        roster.addRosterListener(this);
        roster.addPresenceEventListener(this);
        roster.addSubscribeListener(this);
        roster.addRosterLoadedListener(this);
    }

    @Override
    public void onRosterLoaded(Roster roster) {

    }

    @Override
    public void onRosterLoadingFailed(Exception exception) {

    }

    @Override
    public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
        return null;
    }

    @Override
    public void presenceAvailable(FullJid address, Presence availablePresence) {

    }

    @Override
    public void presenceUnavailable(FullJid address, Presence presence) {

    }

    @Override
    public void presenceError(Jid address, Presence errorPresence) {

    }

    @Override
    public void presenceSubscribed(BareJid address, Presence subscribedPresence) {

    }

    @Override
    public void presenceUnsubscribed(BareJid address, Presence unsubscribedPresence) {

    }

    @Override
    public void entriesAdded(Collection<Jid> addresses) {

    }

    @Override
    public void entriesUpdated(Collection<Jid> addresses) {

    }

    @Override
    public void entriesDeleted(Collection<Jid> addresses) {

    }

    @Override
    public void presenceChanged(Presence presence) {

    }
}
