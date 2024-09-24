package given;

public abstract class Channel {
    public abstract int read(byte[] bytes, int offset, int length) throws Exception;
    public abstract int write(byte[] bytes, int offset, int length) throws Exception;
    public abstract void disconnect();
    public abstract boolean disconnected();
}

