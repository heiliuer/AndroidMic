package com.heiliuer.androidmic_server;

import com.heiliuer.androidmic_server.udpmulti.UdpMulticastSender;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    public void testUdpMulticastSender() {
        UdpMulticastSender.beginSend();
    }
}