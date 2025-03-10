package cs4103.sta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proposer extends PaxosProcess {

    private String proposal;
    private final int ballotId;
    private final List<Acceptor> acceptors;
    private Map<Acceptor, Boolean> acks = new HashMap<>();

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
                case acknowledge: {
                    
                    
                        // Otherwise, send accept request with the proposal
                        for (Acceptor acceptor : this.acceptors) {
                            MessagePool.send(new Message(this.getProcessId(), acceptor.getProcessId(), Message.MessageType.accept, this.ballotId, this.proposal));
                        }

                    }
                }
                break;

                default: {
                    log("incorrect message <" + msg.getMessageType() + "> received");
                    return true;
                }
            }

        } else {
            // no message received; send a single message as appropriate to proposer progress
            MessagePool.send(new Message(this.getProcessId(), this.acceptors.get(0).getProcessId(), Message.MessageType.dummy));

            // send prepare message to all acceptors
            for (Acceptor acceptor : this.acceptors) {
                MessagePool.send(new Message(this.getProcessId(), acceptor.getProcessId(), Message.MessageType.prepare, this.ballotId));
            }

        }
        return false;

    }
}
