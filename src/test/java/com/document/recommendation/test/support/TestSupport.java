package com.document.recommendation.test.support;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

public abstract class TestSupport {

    @Before
    public void setUpTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        setUp();
    }

    public abstract void setUp();

    public void assertEquals(final Object expected, final Object actual) {
        Assert.assertEquals(expected, actual);
    }

    public InOrder inOrder(final Object... mocks) {
        return Mockito.inOrder(mocks);
    }

    public <T> OngoingStubbing<T> when(final T methodCall) {
        return Mockito.when(methodCall);
    }

    public <T> T any(Class<T> clazz) {
        return Matchers.any(clazz);
    }

    public <T> T verify(T mock) {
        return Mockito.verify(mock);
    }

    public static <T> T verify(T mock, VerificationMode mode) {
        return Mockito.verify(mock, mode);
    }
    
    public static VerificationMode atLeastOnce() {
        return Mockito.atLeastOnce();
    }
    
    public static Stubber doReturn(Object toBeReturned) {
        return Mockito.doReturn(toBeReturned);
    }
    
    public static <T> T spy(T object) {
        return  Mockito.spy(object);
    }
    
    public static VerificationMode atMost(int maxNumberOfInvocations) {
        return Mockito.atMost(maxNumberOfInvocations);
    }
    
    public static VerificationMode times(int wantedNumberOfInvocations) {
        return Mockito.times(wantedNumberOfInvocations);
    }
    
    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
    	Assert.assertThat("", actual, matcher);
    }
    
    public static <T> void assertThat(String reason, T actual,
            Matcher<? super T> matcher) {
        Assert.assertThat(reason, actual, matcher);
    }
    
    public static <T> Matcher<T> equalTo(T operand) {
        return CoreMatchers.equalTo(operand);
      }
    
}
