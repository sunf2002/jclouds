/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.openstack.swift;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.swift.internal.BaseSwiftExpectTest;
import org.testng.annotations.Test;

/**
 * 
 * @author Adrian Cole
 */
@Test(testName = "SwiftClientExpectTest")
public class SwiftClientExpectTest extends BaseSwiftExpectTest<SwiftClient> {

   public void testContainerExistsWhenResponseIs2xxReturnsTrue() throws Exception {
      HttpRequest headContainer = HttpRequest.builder()
            .method("HEAD")
            .endpoint(swiftEndpointWithHostReplaced + "/foo")
            .addHeader("X-Auth-Token", authToken).build();

      HttpResponse headContainerResponse = HttpResponse.builder().statusCode(200).build();

      SwiftClient clientWhenContainerExists = requestsSendResponses(authRequest,
            authResponse, headContainer, headContainerResponse);

      assertTrue(clientWhenContainerExists.containerExists("foo"));
   }

   public void testContainerExistsWhenResponseIs404ReturnsFalse() throws Exception {
      HttpRequest headContainer = HttpRequest.builder()
            .method("HEAD")
            .endpoint(swiftEndpointWithHostReplaced + "/foo")
            .addHeader("X-Auth-Token", authToken).build();

      HttpResponse headContainerResponse = HttpResponse.builder().statusCode(404).build();

      SwiftClient clientWhenContainerDoesntExist = requestsSendResponses(authRequest,
            authResponse, headContainer, headContainerResponse);

      assertFalse(clientWhenContainerDoesntExist.containerExists("foo"));

   }

}
