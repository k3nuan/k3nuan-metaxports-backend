package com.myorg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import software.amazon.awscdk.core.App;

import java.io.IOException;

public class CdkTest {
    private final static ObjectMapper JSON =
        new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    public void testStack() throws IOException {
        App app = new App();
        //CdkStack stack = new CdkStack(app, "test");

        // synthesize the stack to a CloudFormation template
        //JsonNode actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());

        // Update once resources have been added to the stack
        //assertThat(actual.get("Resources")).isNull();
    }
}
