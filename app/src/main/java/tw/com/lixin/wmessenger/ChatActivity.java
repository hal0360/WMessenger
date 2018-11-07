package tw.com.lixin.wmessenger;

import android.os.Bundle;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import tw.com.atromoby.widgets.RootActivity;

public class ChatActivity extends RootActivity implements IncomingChatMessageListener {

    private ChatManager chatManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

     //   chatManager = ChatManager.getInstanceFor(App.connection);
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {

    }

    private void sendMessage(String to, Message newMessage) {
        //Chat newChat = chatManager.chatWith();

        //newChat.sendMessage(newMessage);
    }
}
