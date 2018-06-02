package sg.activewealth.roboadvisor.infra.dto;

public class FilterDto {

	private String field;
	private Operetor operetor;
	private Object[] value;

	public enum Operetor {
		EQ, NE, LT, LE, GT, GE, LIKE, ILIKE, BETWEEN,IN;
	}

	public FilterDto() {
		this(null, Operetor.EQ, null);
	}

	public FilterDto(String field, Object value[]) {
		this(field, Operetor.EQ, value);
	}

	public FilterDto(String field, Operetor op, Object value[]) {
		this.field = field;
		this.operetor = op;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Operetor getOperator() {
		return operetor;
	}

	public void setOp(Operetor op) {
		this.operetor = op;
	}

	public Object[] getValue() {
		return value;
	}

	public void setValue(Object[] value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FilterDto [field=").append(field).append(", op=").append(operetor).append(", value=")
				.append(value).append("]");
		return builder.toString();
	}

}
