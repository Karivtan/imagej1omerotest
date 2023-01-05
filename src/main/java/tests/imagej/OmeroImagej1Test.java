/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package tests.imagej;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import LeidenUniv.Omero.Open_Omero_Dataset;
import LeidenUniv.Omero.getOmeroDatasetAndAttachData;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import omero.gateway.model.ImageData;


/**
 * A template for processing each pixel of either
 * GRAY8, GRAY16, GRAY32 or COLOR_RGB images.
 *
 * @author Johannes Schindelin
 */
public class OmeroImagej1Test implements PlugIn {

	/**
	 * Main method for debugging.
	 *
	 * For debugging, it is convenient to have a method that starts ImageJ, loads
	 * an image and calls the plugin, e.g. after setting breakpoints.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) throws Exception {
		// start ImageJ
		new ImageJ();
		OmeroImagej1Test pp = new OmeroImagej1Test();
		pp.run(null);
	}

	@Override
	public void run(String arg) {
		// The part below is used for opening all images in a dataset, this only works directly from FIJI so you need to compile and add the plugin to the plugins folder
    	/* 
		Open_Omero_Dataset ood = new Open_Omero_Dataset();
        ood.run(null);
        */
		
        getOmeroDatasetAndAttachData godaad = new getOmeroDatasetAndAttachData();
    	Collection<ImageData> images = godaad.getImageCollection();// we now have a collection of images we can run through
    	if (images!=null){// if there are images
    		int counter=0;
    		Iterator<ImageData> image = images.iterator(); // create an iterator to go through images 
    		while (image.hasNext()) {// run through all the images
    			counter++;
    			ImageData data = image.next(); // gets the current image
    			IJ.log("Loading image "+counter+" of "+images.size()); // provides progress feedback for users
    			try {
                	//ImagePlus timp = godaad.openImagePlus(data.getId(), data.getGroupId()); // loads the image if needed
	                ResultsTable rt2 = new ResultsTable(); // create a result table with some data
	                rt2.incrementCounter();
	                rt2.addValue("Teststring", "test1");
	                rt2.addValue("Test", counter);
	                godaad.attachDataToImage(rt2, 1, data, "table name"); // this is to add a resultstable
	                godaad.attachDataToDataset(rt2, 1, "Named table"); // this is to add a resultstable to a dataset
	                // this part will create a file to attach
	                File file=null; 
	                try {
	                	file = new File("temp.txt");
	                    if (file.createNewFile()) {
	                      System.out.println("File created: " + file.getName());
	                    } else {
	                      System.out.println("File already exists.");
	                    }
	                  } catch (IOException e) {
	                    System.out.println("An error occurred.");
	                    e.printStackTrace();
	                  }
	                // till here
	                godaad.attachFile(file, "attached file", "Textfile", "txt", data); // this will attach the file to the image
               
                } catch (Exception e) {
                	 IJ.log("Error Loading image "+counter+" of "+images.size());
                	 IJ.log(e.getMessage());
                    	StackTraceElement[] t = e.getStackTrace();
                    	for (int i=0;i<t.length;i++){
                    		IJ.log(t[i].toString());
                    	}
                }
    			IJ.log("Finished image "+counter+" of "+images.size());
    		}
    	}
		godaad.closeConnection(); // needed to close omero connection
	}
}
