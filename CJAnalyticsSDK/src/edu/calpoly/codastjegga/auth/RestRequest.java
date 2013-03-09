/*
 * Copyright (c) 2011, salesforce.com, inc.
 * All rights reserved.
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of salesforce.com, inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission of salesforce.com, inc.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.calpoly.codastjegga.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;


/**
 * Modified version of Salesforce Android SDK. Only allows one to send data.
 * RestRequest: Class to represent any REST request.
 * 
 * The class offers factory methods to build RestRequest objects for all REST API actions:
 * <ul>
 * <li> create</li>
 * </ul>
 * 
 * It also has constructors to build any arbitrary request.
 * 
 */
public class RestRequest {

	/**
	 * Enumeration for all HTTP methods.
	 *
	 */
	public enum RestMethod {
		GET, POST, PUT, DELETE, HEAD, PATCH;
	}
	
	/**
	 * Enumeration for Create REST API action.
	 */
	private enum RestAction {
		CREATE("/services/data/%s/sobjects/%s"); 

		private final String pathTemplate;

		private RestAction(String uriTemplate) {
			this.pathTemplate = uriTemplate;
		}
		
		public String getPath(Object... args) {
			return String.format(pathTemplate, args);
		}
	}

	private final RestMethod method;
	private final String path;
	private final HttpEntity requestEntity;
	private final Map<String, String> additionalHttpHeaders;
	
	/**
	 * Generic constructor for arbitrary requests.
	 * 
	 * @param method				the HTTP method for the request (GET/POST/DELETE etc)
	 * @param path					the URI path, this will automatically be resolved against the users current instance host.
	 * @param httpEntity			the request body if there is one, can be null.
	 */
	public RestRequest(RestMethod method, String path, HttpEntity requestEntity) {
		this(method, path, requestEntity, null);
	}

	public RestRequest(RestMethod method, String path, HttpEntity requestEntity, Map<String, String> additionalHttpHeaders) {
		this.method = method;
		this.path = path;
		this.requestEntity = requestEntity;
		this.additionalHttpHeaders = additionalHttpHeaders;
	}
	
	/**
	 * @return HTTP method of the request.
	 */
	public RestMethod getMethod() {
		return method;
	}

	/**
	 * @return Path of the request.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return request HttpEntity 
	 */
	public HttpEntity getRequestEntity() {
		return requestEntity;
	}
	
	public Map<String, String> getAdditionalHttpHeaders() {
		return additionalHttpHeaders;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(method).append(" ").append(path);
		return sb.toString();
	}
	

	/**
	 * Request to create a record. 
	 * See http://www.salesforce.com/us/developer/docs/api_rest/index_Left.htm#StartTopic=Content/resources_sobject_retrieve.htm
	 * 
	 * @param apiVersion
	 * @param objectType
	 * @param fields
	 * @return a RestRequest
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static RestRequest getRequestForCreate(String apiVersion, String objectType, Map<String, Object> fields) throws UnsupportedEncodingException, IOException  {
		HttpEntity fieldsData = prepareFieldsData(fields); 
		return new RestRequest(RestMethod.POST, RestAction.CREATE.getPath(apiVersion, objectType), fieldsData);	
	}

	
	/**
	 * Jsonize map and create a StringEntity out of it 
	 * @param fields
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static StringEntity prepareFieldsData(Map<String, Object> fields)
			throws UnsupportedEncodingException {
		if (fields == null) {
			return null;
		}
		else {
			StringEntity entity = new StringEntity(new JSONObject(fields).toString(), HTTP.UTF_8);
			entity.setContentType("application/json"); 
			return entity;
		}
	}
}
