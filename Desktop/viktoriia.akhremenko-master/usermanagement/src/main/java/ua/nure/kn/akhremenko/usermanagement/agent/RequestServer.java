package ua.nure.kn.akhremenko.usermanagement.agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ua.nure.kn.akhremenko.usermanagement.User;
import ua.nure.kn.akhremenko.usermanagement.db.DaoFactory;
import ua.nure.kn.akhremenko.usermanagement.db.DatabaseException;

import java.time.LocalDate;
import java.util.*;

public class RequestServer extends CyclicBehaviour {
    @Override
    public void action() {
        ACLMessage message = myAgent.receive();
        if (message != null) {
            if (message.getPerformative() == ACLMessage.REQUEST) {
                myAgent.send(createReply(message));
            } else {
                Collection users = parseMessage(message);
                ((SearchAgent) myAgent).showUsers(users);
            }
        } else {
            block();
        }
    }

    private ACLMessage createReply(ACLMessage message) {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.INFORM);

        String content = message.getContent();
        StringTokenizer tokenizer = new StringTokenizer(content, ",");
        if (tokenizer.countTokens() == 2) {
            String firstName = tokenizer.nextToken();
            String lastName = tokenizer.nextToken();
            Collection<User> users;
            try {
                users = DaoFactory.getInstance().getUserDao().find(firstName, lastName);
            } catch (DatabaseException e) {
                e.printStackTrace();
                users = new ArrayList<>();
            }
            StringBuffer buffer = new StringBuffer();
            for (User user : users) {
                buffer.append(user.getId()).append(",");
                buffer.append(user.getFirstName()).append(",");
                buffer.append(user.getLastName()).append(",");
                buffer.append(user.getDateOfBirth()).append(";");
            }
            reply.setContent(buffer.toString());
        }

        return reply;
    }

    private Collection<User> parseMessage(ACLMessage message) {
        Collection<User> users = new LinkedList<>();

        String content = message.getContent();
        if (content != null) {
            StringTokenizer semicolonTokenizer = new StringTokenizer(content, ";");
            while (semicolonTokenizer.hasMoreTokens()) {
                String userInfo = semicolonTokenizer.nextToken();

                StringTokenizer comaTokenizer = new StringTokenizer(userInfo, ",");
                String id = comaTokenizer.nextToken();
                String firstName = comaTokenizer.nextToken();
                String lastName = comaTokenizer.nextToken();
                String dateOfBirth = comaTokenizer.nextToken();

                users.add(new User(Long.valueOf(id), firstName, lastName, LocalDate.parse(dateOfBirth)));
            }
        }

        return users;
    }
}
