package uk.ac.standrews.rchc.paxos_practical;

public class Acceptor extends PaxosProcess {

    public Acceptor() {
        this.processType = "Acceptor";
    }

    @Override
    public boolean nextTask() {
        Message msg = MessagePool.receive(this.getProcessId());
        if (msg != null) {
            this.log(msg);
            Message.MessageType messageType = msg.getMessageType();
            switch (messageType) {
                case dummy: {
                    MessagePool.send(new Message(this.getProcessId(), msg.getFrom(), Message.MessageType.dummy));
                }
                ;
                break;
                default: {
                    log("incorrect message <" + msg.getMessageType() + "> received");
                    return true;
                }
            }

        } else {
            // acceptor only sends messages in response to one received
        }

        return false;
    }
}
