package com.mcreations.renderer.velocity;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Reza Rahimi <rahimi@m-creations.com>
 *
 */
public class VelocityRenderer {
	private static final Logger LOG = LoggerFactory.getLogger(VelocityRenderer.class);

	public static final String[][] OPTIONS = {
	      { "s", "src", "src-path", "Source path for velocity template files" },
	      { "f", "files", "files-wildcard", "Velocity template file names pattern, by example: *.vm" },
	      { "d", "dest", "destination-path", "Destination path for rendered velocity template files" }
	};

	private static final String APPLICATION_NAME = "VelocityRenderer";

	public static void main(String[] args) {
		VelocityRenderer velocityRenderer = new VelocityRenderer();
		String sourcePath = null;
		String destPath = null;
		String filePattern = null;
		/*
		 * create the command line parser
		 */
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		// create the Options
		Options options = new Options();
		for(String[] option : OPTIONS) {
			options.addOption(Option.builder(option[0]).argName(option[1]).type(String.class).longOpt(option[2]).hasArg(true).desc(option[3]).build());
		}
		/*
		 * Parse the command line options
		 */

		try {
			CommandLine line = parser.parse(options, args);
			/*
			 * Validate command lines options
			 */
			for(String[] option : OPTIONS) {
				if(!line.hasOption(option[2])) {
					formatter.printHelp(APPLICATION_NAME, options);
					System.exit(-1);
				}
			}

			sourcePath = line.getOptionValue("s");
			destPath = line.getOptionValue("d");
			filePattern = line.getOptionValue("f");
		} catch(ParseException e) {
			formatter.printHelp(APPLICATION_NAME, options);
		}

		/*
		 * Logging application arguments
		 */
		LOG.info("sourcePath=" + sourcePath);
		LOG.info("destPath=" + destPath);
		LOG.info("filePattern=" + filePattern);
		try {
			velocityRenderer.render(sourcePath, destPath, filePattern, null);
		} catch(Exception e) {
			LOG.error("Rendering velocity templats failed: ", e);
			System.exit(-2);
		}
	}

	public void render(String sourcePath, String destPath, String filePattern, Map<String, Object> additionalContextParameters) throws Exception {
		List<String> foundFiles = new ArrayList<String>();
		final String filePatternLocal = filePattern;
		Stream<Path> foundStream = Files.find(Paths.get(sourcePath),
		      Integer.MAX_VALUE, (path, basicFileAttributes) -> path.toFile().getName().matches(filePatternLocal));
		foundFiles = foundStream
		      .sorted()
		      .map(f -> f.getFileName().toString())
		      .collect(Collectors.toList());
		/*
		 * Velocity Engine init
		 */
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file,classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());
		ve.setProperty("input.encoding", "UTF-8");
		ve.setProperty("file.resource.loader.path", Paths.get(sourcePath).toString());
		ve.setProperty("runtime.log.logsystem.log4j.logger", VelocityRenderer.class.getName());
		ve.init();

		/*
		 * Prepare velocity context
		 */
		VelocityContext context = new VelocityContext();
		if(additionalContextParameters != null && !additionalContextParameters.isEmpty()) {
			for(String paramKey : additionalContextParameters.keySet()) {
				context.put(paramKey, additionalContextParameters.get(paramKey));
			}
		}
		context.put("envVars", System.getenv());
		/*
		 * Parsing templattes and writing in destination
		 */
		for(String foundFile : foundFiles) {

			Template t = ve.getTemplate(foundFile);
			File destFile = Paths.get(destPath, FilenameUtils.getBaseName(foundFile)).toFile();
			FileWriter writer = new FileWriter(destFile, false);
			// StringWriter writer = new StringWriter();
			t.merge(context, writer);
			writer.flush();
			writer.close();
			LOG.info("Rendered velocity template file saved here: " + destFile.getAbsolutePath());
			LOG.info("\n" + FileUtils.readFileToString(destFile, StandardCharsets.UTF_8));
		}
	}

}
