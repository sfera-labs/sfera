package cc.sferalabs.sfera.core.events;

public class SystemStateEvent extends SystemEvent {

	private String state;

	public SystemStateEvent(String state) {
		super("state");
		this.state = state;
	}

	@Override
	public String getValue() {
		return state;
	}

}
