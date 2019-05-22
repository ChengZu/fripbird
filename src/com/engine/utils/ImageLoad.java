package com.engine.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageLoad {
	private Map<String, Image> images;
    public ImageLoad AddImage (String name, String path) throws FileNotFoundException, IOException
    {
    	BufferedImage image = ImageIO.read(new FileInputStream(path)); 
        this.images.put(name, image);
        return this;
    }
}
