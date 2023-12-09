/*
 * Copyright (c) 2020 Trillo Inc. All Rights Reserved THIS IS UNPUBLISHED PROPRIETARY CODE OF TRILLO
 * INC. The copyright notice above does not evidence any actual or intended publication of such
 * source code.
 *
 */



package com.collager.trillo.util;

import com.collager.trillo.pojo.Result;

public class PubSubApi extends BaseApi {

  public static Result publish(String topicId, Object message) {
    return remoteCallAsResult("PubSubApi", "publish", topicId, message);
  }

}


