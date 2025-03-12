package cs4103.sta;

public class Acceptor extends PaxosProcess {

    // The highest ballot number that the acceptor has seen
    // All real ballot numbers are positve integers
    private int maxBallotId = -1;
    private String acceptedValue = null;

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
                case prepare: {
                    // Check if the number is greater than the current max ballot numer
                    if (msg.getBallotId() > this.maxBallotId) {

                        // Update max ballot number
                        this.maxBallotId = msg.getBallotId();

                        // Send acknowledge message including the highest accepted value if any
                        if (this.acceptedValue != null) {
                            MessagePool.send(new Message(this.getProcessId(), msg.getFrom(), Message.MessageType.acknowledge, msg.getBallotId(), this.acceptedvalue));
                        } else {
                            MessagePool.send(new Message(this.getProcessId(), msg.getFrom(), Message.MessageType.acknowledge, msg.getBallotId()));
                        }

                    } else {
                        // Ignore the message if the ballot number is not greater than previous ones
                    }
                };
                break;
                case accept: {
                    // Accept the value
                    this.acceptedValue = msg.getConsensusValue();
                };
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
