//package org.openmrs.module.htmlformentryrest;
//
//import org.apache.commons.beanutils.PropertyUtils;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.openmrs.api.context.Context;
//import org.openmrs.module.webservices.rest.SimpleObject;
//import org.openmrs.module.webservices.rest.test.Util;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.openmrs.module.webservices.rest.SimpleObject;
//
///**
// * Facilitates testing controllers.
// */
//public abstract class RestControllerTest extends RestControllerTestUtils {
//
//    @Before
//    public void setUp() throws Exception {
//        //executeDataSet("org/openmrs/module/idgen/include/TestData.xml");
//    }
//
//    @Test
//    public void shouldGetDefaultByUuid() throws Exception {
//        //MockHttpServletResponse response = handle(request(RequestMethod.GET, getURI() + "/" + getUuid()));
//        //SimpleObject result = deserialize(response);
//
//        //Assert.assertNotNull(result);
//        //Assert.assertEquals(getUuid(), PropertyUtils.getProperty(result, "uuid"));
//    }
//
//}
