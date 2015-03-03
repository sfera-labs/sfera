package cc.sferalabs.sfera.events;


public class StringEvent extends BaseEvent {
	
	private final String value;

	public StringEvent(Node source, String id, String value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
