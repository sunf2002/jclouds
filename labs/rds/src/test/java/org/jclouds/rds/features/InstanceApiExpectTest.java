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
 * Unles required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either expres or implied.  See the License for the
 * specific language governing permisions and limitations
 * under the License.
 */
package org.jclouds.rds.features;

import static org.jclouds.rds.options.ListInstancesOptions.Builder.afterMarker;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.TimeZone;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.rds.RDSApi;
import org.jclouds.rds.internal.BaseRDSApiExpectTest;
import org.jclouds.rds.parse.DescribeDBInstancesResponseTest;
import org.jclouds.rds.parse.GetInstanceResponseTest;
import org.jclouds.rest.ResourceNotFoundException;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "InstanceApiExpectTest")
public class InstanceApiExpectTest extends BaseRDSApiExpectTest {

   public InstanceApiExpectTest() {
      TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
   }

   HttpRequest get = HttpRequest.builder()
                                .method("POST")
                                .endpoint("https://rds.us-east-1.amazonaws.com/")
                                .addHeader("Host", "rds.us-east-1.amazonaws.com")
                                .payload(
                                   payloadFromStringWithContentType(
                                         "Action=DescribeDBInstances" +
                                               "&DBInstanceIdentifier=id" +
                                               "&Signature=jcvzapwazR2OuWnMILEt48ycu226NOn2AuEBKSxV2O0%3D" +
                                               "&SignatureMethod=HmacSHA256" +
                                               "&SignatureVersion=2" +
                                               "&Timestamp=2009-11-08T15%3A54%3A08.897Z" +
                                               "&Version=2012-04-23" +
                                               "&AWSAccessKeyId=identity",
                                         "application/x-www-form-urlencoded"))
                                .build();
   
   
   public void testGetWhenResponseIs2xx() throws Exception {

      HttpResponse getResponse = HttpResponse.builder().statusCode(200)
            .payload(payloadFromResourceWithContentType("/get_instance.xml", "text/xml")).build();

      RDSApi apiWhenExist = requestSendsResponse(
            get, getResponse);

      assertEquals(apiWhenExist.getInstanceApi().get("id").toString(), new GetInstanceResponseTest().expected().toString());
   }

   public void testGetWhenResponseIs404() throws Exception {

      HttpResponse getResponse = HttpResponse.builder().statusCode(404).build();

      RDSApi apiWhenDontExist = requestSendsResponse(
            get, getResponse);

      assertNull(apiWhenDontExist.getInstanceApi().get("id"));
   }

   HttpRequest list = HttpRequest.builder()
                                 .method("POST")
                                 .endpoint("https://rds.us-east-1.amazonaws.com/")
                                 .addHeader("Host", "rds.us-east-1.amazonaws.com")
                                 .payload(
                                    payloadFromStringWithContentType(
                                             "Action=DescribeDBInstances" +
                                             "&Signature=SnClCujZG9Sq9sMdf59xZWsjQxIbMOp5YEF%2FFBsurf4%3D" +
                                             "&SignatureMethod=HmacSHA256" +
                                             "&SignatureVersion=2" +
                                             "&Timestamp=2009-11-08T15%3A54%3A08.897Z" +
                                             "&Version=2012-04-23" +
                                             "&AWSAccessKeyId=identity",
                                          "application/x-www-form-urlencoded"))
                                 .build();
   
   public void testListWhenResponseIs2xx() throws Exception {

      HttpResponse listResponse = HttpResponse.builder().statusCode(200)
            .payload(payloadFromResourceWithContentType("/describe_instances.xml", "text/xml")).build();

      RDSApi apiWhenExist = requestSendsResponse(
            list, listResponse);

      assertEquals(apiWhenExist.getInstanceApi().list().get(0).toString(), new DescribeDBInstancesResponseTest().expected().toString());
   }
   
