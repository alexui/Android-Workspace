import java.util.StringTokenizer;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String text= "am zece ani 10";
		System.out.println(text);
		StringTokenizer st = new StringTokenizer(text," ");
		String xx = st.nextToken();
		System.out.println(xx);
		String message= text.substring(text.indexOf(xx)+xx.length()+1);
		System.out.println(message);
	}

}
