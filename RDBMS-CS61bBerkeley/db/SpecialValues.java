package db;

enum SpecialValues {
	NOVALUE(0.0, "NOVALUE"), 
	NAN(Double.POSITIVE_INFINITY, "NaN");
	
	private final double value;
	private final String str;
	
	SpecialValues(double value, String str) {
		this.value = value;
		this.str = str;
		
	}
	
	double val() {
		return value;
	}
	
	static Object ifSpecGetVal(Object A) {
		 if (A.equals(SpecialValues.NOVALUE)) return SpecialValues.NOVALUE.val();
		 else if (A.equals(SpecialValues.NAN)) return SpecialValues.NAN.val();
		 else return A;
	}
	
	@Override
	public String toString() {
		return str;
	}
	
}
