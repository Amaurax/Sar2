package given;

public abstract class Channel {
    public  int read(byte[] bytes, int offset, int length) throws Exception {
		return 0;
		}
    
    public  int write(byte[] bytes, int offset, int length) throws Exception {
		return 0;
    }
    
    public  void disconnect() {}
    
    public  boolean disconnected() {
		return false;
		
		}
}

