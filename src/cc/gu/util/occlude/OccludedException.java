package cc.gu.util.occlude;

public class OccludedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8044706278586641705L;
	public OccludedException() {
		super();
	}
	public OccludedException(String message) {
		super(message);
	}
	public OccludedException(Throwable cause) {
		super(cause);
	}
	public OccludedException(String message, Throwable cause) {
		super(message, cause);
	}
}
