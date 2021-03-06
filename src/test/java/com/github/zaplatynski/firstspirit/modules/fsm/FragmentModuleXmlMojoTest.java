package com.github.zaplatynski.firstspirit.modules.fsm;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.needle4j.annotation.InjectInto;
import org.needle4j.annotation.Mock;
import org.needle4j.annotation.ObjectUnderTest;
import org.needle4j.junit.NeedleBuilders;
import org.needle4j.junit.NeedleRule;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class FragmentModuleXmlMojoTest {

  @Rule
  public NeedleRule needleRule = NeedleBuilders
      .needleMockitoRule()
      .build();

  @Mock
  @InjectInto(targetComponentId = "testling", fieldName = "project")
  private MavenProject project;

  @Mock
  @InjectInto(targetComponentId = "testlingMissingParameterTarget", fieldName = "project")
  private MavenProject project2;

  @Mock
  @InjectInto(targetComponentId = "testlingMissingParameterSource", fieldName = "project")
  private MavenProject project3;

  @InjectInto(targetComponentId = "testling", fieldName = "serverScopes")
  private List<String> serverScopes = Arrays.asList("test:artifact");

  @InjectInto(targetComponentId = "testling", fieldName = "source")
  private String source = "/module-fragment.vm";

  @InjectInto(targetComponentId = "testlingMissingParameterTarget", fieldName = "source")
  private String source2 = source;

  @InjectInto(targetComponentId = "testling", fieldName = "target")
  private File target = new File("target/module-fragment.xml");

  @ObjectUnderTest(id = "testling")
  private FragmentModuleXmlMojo testling = new FragmentModuleXmlMojo();

  @ObjectUnderTest(id = "testlingMissingParameterTarget")
  private FragmentModuleXmlMojo testlingMissingParameterTarget = new FragmentModuleXmlMojo();

  @ObjectUnderTest(id = "testlingMissingParameterSource")
  private FragmentModuleXmlMojo testlingMissingParameterSource = new FragmentModuleXmlMojo();

  @Before
  public void setUp() throws Exception {
    target.createNewFile();
  }

  @Test
  public void execute() throws Exception {
    final Properties properties = new Properties();

    when(project.getArtifactId()).thenReturn("artifact");
    when(project.getName()).thenReturn("My FSM");
    when(project.getProperties()).thenReturn(properties);
    when(project.getBasedir()).thenReturn(new File("src/test/resources/"));

    testling.execute();

    assertThat(properties.keySet(), hasItem("serverScopes"));
    assertThat(properties.values(), hasItem(Arrays.asList("test:artifact")));
  }

  @Test(expected = MojoFailureException.class)
  public void executeWithMissingSource() throws Exception {
    testlingMissingParameterSource.execute();
  }

  @Test(expected = MojoFailureException.class)
  public void executeWithMissingTarget() throws Exception {
    testlingMissingParameterTarget.execute();
  }

}