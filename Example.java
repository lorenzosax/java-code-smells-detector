
public interface Example extends ExampleParents, ExampleParents1 {

	public int x;
	public final Object y = 10;
	public final Object<Integer, String> z;
	
	public static void print(String output, int N) throws ExoticException;
	public abstract final static HashMap<Integer, String> getASimpleMap();
}