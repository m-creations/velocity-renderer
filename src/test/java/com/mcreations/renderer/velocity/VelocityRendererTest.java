package com.mcreations.renderer.velocity;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Reza Rahimi <rahimi@m-creations.com>
 *
 */
public class VelocityRendererTest {

	protected static VelocityRenderer renderer = new VelocityRenderer();

	@BeforeClass
	public static void init() {
	}

	@Test
	public void testRender() throws Exception {
		File destFile = Paths.get("/tmp/sample.test").toFile();
		if(destFile.exists())
			destFile.delete();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("additionalParams", Arrays.asList(new String[] { "Table1", "Table2" }));
		renderer.render("./src/", "/tmp/", ".*\\.vm", params);
		Assert.assertTrue("Rendered file does not find", destFile.exists());
	}
}
