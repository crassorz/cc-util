package cc.gu.test;

public class CC_Utill {
	public static void main(String[] args) throws Throwable {
		try {
			System.out.println("" + (int)Double.NEGATIVE_INFINITY);
			System.out.println(Double.parseDouble("" + Double.NEGATIVE_INFINITY));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
