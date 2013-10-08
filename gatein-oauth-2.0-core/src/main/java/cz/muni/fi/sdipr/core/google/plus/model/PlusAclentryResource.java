/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-09-16 16:01:30 UTC)
 * on 2013-10-08 at 00:29:17 UTC 
 * Modify at your own risk.
 */

package cz.muni.fi.sdipr.core.google.plus.model;

/**
 * Model definition for PlusAclentryResource.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the Google+ API. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class PlusAclentryResource extends com.google.api.client.json.GenericJson {

  /**
   * A descriptive name for this entry. Suitable for display.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String displayName;

  /**
   * The ID of the entry. For entries of type "person" or "circle", this is the ID of the resource.
   * For other types, this property is not set.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String id;

  /**
   * The type of entry describing to whom access is granted. Possible values are: - "person" -
   * Access to an individual.  - "circle" - Access to members of a circle.  - "myCircles" - Access
   * to members of all the person's circles.  - "extendedCircles" - Access to members of all the
   * person's circles, plus all of the people in their circles.  - "domain" - Access to members of
   * the person's Google Apps domain.  - "public" - Access to anyone on the web.
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private String type;

  /**
   * A descriptive name for this entry. Suitable for display.
   * @return value or {@code null} for none
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * A descriptive name for this entry. Suitable for display.
   * @param displayName displayName or {@code null} for none
   */
  public PlusAclentryResource setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * The ID of the entry. For entries of type "person" or "circle", this is the ID of the resource.
   * For other types, this property is not set.
   * @return value or {@code null} for none
   */
  public String getId() {
    return id;
  }

  /**
   * The ID of the entry. For entries of type "person" or "circle", this is the ID of the resource.
   * For other types, this property is not set.
   * @param id id or {@code null} for none
   */
  public PlusAclentryResource setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * The type of entry describing to whom access is granted. Possible values are: - "person" -
   * Access to an individual.  - "circle" - Access to members of a circle.  - "myCircles" - Access
   * to members of all the person's circles.  - "extendedCircles" - Access to members of all the
   * person's circles, plus all of the people in their circles.  - "domain" - Access to members of
   * the person's Google Apps domain.  - "public" - Access to anyone on the web.
   * @return value or {@code null} for none
   */
  public String getType() {
    return type;
  }

  /**
   * The type of entry describing to whom access is granted. Possible values are: - "person" -
   * Access to an individual.  - "circle" - Access to members of a circle.  - "myCircles" - Access
   * to members of all the person's circles.  - "extendedCircles" - Access to members of all the
   * person's circles, plus all of the people in their circles.  - "domain" - Access to members of
   * the person's Google Apps domain.  - "public" - Access to anyone on the web.
   * @param type type or {@code null} for none
   */
  public PlusAclentryResource setType(String type) {
    this.type = type;
    return this;
  }

  @Override
  public PlusAclentryResource set(String fieldName, Object value) {
    return (PlusAclentryResource) super.set(fieldName, value);
  }

  @Override
  public PlusAclentryResource clone() {
    return (PlusAclentryResource) super.clone();
  }

}
