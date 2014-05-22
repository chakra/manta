package com.espendwise.manta.web.util;

import com.espendwise.manta.util.alert.DisplayMessage;
import com.espendwise.manta.util.alert.MessageType;

import java.io.Serializable;
import java.util.*;


public class ActionMessages implements Serializable {

   protected Map<MessageType, List<DisplayMessage>> messages = new HashMap<MessageType, List<DisplayMessage>>();

    public ActionMessages() {
        super();
    }


    public ActionMessages(ActionMessages messages) {
        super();
        this.add(messages);
    }

   public void add(MessageType messageType, DisplayMessage message) {
       add(messageType, Arrays.asList(message));
    }

   public void add(MessageType messageType, List<? extends DisplayMessage> messageList) {

       List<DisplayMessage> list =  messages.get(messageType);

        if (list == null) {
            list = new ArrayList<DisplayMessage>();
            messages.put(messageType, list);
        }

        list.addAll(messageList);

    }


    public void add(ActionMessages messages) {

        if (messages == null) {
            return;
        }

        // loop over properties
        Collection<Map.Entry<MessageType, List<DisplayMessage>>> props = messages.properties();
        for (Map.Entry<MessageType, List<DisplayMessage>> entry : props) {
            add(entry.getKey(), entry.getValue());
        }
    }

    public void add(ActionMessage message) {

        if (message == null) {
            return;
        }

        add(message.getType(), message);
    }

    public List<DisplayMessage> get() {

        if (messages.isEmpty()) {
            return new ArrayList<DisplayMessage>();
        }

        List<DisplayMessage> results = new ArrayList<DisplayMessage>();

        for (List<DisplayMessage> displayMessages : messages.values()) {
            results.addAll(displayMessages);
        }

        return results;
    }

    public List<DisplayMessage> get(MessageType messageType) {

        List<DisplayMessage> items = messages.get(messageType);

        if (items == null) {
            return new ArrayList<DisplayMessage>();
        } else {
            return items;
        }

    }


    public Collection<Map.Entry<MessageType, List<DisplayMessage>>> properties() {

        if (messages.isEmpty()) {
            return new HashMap<MessageType, List<DisplayMessage>>().entrySet();
        }

        return messages.entrySet();

    }


    public int size() {

        int total = 0;

        for (List<?> objs : messages.values()) {
            total += objs.size();
        }

        return (total);

    }


	public int size(MessageType messageType) {

        List<DisplayMessage> items = get(messageType);

        return (items == null) ? 0 : items.size();
	}


    public void clear() {
        messages.clear();
    }

    public boolean isEmpty(){
        return (messages.isEmpty());
    }

    public String toString() {
        return this.messages.toString();
	}
}