   public void testList2PagesWhenResponseIs2xx() throws Exception {

      HttpResponse listResponse = HttpResponse.builder().statusCode(200)
            .payload(payloadFromResourceWithContentType("/describe_instances_marker.xml", "text/xml")).build();

      HttpRequest list2 = HttpRequest.builder()
                                     .method("POST")
                                     .endpoint("https://rds.us-east-1.amazonaws.com/")
                                     .addHeader("Host", "rds.us-east-1.amazonaws.com")
                                     .payload(
                                        payloadFromStringWithContentType(
                                                 "Action=DescribeDBInstances" +
                                                 "&Marker=MARKER" +
                                                 "&Signature=TFW8vaU2IppmBey0ZHttbWz4rMFh%2F5ACWl6Xyt58sQU%3D" +
                                                 "&SignatureMethod=HmacSHA256" +
                                                 "&SignatureVersion=2" +
                                                 "&Timestamp=2009-11-08T15%3A54%3A08.897Z" +
                                                 "&Version=2012-04-23" +
                                                  "&AWSAccessKeyId=identity",
                                              "application/x-www-form-urlencoded"))
                                     .build();
      
      HttpResponse list2Response = HttpResponse.builder().statusCode(200)
               .payload(payloadFromResourceWithContentType("/describe_instances.xml", "text/xml")).build();

      RDSApi apiWhenExist = requestsSendResponses(
            list, listResponse, list2, list2Response);

      assertEquals(apiWhenExist.getInstanceApi().list().concat().toImmutableList(),
               ImmutableList.copyOf(Iterables.concat(new DescribeDBInstancesResponseTest().expected(),
                        new DescribeDBInstancesResponseTest().expected())));
   }
   
   // TODO: this should really be an empty set
   @Test(expectedExceptions = ResourceNotFoundException.class)
   public void testListWhenResponseIs404() throws Exception {

      HttpResponse listResponse = HttpResponse.builder().statusCode(404).build();

      RDSApi apiWhenDontExist = requestSendsResponse(
            list, listResponse);

      apiWhenDontExist.getInstanceApi().list().get(0);
   }
   
   public void testListWithOptionsWhenResponseIs2xx() throws Exception {
      HttpRequest listWithOptions =
            HttpRequest.builder()
                       .method("POST")
                       .endpoint("https://rds.us-east-1.amazonaws.com/")
                       .addHeader("Host", "rds.us-east-1.amazonaws.com")
                       .payload(payloadFromStringWithContentType(
                                                  "Action=DescribeDBInstances" +
                                                  "&Marker=MARKER" +
                                                  "&Signature=TFW8vaU2IppmBey0ZHttbWz4rMFh%2F5ACWl6Xyt58sQU%3D" +
                                                  "&SignatureMethod=HmacSHA256" +
                                                  "&SignatureVersion=2" +
                                                  "&Timestamp=2009-11-08T15%3A54%3A08.897Z" +
                                                  "&Version=2012-04-23" +
                                                  "&AWSAccessKeyId=identity",
                                            "application/x-www-form-urlencoded"))
                       .build();
      
      HttpResponse listWithOptionsResponse = HttpResponse.builder().statusCode(200)
               .payload(payloadFromResourceWithContentType("/describe_instances.xml", "text/xml")).build();

      RDSApi apiWhenWithOptionsExist = requestSendsResponse(listWithOptions,
               listWithOptionsResponse);

      assertEquals(apiWhenWithOptionsExist.getInstanceApi().list(afterMarker("MARKER")).toString(),
               new DescribeDBInstancesResponseTest().expected().toString());
   }
   
   HttpRequest delete = HttpRequest.builder()
            .method("POST")
            .endpoint("https://rds.us-east-1.amazonaws.com/")
            .addHeader("Host", "rds.us-east-1.amazonaws.com")
            .payload(
               payloadFromStringWithContentType(
                        "Action=DeleteDBInstance" +
                        "&DBInstanceIdentifier=id" +
                        "&Signature=3UFxv5zaG%2B3dZRAhinJZ10De0LAxkJtr3Nb6kfspC7w%3D" +
                        "&SignatureMethod=HmacSHA256" +
                        "&SignatureVersion=2" +
                        "&Timestamp=2009-11-08T15%3A54%3A08.897Z" +
                        "&Version=2012-04-23" +
                        "&AWSAccessKeyId=identity",
                     "application/x-www-form-urlencoded"))
            .build();

   public void testDeleteWhenResponseIs2xx() throws Exception {

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(200).build();

      RDSApi apiWhenExist = requestSendsResponse(delete, deleteResponse);

      apiWhenExist.getInstanceApi().delete("id");
   }

   public void testDeleteWhenResponseIs404() throws Exception {

      HttpResponse deleteResponse = HttpResponse.builder().statusCode(404).build();

      RDSApi apiWhenDontExist = requestSendsResponse(delete, deleteResponse);

      apiWhenDontExist.getInstanceApi().delete("id");
   }
}
