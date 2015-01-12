package cc.sferalabs.sfera.events;


public class NumberEvent extends Event {
	
	private final Double value;

	public NumberEvent(Node source, String id, Double value) {
		super(source, id);
		this.value = value;
	}

	@Override
	public Double getValue() {
		return value;
	}

}
