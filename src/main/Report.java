package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Report {
	private String metricsFile;
	private String smellsFile;
	
	public Report(String metricsFileName, String smellsFileName) throws IOException {
		File metricsFile = new File(metricsFileName);
		File smellsFile = new File(smellsFileName);		
		Files.deleteIfExists(metricsFile.toPath());
		Files.deleteIfExists(smellsFile.toPath());
		this.metricsFile = metricsFileName;
		this.smellsFile = smellsFileName;
	}

	public void appendMetric(String string) {
		try {
			BufferedWriter metricsWriter = new BufferedWriter(new FileWriter(metricsFile, true));
			metricsWriter.write(string);
			metricsWriter.newLine();
			metricsWriter.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void appendSmell(String string) {
		try {
			BufferedWriter smellsWriter = new BufferedWriter(new FileWriter(smellsFile, true));
			smellsWriter.write(string);
			smellsWriter.newLine();
			smellsWriter.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
