package tw.com.lixin.wmessenger.interfaces;

import org.jivesoftware.smack.AbstractXMPPConnection;

public interface CmdConnect {
    void exec(AbstractXMPPConnection connection);
}
