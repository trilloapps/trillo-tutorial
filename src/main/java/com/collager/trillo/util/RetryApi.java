package com.collager.trillo.util;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

/**
 * This class has the abstract retry logic.
 */
public class RetryApi {

	private int retryCount;
	private int waitTime;

	public RetryApi(int retryCount, int waitTime) {
		super();
		this.retryCount = retryCount;
		this.waitTime = waitTime;
	}

	/**
	 * This function will allow the calling function to try max till retryCount.
	 * @param <T>
	 * @param funcToCall
	 * @param condition
	 * @return result
	 */
	public <T> T runAndRetry (Callable<T> funcToCall, Predicate<T> condition) {
	      try {
	          while (true) {
	            T res = funcToCall.call();
	            if (condition.test(res)) {
	              if (retryCount > 0) {
	                retryCount--;
	                waitForNextRetry();
	              } else {
	                return res;
	              }
	            } else {
	              return res;
	            }
	          }
	      } catch (Exception e) {
	          throw new RuntimeException(e);
	      }
	  }

	private void waitForNextRetry() throws IOException {
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			throw new IOException();
		}
	}

	/**
     * This function will retry the function until it passes the condition and within the max retry count.
     * @param <T> type of the return type.
     * @param retry this object will have retry count and interval between 2 call and have the retry logic implemented.
     * @param funcToCall function to be called.
     * @param condition stop condition.
     */
    public static <T> T retryCall(RetryApi retry, Callable<T> funcToCall, Predicate<T> condition) {
      return retry.runAndRetry(funcToCall, condition);
    }

}
