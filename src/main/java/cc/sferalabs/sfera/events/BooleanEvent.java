package cc.sferalabs.sfera.events;


public class BooleanEvent extends Event {
	
	private final Boolean value;

	public BooleanEvent(Node source, String id, Boolean value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

}
