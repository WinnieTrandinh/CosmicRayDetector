package cosmicRayDetector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Last Modified: January 23, 2018. 
 * <p>
 * This program takes in images of potential cosmic rays
 * and identifies them. 
 * <br>
 * The direction and area of the ray is then stored in a text file.
 * @author Winnie Trandinh
 */

public class Main {

	//strings to store file paths
	private static String imagePath = "/images/testImage";
	private static String textPath = "Info.txt";
	//boolean controlling whether the text file should be wiped at start or not
	private static boolean clear = true;
	//number of images to process
	private static int numImages = 9;
	//pixels need to be higher than the threshold to be considered as a gamma pixel
	private static int threshold = 200;
	//minimum values to eliminate false blobs
	private static final double MIN_DENSITY = 0.4;
	private static final double MIN_AREA = 20;
	//amount of pixels to skip when scanning through images
	public static int pixelsToSkip = 1; 
	//delay after processing each image for debugging purposes
	private static final long IMAGE_DELAY = 0;
	
	//variables shared across threads
	//images contain list of all the indexes of images that still need to be processed
	private volatile static ArrayList<Integer> images = new ArrayList<Integer>();
	//arrayList containing all the running threads
	private volatile static ArrayList<Boolean> running = new ArrayList<Boolean>();	
	
	/**
	 * Constructor for main class.
	 */
	public Main() {

	}

	/**
	 * Main function in the program.
	 * <br>
	 * This is called once when the program starts.
	 * @param args
	 */
	public static void main(String[] args) {
		//timer used to store the start time of the program
		long timer = System.currentTimeMillis();
		
		//if clear boolean == true, then clear the info.txt file
		if (clear) {
			writeFile("", true);
		}
		
		//populate the images arrayList with numbers for each image
		for (int i = 1; i <= numImages; i++) {
			images.add(i);
		}
		
		//set the maximum amount of threads allowed to be created at once
		//change maximum value to optimize efficiency
		int maxThreads = Runtime.getRuntime().availableProcessors()*3;
		//System.out.println("max = " + maxThreads + " initial = " + initialThreads);
		
		//loops runs as long as there are images left to be processed
		while(images.size()>0) {
			//new ArrayList to hold images that are successfully processed
			ArrayList<Integer> delete = new ArrayList<Integer>();
	
			//for loop to iterate through each image
			for (Integer i : images) {
				//final int necessary for use in thread
				final int num = i;
				//gets the current amount of threads already running
				int numThread = Thread.getAllStackTraces().keySet().size();
				//if max thread limit is not yet reached
				if (numThread <= maxThreads) {
					//create a new thread
					Thread thread = new Thread(new Runnable() {
						
						//this is called by thread.start call
						@Override
						public void run() {
							//adds true to let the program know that a
							//separate thread is running
							running.add(true);
							//gets the info from the image
							String info = process(num);
							if (info.equals("error") ) {
								//error indicates a stack overflow error
								System.out.println("error at image" + num);
							} else {
								//image successfully processed
								//prepare to remove the image from the list
								delete.add(num);
								//builds the text and outputs into text file
								String text = "Image " + num + ": " + '\n' + info;
								writeFile(text);
							}
							//thread has finished running
							//remove the thread from the list of running threads
							running.remove(true);
						}
					
					});
					//starts the thread created above
					thread.start();
				} else {
					//run in main thread
					//do the same as above
					String info = process(num);
					if (info.equals("error") ) {
						System.out.println("error at image" + num);
					} else {
						delete.add(num);
						String text = "Image " + num + ": " + '\n' + info;
						writeFile(text);
					}
				}
				//causes delay between images for debug purposes
				try {
					Thread.sleep(IMAGE_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			//ensures that all threads are finished before proceeding
			while (running.size() != 0) {
				try {
					//System.out.println("sleeping");
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//remove all the images that are successfully processed
			images.removeAll(delete);
			//clear the arrayList of completed images
			delete.clear();
			//any images left are left because they encountered an error
			//therefore, increase the amount of pixels to skip to avoid stack overflow
			pixelsToSkip++;
			
		}
		//prints out the time taken to process all images
		System.out.println("time taken for " + numImages + " images = " + 
						(System.currentTimeMillis() - timer) ); 
	}
	
	/**
	 * This method begins the processing of the images.
	 * <br>
	 * It creates and calls other methods to get the final data.
	 * @param num Image number.
	 * @return String containing data of image.
	 */
	private static String process(int num) {
		//System.out.println(" **** Image " + num + " ****");
		//constructs the screen and detector classes
		Screen screen = new Screen(200, 200);
		Detector detector = new Detector(threshold, pixelsToSkip,
										 MIN_DENSITY, MIN_AREA, screen);
		//build image path
		String imagePathNew = imagePath + Integer.toString(num) + ".png";
		//pass in the path to screen to set the image
		screen.setImage(imagePathNew, num);
		//calls the screen to update
		screen.update();
		//string to store the data from the image
		String info = (detector.findPixels() );
		//returns the data
		return info;
	}
	
	/**
	 * Method to write into text file.
	 * @param text Text to write.
	 */
	private synchronized static void writeFile(String text) {
		 try {
			//loads the file
			FileWriter fileWriter = new FileWriter(textPath, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            //System.out.println(text);
            //writes into text file
            writer.write(text);
            writer.newLine();

            //closes text file
            writer.close();
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        }
	}
	
	/**
	 * Overload of previous function.
	 * <br>
	 * Empties the text file.
	 * @param text Text to display.
	 * @param clear Boolean to indicate it to clear.
	 */
	private static void writeFile(String text, boolean clear) {
		try {
			FileWriter fileWriter = new FileWriter(textPath, false);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            //writes into text file
            writer.write(text);

            //closes text file
            writer.close();
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        }
	}

}
