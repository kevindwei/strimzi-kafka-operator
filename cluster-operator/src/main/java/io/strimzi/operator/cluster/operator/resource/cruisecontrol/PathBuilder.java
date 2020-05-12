/*
 * Copyright Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.operator.cluster.operator.resource.cruisecontrol;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PathBuilder {

    String constructedPath;
    boolean firstParam;

    public PathBuilder(CruiseControlEndpoints endpoint) {
        constructedPath = endpoint.path + "?";
        firstParam = true;
    }

    public PathBuilder addParameter(String parameter) {
        if (!firstParam) {
            constructedPath += "&";
        } else {
            firstParam = false;
        }
        try {
            constructedPath += URLEncoder.encode(parameter, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return this;
    }

    public PathBuilder addParameter(CruiseControlParameters param, String value) {
        if (!firstParam) {
            constructedPath += "&";
        } else {
            firstParam = false;
        }
        try {
            constructedPath += param.asPair(value);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return this;
    }

    public PathBuilder addParameter(CruiseControlParameters param, List<String> values) {
        if (!firstParam) {
            constructedPath += "&";
        } else {
            firstParam = false;
        }
        try {
            constructedPath += param.asList(values);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return this;
    }

    public PathBuilder addRebalanceParameters(RebalanceOptions options) {
        if (options != null) {
            PathBuilder builder = addParameter(CruiseControlParameters.DRY_RUN, String.valueOf(options.isDryRun()))
                    .addParameter(CruiseControlParameters.VERBOSE, String.valueOf(options.isVerbose()))
                    .addParameter(CruiseControlParameters.SKIP_HARD_GOAL_CHECK, String.valueOf(options.isSkipHardGoalCheck()));

            if (options.getGoals() != null) {
                builder.addParameter(CruiseControlParameters.GOALS, options.getGoals());
            }
            return builder;
        } else {
            return this;
        }
    }

    public String build() {
        return constructedPath;
    }

}
