/*
 * Copyright (C) 2016 Google Inc.
 *
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
 */

package com.google.copybara.git.gerritapi;

import com.google.copybara.RepoException;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception that maps to Gerrit Http error codes
 */
public class GerritApiException extends RepoException {

  private static final Map<Integer, ResponseCode> codeMap = new HashMap<>();

  private final ResponseCode responseCode;
  private final int exitCode;

  public GerritApiException(int exitCode, String message) {
    super(message);
    this.exitCode = exitCode;
    this.responseCode = parseResponseCode(exitCode);
  }

  public ResponseCode getResponseCode() {
    return responseCode;
  }

  public int getExitCode() {
    return exitCode;
  }

  /**
   * Gerrit known response codes.
   *
   * <p>Note that UNKNOWN will be used for any other not in this list.
   */
  public enum ResponseCode {
    UNKNOWN(0),
    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    CONFLICT(409),
    PRECONDITION_FAILED(412),
    UNPROCESSABLE_ENTITY(422);

    private final int code;

    ResponseCode(int code) {
      this.code = code;
      codeMap.put(code, this);
    }

    public int getCode() {
      return code;
    }
  }

  static ResponseCode parseResponseCode(int code) {
    ResponseCode responseCode = codeMap.get(code);
    return responseCode == null ? ResponseCode.UNKNOWN : responseCode;
  }
}
