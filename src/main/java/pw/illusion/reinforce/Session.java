package pw.illusion.reinforce;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class Session {
    public static final Map<UUID, Session> sessionMap = new HashMap<>();
    public final int targetedItemHash;
    public final double price;
}
