package uk.ac.standrews.rchc.paxos_practical;

import java.util.List;

public class Proposer extends PaxosProcess {

    private String proposal;
    private final int ballotId;
    private final List<Acceptor> acceptors;

    Proposer(List<Acceptor> acceptors, String proposal, int ballotId) {
        this.processType = "Proposer";
        this.acceptors = acceptors;
        this.proposal = proposal;
        this.ballotId = ballotId;
    }

    @Override
    public boolean nextTask() {
        Message msg = MessagePool.receive(this.getProcessId());
        if (msg != null) {
            this.log(msg);
            Message.MessageType messageType = msg.getMessageType();
            switch (messageType) {
                case dummy: {
                    MessagePool.send(new Message(this.getProcessId(), this.acceptors.get(0).getProcessId(), Message.MessageType.dummy));
                }
                ;
                break;

                default: {
                    log("incorrect message <" + msg.getMessageType() + "> received");
                    return true;
                }
            }

        } else {
            // no message received; send a single message as appropriate to proposer progress
            MessagePool.send(new Message(this.getProcessId(), this.acceptors.get(0).getProcessId(), Message.MessageType.dummy));
        }
        return false;

    }
}
