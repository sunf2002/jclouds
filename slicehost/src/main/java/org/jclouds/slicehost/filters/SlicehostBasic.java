/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.slicehost.filters;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.encryption.EncryptionService;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class SlicehostBasic implements HttpRequestFilter {
   private final String apikey;
   private final EncryptionService encryptionService;

   @Inject
   public SlicehostBasic(@Named(Constants.PROPERTY_IDENTITY) String apikey, EncryptionService encryptionService) {
      this.apikey = checkNotNull(apikey, "apikey");
      this.encryptionService = checkNotNull(encryptionService, "encryptionService");
   }

   @Override
   public void filter(HttpRequest request) throws HttpException {
      request.getHeaders().replaceValues("Authorization",
            Collections.singleton(String.format("Basic %s", encryptionService.base64(apikey.getBytes()))));
   }
}