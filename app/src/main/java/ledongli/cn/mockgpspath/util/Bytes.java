package ledongli.cn.mockgpspath.util;

public class Bytes {
	/**
	 * will fill 8 byte into v from offset, include v[offset] self
	 * 
	 * @param v
	 * @param offset
	 * @param x
	 */
	private byte[] value_;

	public byte[] value() {
		return value_;
	}

	public Bytes(byte[] value) {
		if(value == null)
		{
			return;
		}
		value_ = new byte[value.length];
		System.arraycopy(value, 0, value_, 0, value.length);
	}

	public Bytes() {

	}

	public void initWithBytes(byte[] value) {
		value_ = new byte[value.length];
		System.arraycopy(value, 0, value_, 0, value.length);
	}

	public static void fill(byte[] v, int offset, long x) {
		v[offset + 0] = (byte) (x >> 56);
		v[offset + 1] = (byte) (x >> 48);
		v[offset + 2] = (byte) (x >> 40);
		v[offset + 3] = (byte) (x >> 32);
		v[offset + 4] = (byte) (x >> 24);
		v[offset + 5] = (byte) (x >> 16);
		v[offset + 6] = (byte) (x >> 8);
		v[offset + 7] = (byte) (x >> 0);
	}

	/**
	 * return 8 bytes
	 * 
	 * @param x
	 * @return
	 */
	public static byte[] bytes(long x) {
		byte[] v = new byte[8];
		v[0] = (byte) (x >> 56);
		v[1] = (byte) (x >> 48);
		v[2] = (byte) (x >> 40);
		v[3] = (byte) (x >> 32);
		v[4] = (byte) (x >> 24);
		v[5] = (byte) (x >> 16);
		v[6] = (byte) (x >> 8);
		v[7] = (byte) (x >> 0);

		return v;
	}

	public static long toLong(byte[] x) {
		return ((((long) x[0] & 0xff) << 56) | (((long) x[1] & 0xff) << 48)
				| (((long) x[2] & 0xff) << 40) | (((long) x[3] & 0xff) << 32)
				| (((long) x[4] & 0xff) << 24) | (((long) x[5] & 0xff) << 16)
				| (((long) x[6] & 0xff) << 8) | (((long) x[7] & 0xff) << 0));

	}

	public static double toDouble(byte[] x) {
		long result = ((((long) x[0] & 0xff) << 56) | (((long) x[1] & 0xff) << 48)
				| (((long) x[2] & 0xff) << 40) | (((long) x[3] & 0xff) << 32)
				| (((long) x[4] & 0xff) << 24) | (((long) x[5] & 0xff) << 16)
				| (((long) x[6] & 0xff) << 8) | (((long) x[7] & 0xff) << 0));
		return Double.longBitsToDouble(result);
	}

	public static void fill(byte[] v, int offset, double input) {
		long x = Double.doubleToLongBits(input);
		v[offset + 0] = (byte) (x >> 56);
		v[offset + 1] = (byte) (x >> 48);
		v[offset + 2] = (byte) (x >> 40);
		v[offset + 3] = (byte) (x >> 32);
		v[offset + 4] = (byte) (x >> 24);
		v[offset + 5] = (byte) (x >> 16);
		v[offset + 6] = (byte) (x >> 8);
		v[offset + 7] = (byte) (x >> 0);
	}

	public static byte[] bytes(double input) {
		long x = Double.doubleToLongBits(input);
		byte[] v = new byte[8];
		v[0] = (byte) (x >> 56);
		v[1] = (byte) (x >> 48);
		v[2] = (byte) (x >> 40);
		v[3] = (byte) (x >> 32);
		v[4] = (byte) (x >> 24);
		v[5] = (byte) (x >> 16);
		v[6] = (byte) (x >> 8);
		v[7] = (byte) (x >> 0);

		return v;
	}

	public static void fill(byte[] v, int offset, int x) {
		v[offset + 0] = (byte) (x >> 24);
		v[offset + 1] = (byte) (x >> 16);
		v[offset + 2] = (byte) (x >> 8);
		v[offset + 3] = (byte) (x >> 0);
	}

	public static byte[] bytes(int x) {
		byte[] v = new byte[4];
		v[0] = (byte) (x >> 24);
		v[1] = (byte) (x >> 16);
		v[2] = (byte) (x >> 8);
		v[3] = (byte) (x >> 0);

		return v;
	}

	public static int toInt(byte[] x) {
		return ((((int) x[0] & 0xff) << 24) | (((int) x[1] & 0xff) << 16)
				| (((int) x[2] & 0xff) << 8) | (((int) x[3] & 0xff) << 0));

	}

	public static float toFloat(byte[] x) {
		int result = ((((int) x[0] & 0xff) << 24) | (((int) x[1] & 0xff) << 16)
					| (((int) x[2] & 0xff) << 8) | (((int) x[3] & 0xff) << 0));
		return Float.intBitsToFloat(result);

	}

	public static void fill(byte[] v, int offset, float input) {
		int x = Float.floatToIntBits(input);
		v[offset + 0] = (byte) (x >> 24);
		v[offset + 1] = (byte) (x >> 16);
		v[offset + 2] = (byte) (x >> 8);
		v[offset + 3] = (byte) (x >> 0);
	}

	public static byte[] bytes(float input) {
		int x = Float.floatToIntBits(input);
		byte[] v = new byte[4];
		v[0] = (byte) (x >> 24);
		v[1] = (byte) (x >> 16);
		v[2] = (byte) (x >> 8);
		v[3] = (byte) (x >> 0);

		return v;
	}

	public static void fill(byte[] v, int offset, short x) {
		v[offset + 0] = (byte) (x >> 8);
		v[offset + 1] = (byte) (x >> 0);
	}

	public static byte[] bytes(short x) {
		byte[] v = new byte[2];
		v[0] = (byte) (x >> 8);
		v[1] = (byte) (x >> 0);

		return v;
	}

	public static short toShort(byte[] x) {
		return (short) ((x[0] << 8) | (x[1] & 0xff));

	}

	public static void fill(byte[] v, int offset, char x) {
		v[offset + 0] = (byte) (x >> 8);
		v[offset + 1] = (byte) (x >> 0);
	}

	public static byte[] bytes(char x) {
		byte[] v = new byte[2];
		v[0] = (byte) (x >> 8);
		v[1] = (byte) (x >> 0);

		return v;
	}

	public static char toChar(byte[] x) {
		return (char) ((x[0] << 8) | (x[1] & 0xff));

	}

	public static int compare(byte[] a, byte[] b) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] > b[i]) {
				return 1;
			} else if (a[i] < b[i]) {
				return -1;
			}
		}
		return 0;
	}
}
