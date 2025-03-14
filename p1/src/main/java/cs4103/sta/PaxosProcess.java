package uk.ac.standrews.rchc.paxos_practical;

public abstract class PaxosProcess {

    static protected int nextProcessId = 0;
    private final int processId;
    protected String processType;

    PaxosProcess() {
        this.processId = nextProcessId++;
    }

    abstract public boolean nextTask();

    public int getProcessId() {
        return processId;
    }

    protected void log(Message msg) {
        System.out.println(this.processType + " " + this.getProcessId() + ": message <" + msg.getMessageType() + "> received from process " + msg.getFrom());
    }

    protected void log(String s) {
        System.out.println(this.processType + " " + this.getProcessId() + ": " + s);
    }
}
