import java.util.Scanner;

class Multiply
{
	private static final boolean USE_KARATSUBA = true;

	public static void main (String[] args) throws java.lang.Exception
	{
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		while (n-- > 0) {
			Numeric x = (new Numeric(sc.next()));
			Numeric y = (new Numeric(sc.next()));

			if(x.equals(Numeric.ZERO) || y.equals(Numeric.ZERO)) {
				System.out.println(Numeric.ZERO);
				return;
			}
			Numeric result = Numeric.trim(vfmul(x, y));
			System.out.println(result);
		}
	}

	/**
	 * VFMUL : Very fast multiplication using Karatsuba's Algorithm
	 * @param x
	 * @param y
	 * @return
	 */
	private static Numeric vfmul(Numeric x, Numeric y) {

		if (x.getLength() == 1 && y.getLength() == 1) {
			return new Numeric(Integer.parseInt(x.getRepresentation()) * Integer.parseInt(y.getRepresentation()) + "");
		}
		
		Numeric[] xy = Numeric.equalize(x, y);
		x = getEvenizedDigits(Numeric.trim(xy[0]));
		y = getEvenizedDigits(Numeric.trim(xy[1]));
		xy = Numeric.equalize(x, y);
		x = xy[0]; y= xy[1];
		// here we assume both x and y are of same length
		Numeric lower = vfmul(x.getLowerHalf(), y.getLowerHalf());
		Numeric upper = vfmul(x.getUpperHalf(), y.getUpperHalf());
		
		Numeric middle = null; 
		if (!USE_KARATSUBA) {
			// This is non-karatsuba i.e. O(n^2)
			middle = Numeric.add(vfmul(x.getUpperHalf(), y.getLowerHalf()), vfmul(x.getLowerHalf(), y.getUpperHalf()));
		} else {
			// This is karatsuba code under-construction i.e. O(n^(log2(3)))
			Numeric xSum = Numeric.add(x.getUpperHalf(), x.getLowerHalf());
			Numeric ySum = Numeric.add(y.getLowerHalf(), y.getUpperHalf());
			Numeric p1 = vfmul(xSum,ySum);
			Numeric p2 = Numeric.add(lower, upper);
			middle = Numeric.sub(p1, p2);
		}
		Numeric result = Numeric.add(lower, Numeric.add(upper.leftShift(x.getLength()), middle.leftShift(x.getHalfLength())));
		return (result);
	}

	private static Numeric getEvenizedDigits(Numeric y) {
		if(y.getLength()%2!=0) {
			y = new Numeric("0"+y.getRepresentation());
		}
		return y;
	}
	
	private static class Numeric implements Comparable<Numeric>{
		public static Numeric ZERO = new Numeric("0");
		String rep; // representation
		int L; //length ;
		boolean isNegative;
		
		public boolean isNegative() {
			return isNegative;
		}
		
		public static Object zero() {
			return new Numeric("0");
		}

		public void setNegative(boolean isNegative) {
			this.isNegative = isNegative;
		}
		
		public static Numeric add(Numeric x, Numeric y){
			Numeric xy[] = Numeric.equalize(Numeric.trim(x), Numeric.trim(y));
			x = xy[0]; y = xy[1];
			StringBuffer res = new StringBuffer();
			int c = 0, s = 0;
			for(int i=x.getLength()-1 ; i>=0; i--) {
				// replace with faster technique
				s = (c + x.getValueAtIndex(i) + y.getValueAtIndex(i) )%10;
				c = (c + x.getValueAtIndex(i) + y.getValueAtIndex(i) )/10;
				res.append(String.valueOf(s));
			}
			if(c!=0){
				res.append(String.valueOf(c));
			}
			return new Numeric(res.reverse().toString());
		}
		
		public static Numeric sub(Numeric x, Numeric y) {
			Numeric xy[] = Numeric.equalize(x, y);
			x = xy[0]; y = xy[1];
			int cmp = x.compareTo(y);
			
			if(cmp>0) {
				StringBuffer res = new StringBuffer();
				int b = 0, d = 0;
				for(int i=x.getLength()-1 ; i>=0; i--) {
					// replace with faster technique
					
					int xDigit = x.getValueAtIndex(i);
					int yDigit = y.getValueAtIndex(i);
					d = xDigit - yDigit - b;
					if(d<0) {
						b=1; // borrow flag
						d = 10+d;
					} else {
						b = 0;
					}
					res.append(String.valueOf(d));
				}
				Numeric value = new Numeric(res.reverse().toString());
				
				return value;
				
			} else if(cmp<0) {
				Numeric subValue = sub(y,x);
				subValue.setNegative(true);
				return subValue;
				
			} else {
				return Numeric.ZERO;
			}
			
		}
		@Override
		public int compareTo(Numeric y) {
			if(Numeric.trim(this).getLength() > Numeric.trim(y).getLength()) {
				return 1;
			} else if(Numeric.trim(this).getLength() < Numeric.trim(y).getLength()) {
				return -1;
			}
			// compare
			for(int i=0 ; i<=this.getLength()-1; i++) {
				if(this.getValueAtIndex(i)>y.getValueAtIndex(i)) {
					return 1;
				} else if(this.getValueAtIndex(i)<y.getValueAtIndex(i)) {
					return -1;
				} else {
					continue;
				}
			}
			return 0;
		}

		public static Numeric trim(Numeric num) {
			for(int i=0;i<num.getRepresentation().length() ;i++) {
				if(num.getValueAtIndex(i) != 0) {
					return new Numeric(num.getRepresentation().substring(i));
				}
			}
			return Numeric.ZERO;
		}
		public static Numeric[] equalize(Numeric numeric, Numeric numeric2) {
			int diff = numeric.getLength() - numeric2.getLength();
			StringBuffer sb = new StringBuffer();
			if(diff>0) {
				while(diff-->0) {
					sb.append("0");
				}
				sb.append(numeric2);
				numeric2 = new Numeric(sb.toString());
			} else if (diff<0) {
				while(diff++<0) {
					sb.append("0");
				}
				sb.append(numeric);
				numeric = new Numeric(sb.toString());
			} else {
				// do nothing
			}

			return new Numeric[]{numeric,numeric2};
		}
		public Numeric(String representation) {
			this.rep = representation;
			this.L = rep.length();
		}
		
		private int getValueAtIndex(int i) {
			return Integer.parseInt(rep.charAt(i)+"");
		}
		public String getRepresentation() {
			return rep;
		}
		
		public int getLength() {
			return L;
		}
		public int getHalfLength(){
			return getLength()/2;
		}
		public Numeric getUpperHalf() {
			return new Numeric(this.rep.substring(0, getHalfLength()));
		}
		
		public Numeric getLowerHalf() {
			return new Numeric(this.rep.substring(getHalfLength(), L));
		}
		
		public Numeric leftShift(int shift) {
			StringBuffer addendum = new StringBuffer();
			while(shift-->0) {
				addendum.append("0");
			}
			return new Numeric(this.rep+addendum);
		}
		
		@Override
		public String toString() {
			return this.rep;
		}
	}
}
