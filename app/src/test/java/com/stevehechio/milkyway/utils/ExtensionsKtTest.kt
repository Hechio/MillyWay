package com.stevehechio.milkyway.utils

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
open class ExtensionsKtTest {
    @Test
    fun testDateConvertor(){
        val date =  "2017-12-08T00:00:00Z".toDate()
        Assert.assertEquals("2017-12-08", date)
    }
}