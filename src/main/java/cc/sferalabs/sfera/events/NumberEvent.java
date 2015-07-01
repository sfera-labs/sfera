package cc.sferalabs.sfera.events;

public class NumberEvent extends BaseEvent {

	private final Double value;

	public NumberEvent(Node source, String id, Double value) {
		super(source, id);
		this.value = value;
	}

	public NumberEvent(Node source, String id, Integer value) {
		this(source, id, value == null ? null : value.doubleValue());
	}

	@Override
	public Double getValue() {
		return value;
	}

}
