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
package org.jclouds.vcloud.director.v1_5.features.admin;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.EndpointParam;
import org.jclouds.rest.annotations.ExceptionParser;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.binders.BindToXMLPayload;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.domain.AdminCatalog;
import org.jclouds.vcloud.director.v1_5.domain.Owner;
import org.jclouds.vcloud.director.v1_5.domain.params.PublishCatalogParams;
import org.jclouds.vcloud.director.v1_5.features.CatalogAsyncApi;
import org.jclouds.vcloud.director.v1_5.features.MetadataAsyncApi;
import org.jclouds.vcloud.director.v1_5.filters.AddVCloudAuthorizationToRequest;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * @see AdminCatalogApi
 * @author danikov
 */
@RequestFilters(AddVCloudAuthorizationToRequest.class)
public interface AdminCatalogAsyncApi extends CatalogAsyncApi {
   
   /**
    * @see AdminApi#createCatalog(URI, AdminCatalog)
    */
   @POST
   @Path("/catalogs")
   @Consumes(VCloudDirectorMediaType.ADMIN_CATALOG)
   @Produces(VCloudDirectorMediaType.ADMIN_CATALOG)
   @JAXBResponseParser
   ListenableFuture<AdminCatalog> createCatalog(@EndpointParam URI orgRef, 
         @BinderParam(BindToXMLPayload.class) AdminCatalog catalog);

   /**
    * @see AdminApi#getCatalog(URI)
    */
   @Override
   @GET
   @Consumes
   @JAXBResponseParser
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<AdminCatalog> getCatalog(@EndpointParam URI catalogRef);

   /**
    * @see AdminApi#getCatalog(URI)
    */
   @PUT
   @Consumes(VCloudDirectorMediaType.ADMIN_CATALOG)
   @Produces(VCloudDirectorMediaType.ADMIN_CATALOG)
   @JAXBResponseParser
   ListenableFuture<AdminCatalog> updateCatalog(@EndpointParam URI catalogRef, 
         @BinderParam(BindToXMLPayload.class) AdminCatalog catalog);
   
   /**
    * @see AdminApi#deleteCatalog(URI)
    */
   @DELETE
   @Consumes
   @JAXBResponseParser
   ListenableFuture<Void> deleteCatalog(@EndpointParam URI catalogRef);
   
   /**
    * @see AdminApi#getOwner(URI)
    */
   @GET
   @Path("/owner")
   @Consumes
   @JAXBResponseParser
   @ExceptionParser(ReturnNullOnNotFoundOr404.class)
   ListenableFuture<Owner> getOwner(@EndpointParam URI catalogRef);
   
   /**
    * @see AdminApi#setOwner(URI, Owner)
    */
   @PUT
   @Path("/owner")
   @Consumes
   @Produces(VCloudDirectorMediaType.OWNER)
   @JAXBResponseParser
   ListenableFuture<Void> setOwner(@EndpointParam URI catalogRef,
         @BinderParam(BindToXMLPayload.class) Owner newOwner);
   
   /**
    * @see AdminApi#AdminApi(URI, PublishCatalogParams)
    */
   @POST
   @Path("/action/publish")
   @Consumes
   @Produces(VCloudDirectorMediaType.PUBLISH_CATALOG_PARAMS)
   @JAXBResponseParser
   ListenableFuture<Void> publishCatalog(@EndpointParam URI catalogRef,
         @BinderParam(BindToXMLPayload.class) PublishCatalogParams params);

   /**
    * @return synchronous access to {@link Metadata.Writeable} features
    */
   @Override
   @Delegate
   MetadataAsyncApi.Writeable getMetadataApi();

}
