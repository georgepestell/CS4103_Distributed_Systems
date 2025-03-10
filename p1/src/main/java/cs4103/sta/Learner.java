package cs4103.sta;

import java.util.List;

public class Learner extends PaxosProcess {

    private final List<Acceptor> acceptors;

    public Learner(List<Acceptor> acceptors) {
        this.processType = "Learner";
        this.acceptors = acceptors;
    }

    @Override
    public boolean nextTask() {
        Message msg = MessagePool.receive(this.getProcessId());
        if (msg != null) {
            this.log(msg);
            Message.MessageType messageType = msg.getMessageType();
            switch (messageType) {
                case dummy: {

                }
                ;
                case accept: {
                    
                }
                break;

                default: {
                    log("incorrect message <" + msg.getMessageType() + "> received");
                    return true;
                }
            }
        } else {
            MessagePool.send(new Message(this.getProcessId(), this.acceptors.get(1).getProcessId(), Message.MessageType.dummy));
        }
        return false;
    }
}
