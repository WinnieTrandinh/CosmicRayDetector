package cosmicRayDetector;
/**
 * 
 */

/**
 * @author WinnieTrandinh
 *
 */
public class Main {

	/**
	 * 
	 */
	private static String imagePath = "/images/testImage01.png";
	private static int threshold = 50;
	public static final int PIXELS_TO_SKIP = 1; 
	
	private static Screen screen = new Screen(200, 200, imagePath);
	private static Detector detector = new Detector(threshold, PIXELS_TO_SKIP);

		
	public Main() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Screen screen = new Screen(200, 200);
		screen.update();
		detector.findPixels();
		//screen.draw();
		//screen.foo();
		
		
	}

}
