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
                    
                        // Add the ack to the acks list
                        acks.put(acceptors.get(0), true);

                        // Check if a majority of acceptors have acknowledged
                        if (acks.size() > acceptors.size() / 2) {
                            // Send value message to all acks
                            for (Acceptor acceptor: acks.keySet()) {
                                MessagePool.send(new Message(this.getProcessId(), acceptor.getProcessId(), Message.MessageType.accept, this.ballotId, this.proposal));
                            }

                            // Stop processing messages once 
                            return true;
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
            // MessagePool.send(new Message(this.getProcessId(), this.acceptors.get(0).getProcessId(), Message.MessageType.dummy));

            // send prepare message to all acceptors that are not in acks
            for (Acceptor acceptor : this.acceptors) {
                if (!acks.containsKey(acceptor)) {
                    MessagePool.send(new Message(this.getProcessId(), acceptor.getProcessId(), Message.MessageType.prepare, this.ballotId));
                }
            }

        }
        return false;

    }
}
