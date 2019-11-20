package ua.nure.kn.akhremenko.usermanagement.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import ua.nure.kn.akhremenko.usermanagement.User;
import ua.nure.kn.akhremenko.usermanagement.db.DaoFactory;
import ua.nure.kn.akhremenko.usermanagement.db.DatabaseException;
import ua.nure.kn.akhremenko.usermanagement.gui.controller.BrowseController;

import java.util.Collection;

public class SearchAgent extends Agent {

    private AID[] aids;

    @Override
    protected void setup() {
        super.setup();

        DFAgentDescription description = new DFAgentDescription();
        description.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName("JADE-searching");
        serviceDescription.setType("searching");
        description.addServices(serviceDescription);

        try {
            DFService.register(this, description);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        addBehaviour(new TickerBehaviour(this, 60000) {
            @Override
            protected void onTick() {
                DFAgentDescription agentDescription = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();
                serviceDescription.setType("searching");
                agentDescription.addServices(serviceDescription);
                try {
                    DFAgentDescription[] descriptions = DFService.search(myAgent, agentDescription);
                    aids = new AID[descriptions.length];
                    for (int i = 0; i < descriptions.length; i++) {
                        DFAgentDescription d = descriptions[i];
                        aids[i] = d.getName();
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });

        addBehaviour(new RequestServer());

        System.out.println("INFO: " + getAID().getName() + " started. AID: " + getAID());
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("INFO: " + getAID().getName() + " stopped");
        super.takeDown();
    }

    public void search(String firstName, String lastName) throws SearchException {
        System.out.println("INFO: searching for \"" + firstName + " " + lastName + "\"");
        try {
            Collection<User> users = DaoFactory.getInstance().getUserDao().find(firstName, lastName);
            if (users.size() > 0) {
                showUsers(users);
            } else {
                addBehaviour(new SearchRequestBehaviour(aids, firstName, lastName));
            }
        } catch (DatabaseException e) {
            throw new SearchException(e);
        }
    }

    public void showUsers(Collection<User> users) {
        BrowseController.setUsers(users);
    }

}
