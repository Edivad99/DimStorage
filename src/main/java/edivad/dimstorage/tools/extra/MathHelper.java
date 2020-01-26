package edivad.dimstorage.tools.extra;

public class MathHelper {
	
	
	public static int approachExpI(int a, int b, double ratio) {
        int r = (int) Math.round(approachExp(a, b, ratio));
        return r == a ? b : r;
    }
	
	public static int retreatExpI(int a, int b, int c, double ratio, int kick) {
        int r = (int) Math.round(retreatExp(a, b, c, ratio, kick));
        return r == a ? b : r;
    }
	
	/**
	 * @param a     The value
	 * @param b     The value to approach
	 * @param ratio The ratio to reduce the difference by
	 * @param c     The value to retreat from
	 * @param kick  The difference when a == c
	 * @return
	 */
	public static double retreatExp(double a, double b, double c, double ratio, double kick)
	{
		double d = (Math.abs(c - a) + kick) * ratio;
		if(d > Math.abs(b - a))
		{
			return b;
		}
		return a + Math.signum(b - a) * d;
	}
	
	 /**
     * @param a     The value
     * @param b     The value to approach
     * @param ratio The ratio to reduce the difference by
     * @return a+(b-a)*ratio
     */
    public static double approachExp(double a, double b, double ratio) {
        return a + (b - a) * ratio;
    }
}
