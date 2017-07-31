package drat.proteus.service.licenses;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import drat.proteus.services.licenses.RatLogFile;

public class TestRatLogFile {

  private static String ratLogFileContents;
  private static final String ratLogFileName = "sample-rat.log";
  private final static String UNKNOWN_LIC = "!?????";
  private final String expectedDate = "2017-07-29T17:10:42-07:00";
  private static final String[] licenseTypes = { "Notes", "Binaries",
      "Archives", "Standards", "Apache Licensed", "Generated Documents" };
  private static final int[] licenseCounts = { 0, 0, 0, 12, 2, 0 };
  private static final String[] unapprovedLicFiles = {
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/4819c0134b20143ab9e5e5388c33fcf0-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/4c6c80630a1ea42303d015ca8153ec2d-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/82242fcc686f97db312f733ff2c914da-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/826eff522a572b7d1bc0df86a945bc8e-CONTRIBUTING.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/c8a69d1977bbe04d44462a9e98de7fc6-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/ebd586f9d3e9a0fe416e1bf0e2d5b81a-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/f240c690773715be31ab125bee9fb8c5-CONTRIBUTING.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/fd4342f0e1cc47ef3222d5db12807438-README.md" };

  private static final String[] ratLogFiles = {
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/421e7294043e0581cae0ced0bb35008b-LICENSE.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/4819c0134b20143ab9e5e5388c33fcf0-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/4c6c80630a1ea42303d015ca8153ec2d-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/61c9938c2576418a6de2a01002f6cd19-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/82242fcc686f97db312f733ff2c914da-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/826eff522a572b7d1bc0df86a945bc8e-CONTRIBUTING.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/c8a69d1977bbe04d44462a9e98de7fc6-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/d54b61d46665f629220de71c2d85201c-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/db6934b99687afc0427d80757583534e-LICENSE.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/ebd586f9d3e9a0fe416e1bf0e2d5b81a-README.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/f240c690773715be31ab125bee9fb8c5-CONTRIBUTING.md",
      "/Users/mattmann/drat/deploy/data/jobs/rat/1501373441424/input/fd4342f0e1cc47ef3222d5db12807438-README.md" };

  private static final String[] ratLogFileLicenses = { "AL", UNKNOWN_LIC,
      UNKNOWN_LIC, "MIT", UNKNOWN_LIC, UNKNOWN_LIC, UNKNOWN_LIC, "MIT", "AL",
      UNKNOWN_LIC, UNKNOWN_LIC, UNKNOWN_LIC };

  @BeforeClass
  public static void prepare() throws InstantiationException {
    String ratLogFilePath = TestRatLogFile.class.getResource(ratLogFileName)
        .getFile();
    try {
      ratLogFileContents = FileUtils.readFileToString(new File(ratLogFilePath));
    } catch (IOException e) {
      e.printStackTrace();
      throw new InstantiationException(
          "Unable to read RatLog: [" + ratLogFilePath + "]");
    }

  }

  @Test
  public void testReadGeneratedDate() {
    RatLogFile ratLog = new RatLogFile(null, ratLogFileContents);
    assertNotNull(ratLog);
    assertEquals(expectedDate, ratLog.getGeneratedDateStr());
  }

  @Test
  public void testReadLicenseCounts() {
    RatLogFile ratLog = new RatLogFile(null, ratLogFileContents);
    assertNotNull(ratLog);
    Map<String, Integer> licCounts = ratLog.getLicenseCounts();
    assertNotNull(licCounts);
    for (int i = 0; i < licenseTypes.length; i++) {
      int readLicCount = licCounts.get(licenseTypes[i]);
      assertEquals(readLicCount, licenseCounts[i]);
    }
  }

  @Test
  public void testReadUnapprovedLicFiles() {
    RatLogFile ratLog = new RatLogFile(null, ratLogFileContents);
    assertNotNull(ratLog);
    List<String> unapproved = ratLog.getUnapprovedLicensedFiles();
    assertNotNull(unapproved);
    assertEquals(Arrays.asList(unapprovedLicFiles), unapproved);
  }

  @Test
  public void testReadRatLogFileLicenses() {
    RatLogFile ratLog = new RatLogFile(null, ratLogFileContents);
    assertNotNull(ratLog);
    Map<String, String> ratLogLicenses = ratLog.getDetectedLicensesPerFile();
    assertNotNull(ratLogLicenses);
    assertNotNull(ratLogLicenses.keySet());
    assertEquals(ratLogFiles.length, ratLogLicenses.keySet().size());
    for (int i = 0; i < ratLogFiles.length; i++) {
      String ratLogFilePath = ratLogFiles[i];
      String ratLogDetectedLic = ratLogFileLicenses[i];
      assertEquals(ratLogDetectedLic, ratLogLicenses.get(ratLogFilePath));
    }
  }

  @AfterClass
  public static void after() {
    ratLogFileContents = null;
  }

}
