package core;

public class SubstitutionWrapper {

	public final boolean isUpdated;
	public final Expression expression;

	public SubstitutionWrapper(boolean isUpdated, Expression expression) {
		this.isUpdated = isUpdated;
		this.expression = expression;
	}

	public SubstitutionWrapper(boolean isUpdated, SubstitutionWrapper wrapper) {
		this.isUpdated = isUpdated || wrapper.isUpdated;
		this.expression = wrapper.expression;
	}
}
